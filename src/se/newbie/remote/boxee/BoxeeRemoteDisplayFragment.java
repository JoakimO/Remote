package se.newbie.remote.boxee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.util.HttpRequestTask;
import se.newbie.remote.util.HttpRequestTaskHandler;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Notification;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BoxeeRemoteDisplayFragment extends Fragment implements RemoteDisplay, JSONRPC2NotificationListener {
	private static final String TAG = "BoxeeDisplayFragment";
	
    private Handler handler;
    
	private BoxeeRemoteDevice boxeeRemoteDevice;
   // private WebView webView;  
	private ImageView imageView;

	private LinearLayout rootPanel;
	private LinearLayout pendingLayout;
	private LinearLayout displayPanel;
	
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
    	startUpdateView();
    	super.onResume();
    }	
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	Log.v(TAG, "onCreate");
    	
    	handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	startUpdateView();
            }
        };	    	
    	
    	
    	rootPanel = new LinearLayout(this.getActivity().getApplicationContext());
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    	rootPanel.setLayoutParams(params);
    	rootPanel.setOrientation(LinearLayout.VERTICAL);
    	
    	//View v = inflater.inflate(R.layout.boxee_currently_playing, container, false);    	
    	//	webView = new WebView(this.getActivity().getApplicationContext());
    //	webView.getSettings().setJavaScriptEnabled(true);
    //	webView.loadUrl("file:///android_asset/boxee/index.html");
    //	return webView;
    //	return v; //imageView;
    	return rootPanel;
    }
    
    private String getContent(InputStream inputStream) {
    	Writer writer = new StringWriter();
    	if (inputStream != null) {
	    	char[] buf = new char[1024];
	    	try	{
	    		Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    		int i;
	    		while ((i = reader.read(buf)) != -1) {
	    			writer.write(buf, 0, i);
	    		}
	    	}
	    	catch (IOException e) {
	    		Log.v(TAG, e.getMessage());
	    	}
    	}
    	return writer.toString();
    }

    private void sendHttpRequestTask(String url, HttpRequestTaskHandler httpRequestTaskHandler) {
    	HttpRequestTask request = new HttpRequestTask(); 

    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		if (preferences.contains(boxeeRemoteDevice.getIdentifier() + ".user")) {
			request.setCredentials(preferences.getString(boxeeRemoteDevice.getIdentifier() + ".user", "")
					, preferences.getString(boxeeRemoteDevice.getIdentifier() + ".password", ""));
		}    	
    	
    	request.setHttpRequestTaskHandler(httpRequestTaskHandler);
    	request.execute(url);    	
    }
    
    private Map<String, String> parseResponse(String response) {
    	Map<String, String> result = new HashMap<String, String>();
    	
    	String[] strings = response.split("\\r?\\n");
    	if (strings != null) { 
    		for (String s : strings) {
    			if (s.startsWith("<li>")) {
    				s = s.substring(4);
    				int index = s.indexOf(":");
    				String key = s.substring(0, index);
    				String value = s.substring(index + 1, s.length());
    				result.put(key, value);
    			}
    		}
    	}
    	return result;
    }
    
    
    
    
	public void onNotification(JSONRPC2Notification notification) {
		Log.v(TAG, "Got notification of change");
		
		if (notification.getMethod().equals("Announcement")) {
			if (notification.getStringParam("message").equals("PlaybackStarted")) {
				handler.sendEmptyMessage(0);
				//startUpdateView();
			} else if (notification.getStringParam("message").equals("PlaybackStopped")) {
				handler.sendEmptyMessage(0);
				//startUpdateView();
			}
					
		}
	}
	
	public void startUpdateView() {
		showProgressPanel();
		showView();		
	}
	
	public void showProgressPanel() {
		rootPanel.removeAllViews();
		LinearLayout progressPanel = new LinearLayout(this.getActivity().getApplicationContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    	progressPanel.setLayoutParams(params);
    	progressPanel.setOrientation(LinearLayout.HORIZONTAL);
    	
    	ProgressBar progressBar = new ProgressBar(getActivity().getApplicationContext());
    	ViewGroup.LayoutParams progressLayout = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

    	progressBar.setLayoutParams(progressLayout);
    	progressBar.setMax(100);
    	progressBar.setProgress(50);
    	progressPanel.addView(progressBar);    	
    	
    	rootPanel.addView(progressPanel);	
	}
	
	
	public void showView() {
    	BoxeeRemoteDeviceDetails details = (BoxeeRemoteDeviceDetails)boxeeRemoteDevice.getRemoteDeviceDetails();
		LinearLayout.LayoutParams params;
		
    	pendingLayout = new LinearLayout(this.getActivity().getApplicationContext());
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    	pendingLayout.setLayoutParams(params);
    	pendingLayout.setOrientation(LinearLayout.VERTICAL);		
		
		displayPanel = new LinearLayout(this.getActivity().getApplicationContext());
    	params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    	displayPanel.setLayoutParams(params);
    	displayPanel.setOrientation(LinearLayout.HORIZONTAL);
    	pendingLayout.addView(displayPanel);
    	
    	HttpRequestTaskHandler httpRequestTaskHandler = new HttpRequestTaskHandler(){
    		public void onSuccess(InputStream in) {
    			String content = getContent(in);
    			//Log.v(TAG, content);    			
    			if (content.contains("Nothing Playing")) {
    				rootPanel.removeAllViews();
    			} else if (content.contains("Changed")) { 				
    				Map<String, String> response = parseResponse(content);
   					updateInfo(response);
   					updateCurrentPlayingImage();
    			}
    		} 
    	};       
    	sendHttpRequestTask(String.format("http://%s:%s/xbmcCmds/xbmcHttp?command=GetCurrentlyPlaying(special:%%5C%%5Cxbmc%%5Cweb%%5Cthumb.jpg)", details.getHost(), details.getPort()), httpRequestTaskHandler);
	}
   
	private void updateInfo(Map<String, String> response) {
		LinearLayout panel = new LinearLayout(this.getActivity().getApplicationContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	panel.setLayoutParams(params);
    	panel.setOrientation(LinearLayout.VERTICAL);		
		
		TextView text = new TextView(getActivity().getApplicationContext());
		text.setText(response.get("Show Title"), TextView.BufferType.NORMAL);
		panel.addView(text);
		text = new TextView(getActivity().getApplicationContext());
		text.setText(response.get("Title"), TextView.BufferType.NORMAL);
		panel.addView(text);
		text = new TextView(getActivity().getApplicationContext());
		text.setText(response.get("Plot"), TextView.BufferType.NORMAL);
		panel.addView(text);    			
		
		displayPanel.addView(panel);
		rootPanel.removeAllViews();
		rootPanel.addView(pendingLayout);
		
	}
	
    private void updateCurrentPlayingImage() {
    	HttpRequestTaskHandler httpRequestTaskHandler = new HttpRequestTaskHandler(){
    		public void onSuccess(InputStream in) {
    			
    	    	imageView = new ImageView(getActivity().getApplicationContext());
    			Drawable d = Drawable.createFromStream(in, "src");
    			imageView.setImageDrawable(d);
    	    	displayPanel.addView(imageView);
    	    	
    		}
    	};    	
    	BoxeeRemoteDeviceDetails details = (BoxeeRemoteDeviceDetails)boxeeRemoteDevice.getRemoteDeviceDetails();
    	sendHttpRequestTask(String.format("http://%s:%s/thumb.jpg", details.getHost(), details.getPort()), httpRequestTaskHandler);    	
    } 
}



