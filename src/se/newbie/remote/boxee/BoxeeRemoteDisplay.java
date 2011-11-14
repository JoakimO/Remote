package se.newbie.remote.boxee;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Notification;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.app.Fragment;
import android.util.Log;

public class BoxeeRemoteDisplay implements RemoteDisplay, JSONRPC2NotificationListener {
	private static final String TAG = "BoxeeRemoteDisplay";
   
	private BoxeeRemoteDevice boxeeRemoteDevice;
	private MediaItem mediaItem;
	private Fragment fragment;
	
	protected enum InfoLabel {
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
	
	public BoxeeRemoteDisplay(BoxeeRemoteDevice boxeeRemoteDevice) {
		this.boxeeRemoteDevice = boxeeRemoteDevice;
	} 
	
	public String getIdentifier() {
		return "currentlyPlaying";
	}

	public Fragment createFragment() {
		return new BoxeeRemoteDisplayFragment();
	}	
	
	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
		getCurrentlyPlaying();
		//updateFragment(mediaItem);
	}
    
    private void getCurrentlyPlaying() {
    	BoxeeRemoteDeviceConnection connection = boxeeRemoteDevice.getConnection();
    	
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				Log.v(TAG, response.serialize());
				MediaItem newMediaItem = null;
				try {
					
					if (response.getStringResult(InfoLabel.VideoTitle.getKey()) != null && response.getStringResult(InfoLabel.VideoTitle.getKey()).length() > 0) {
						newMediaItem = new MediaItem();
						newMediaItem.setMediaType(BoxeeMediaType.video);
						setProperties(newMediaItem, "VideoPlayer", response);
					}					
					if (response.getStringResult(InfoLabel.MusicTitle.getKey()) != null && response.getStringResult(InfoLabel.MusicTitle.getKey()).length() > 0) {
						newMediaItem = new MediaItem();
						newMediaItem.setMediaType(BoxeeMediaType.music);
						setProperties(newMediaItem, "MusicPlayer", response);
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				mediaItem = newMediaItem;
				updateFragment(newMediaItem);
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
	
	private void updateFragment(MediaItem mediaItem) {
		if (fragment != null) {
			((BoxeeRemoteDisplayFragment)fragment).update(mediaItem);
		}
	}
	
	public class MediaItem {
		private BoxeeMediaType mediaType;
		private String title;
		private Map<String, String> properties = new HashMap<String, String>();
		
		public MediaItem() {
		}
		
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



