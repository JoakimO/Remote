package se.newbie.remote.boxee;

import org.json.JSONArray;

import se.newbie.remote.R;
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
	private LinearLayout rootPanel;
	private MediaItem mediaItem;
	
	public BoxeeRemoteDisplayFragment(BoxeeRemoteDevice boxeeRemoteDevice) {
		super();
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
    	
    	handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	updateFragment();
            }
        };	    	
        rootPanel = new LinearLayout(this.getActivity().getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        rootPanel.setLayoutParams(layoutParams);
        rootPanel.setBackgroundResource(R.drawable.standard_boxee_browser);
    	return rootPanel;
    }
    
    private void updateFragment() {
    	rootPanel.removeAllViews();
    	if (mediaItem != null) {
			LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.boxee_video_view, null);    	
			
			TextView textView;
			
			textView = (TextView)view.findViewById(R.id.boxee_video_title);
			textView.setText(mediaItem.getTitle());
			textView = (TextView)view.findViewById(R.id.boxee_video_year);
			textView.setText(mediaItem.getYear());
			
			textView = (TextView)view.findViewById(R.id.boxee_video_genre);
			textView.setText(mediaItem.getGenre());
			textView = (TextView)view.findViewById(R.id.boxee_video_duration);
			textView.setText(mediaItem.getDuration());
			
			textView = (TextView)view.findViewById(R.id.boxee_video_plotoutline);
			textView.setText(mediaItem.getPlotoutline());		
			
			textView = (TextView)view.findViewById(R.id.boxee_video_director);
			textView.setText(mediaItem.getDirector());		
			
			rootPanel.addView(view);
    	}
    }

    
    private void getCurrentlyPlaying() {
    	BoxeeRemoteDeviceConnection connection = boxeeRemoteDevice.getConnection();
    	
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				Log.v(TAG, response.serialize());
				try {
					mediaItem = new MediaItem();
					mediaItem.setTitle(response.getStringResult("VideoPlayer.Title"));
					mediaItem.setGenre(response.getStringResult("VideoPlayer.Genre"));
					mediaItem.setStudio(response.getStringResult("VideoPlayer.Studio"));
					mediaItem.setDirector(response.getStringResult("VideoPlayer.Director"));
					mediaItem.setPlotoutline(response.getStringResult("VideoPlayer.Plotoutline"));
					mediaItem.setYear(response.getStringResult("VideoPlayer.Year"));
					mediaItem.setDuration(response.getStringResult("VideoPlayer.Duration"));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				handler.sendEmptyMessage(0); 
			}
    		
    	};
    	
		//{ "jsonrpc": "2.0","id": 1,"method": "System.GetInfoLabels", "params": {"labels": [ "VideoPlayer.Title","VideoPlayer.Genre","VideoPlayer.Studio","VideoPlayer.Director","VideoPlayer.Plotoutline","VideoPlayer.Year","VideoPlayer.Time" ] } }
		JSONRPC2Request request = connection.createJSONRPC2Request("System.GetInfoLabels");
		if (request != null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.put("VideoPlayer.Title");
			jsonArray.put("VideoPlayer.Genre");
			jsonArray.put("VideoPlayer.Studio");
			jsonArray.put("VideoPlayer.Director");
			jsonArray.put("VideoPlayer.Plotoutline");
			jsonArray.put("VideoPlayer.Year");
			jsonArray.put("VideoPlayer.Duration");
			request.setParam("labels", jsonArray);
			Log.v(TAG, "SendRequest:" + request.serialize());
			connection.sendRequest(request, responseHandler);
		}    		
    }

	public void onNotification(JSONRPC2Notification notification) {
		if (notification.getStringParam("message").equals("PlaybackStarted") || notification.getStringParam("message").equals("PlaybackEnded")) {
			getCurrentlyPlaying();
		}
	}
	
	class MediaItem {
		private String title;
		private String genre;
		private String studio;
		private String director;
		private String plotoutline;
		private String year;
		private String duration;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getGenre() {
			return genre;
		}
		public void setGenre(String genre) {
			this.genre = genre;
		}
		public String getStudio() {
			return studio;
		}
		public void setStudio(String studio) {
			this.studio = studio;
		}
		public String getDirector() {
			return director;
		}
		public void setDirector(String director) {
			this.director = director;
		}
		public String getPlotoutline() {
			return plotoutline;
		}
		public void setPlotoutline(String plotoutline) {
			this.plotoutline = plotoutline;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
	}
}



