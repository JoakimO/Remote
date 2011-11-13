package se.newbie.remote.boxee;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Notification;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoxeeRemoteDisplayFragment extends Fragment implements RemoteDisplay, JSONRPC2NotificationListener {
	private static final String TAG = "BoxeeDisplayFragment";
	
    private Handler handler;
    
	private BoxeeRemoteDevice boxeeRemoteDevice;
	private LinearLayout contentLayout;
	private MediaItem mediaItem;
	
	private enum InfoLabel {
		VideoTitle("VideoPlayer", "Title")
		, VideoGenre("VideoPlayer", "Genre")
		, VideoStudio("VideoPlayer", "Studio")
		, VideoDirector("VideoPlayer", "Director")
		, VideoPlotoutline("VideoPlayer", "Plotoutline")
		, VideoPlot("VideoPlayer", "Plot")
		, VideoYear("VideoPlayer", "Year")
		, VideoDuration("VideoPlayer", "Duration")
		
		, MusicTitle("MusicPlayer", "Title")
		, MusicArtist("MusicPlayer", "Artist")
		, MusicAlbum("MusicPlayer", "Album")
		, MusicGenre("MusicPlayer", "Genre")
		, MusicYear("MusicPlayer", "Year")
		, MusicDuration("MusicPlayer", "Duration")
		
		;
	
		
		
		String namespace;
		String name;
		
		InfoLabel(String namespace, String name) {
			this.namespace = namespace;
			this.name = name;
		}
		
		public String getNamespace() {
			return namespace;
		}
		
		public String getName() {
			return name;
		}
		
		public String getKey() {
			return namespace + "." + name;
		}
	}
	
	/**
	 * Need a empty constructor to handle recreate on orientation change
	 */
	public BoxeeRemoteDisplayFragment() {
	}
	
	public BoxeeRemoteDisplayFragment(BoxeeRemoteDevice boxeeRemoteDevice) {
		this.boxeeRemoteDevice = boxeeRemoteDevice;
	} 
	
	public String getIdentifier() {
		return "currentlyPlaying";
	}

	public Fragment getFragment() {
		return this;
	}	
    
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	getCurrentlyPlaying();
    	super.onResume();
    }	
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	Log.v(TAG, "onCreate");

    	super.onCreateView(inflater, container, savedInstanceState);

    	handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	updateFragment();
            }
        };	    	

        if (savedInstanceState != null) {
        	boxeeRemoteDevice = (BoxeeRemoteDevice)RemoteApplication.getInstance()
       			.getRemoteDeviceFactory().getRemoteDevice(savedInstanceState.getString("remote_device"));
        }
        
        View view = inflater.inflate(R.layout.standard_display_layout, container, false);
        contentLayout = (LinearLayout)view.findViewById(R.id.standard_display_content);
    	return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putString("remote_device", boxeeRemoteDevice.getIdentifier());
    }    
    
    
    private void updateFragment() {
    	contentLayout.removeAllViews();
    	Log.v(TAG, "Update display view");
    	if (mediaItem != null) {
    		if (mediaItem.getMediaType() == BoxeeMediaType.video) {
				LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.boxee_video_view, null);    	
				
				TextView textView;
				
				textView = (TextView)view.findViewById(R.id.boxee_video_title);
				textView.setText(mediaItem.getTitle());
				textView = (TextView)view.findViewById(R.id.boxee_video_year);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoYear.getName(), ""));
				
				textView = (TextView)view.findViewById(R.id.boxee_video_genre);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoGenre.getName(), ""));
				textView = (TextView)view.findViewById(R.id.boxee_video_duration);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoDuration.getName(), ""));
				
				textView = (TextView)view.findViewById(R.id.boxee_video_plotoutline);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoPlotoutline.getName(), ""));		
				
				textView = (TextView)view.findViewById(R.id.boxee_video_director);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoDirector.getName(), ""));		
				
				contentLayout.addView(view);
    		} else if (mediaItem.getMediaType() == BoxeeMediaType.music) {
				LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.standard_display_music_layout, contentLayout);    	
				
				TextView textView;
				
				textView = (TextView)view.findViewById(R.id.standard_display_music_title);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicTitle.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_artist);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicArtist.getName(), ""));				
				textView = (TextView)view.findViewById(R.id.standard_display_music_duration);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicDuration.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_genre);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicGenre.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_year);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicYear.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_album);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicAlbum.getName(), ""));				
    		}
    	} else {
    		
    	}
    }

    
    private void getCurrentlyPlaying() {
    	BoxeeRemoteDeviceConnection connection = boxeeRemoteDevice.getConnection();
    	
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				Log.v(TAG, response.serialize());
				mediaItem = null;
				try {
					
					if (response.getStringResult(InfoLabel.VideoTitle.getKey()) != null && response.getStringResult(InfoLabel.VideoTitle.getKey()).length() > 0) {
						mediaItem = new MediaItem();
						mediaItem.setMediaType(BoxeeMediaType.video);
						setProperties(mediaItem, "VideoPlayer", response);
					}					
					if (response.getStringResult(InfoLabel.MusicTitle.getKey()) != null && response.getStringResult(InfoLabel.MusicTitle.getKey()).length() > 0) {
						mediaItem = new MediaItem();
						mediaItem.setMediaType(BoxeeMediaType.music);
						setProperties(mediaItem, "MusicPlayer", response);
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				handler.sendEmptyMessage(0); 
			}
    		
    	};
    	
		JSONRPC2Request request = connection.createJSONRPC2Request("System.GetInfoLabels");
		if (request != null) {
			JSONArray jsonArray = new JSONArray();
			
			for (InfoLabel infoLabel : InfoLabel.values()) {
				jsonArray.put(infoLabel.getKey());
			}
			request.setParam("labels", jsonArray);
			Log.v(TAG, "SendRequest:" + request.serialize());
			connection.sendRequest(request, responseHandler);
		}    		
    }
    
    private void setProperties(MediaItem item, String namespace, JSONRPC2Response response) {
    	for (InfoLabel infoLabel : InfoLabel.values()) {
    		if (infoLabel.getNamespace().equals(namespace)) {
    			String s = response.getStringResult(infoLabel.getKey());
    			item.setProperty(infoLabel.getName(), s);
    		}
    	}
    }

	public void onNotification(JSONRPC2Notification notification) {
		if (notification.getStringParam("message").equals("PlaybackStarted") || notification.getStringParam("message").equals("PlaybackEnded") || notification.getStringParam("message").equals("PlaybackStopped")) {
			getCurrentlyPlaying();
		}
	}
	
	class MediaItem {
		private BoxeeMediaType mediaType;
		private String title;
		private Map<String, String> properties = new HashMap<String, String>();
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
		public void setProperty(String key, String value) {
			properties.put(key,  value);
		}
		
		public String getProperty(String key, String defaultValue) {
			String value = properties.get(key);
			if (properties != null && properties.size() > 0) {
				return value;				
			} else {
				return defaultValue;
			}
		}
		public BoxeeMediaType getMediaType() {
			return mediaType;
		}
		public void setMediaType(BoxeeMediaType mediaType) {
			this.mediaType = mediaType;
		}
		
	}
}



