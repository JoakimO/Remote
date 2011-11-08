package se.newbie.remote.boxee;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.R;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BoxeeBrowserFragment extends Fragment implements RemoteDisplay {
	private final static String TAG = "BoxeeBrowserFragment";
	private BoxeeRemoteDevice device;
	private Handler handler;
	//private JSONRPC2ResponseHandler responseHandler;
	private String directory = "smb://192.168.0.1/share";
	private Map<String, Map<String,String>> files = new HashMap<String, Map<String,String>>();

	private ListView listView;
	
	
	public BoxeeBrowserFragment(BoxeeRemoteDevice device) {
		this.device = device;
	}

	
	
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	getDirectory();
    	super.onResume();
    }	
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	Log.v(TAG, "onCreate");
    	
    	handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	updateList();
            }
        };	    	
    	
    	listView = new ListView(this.getActivity().getApplicationContext());
    	listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (files != null) {
					
					int i = 0;
					for (String key : files.keySet()) {
						if (i == position) {
							Map<String, String> detail = files.get(key);
							if (detail.get("filetype").equals("directory")) {
								directory = detail.get("file");
								getDirectory();
								
							} else {
								playMedia(detail.get("file"));
							}
						} 
						i++;
					}
					handler.sendEmptyMessage(0);
				}				
			}
    	});
    	
    	
    	return listView;
    }
    
    private void playMedia(String file) {
    	BoxeeRemoteDeviceConnection connection = device.getConnection();
		//{"jsonrpc": "2.0", "method": "XBMC.Play","params":{"file": "smb://192.168.0.1/share/Movies/HD/Blade.Runner.1982.FiNAL.CUT.720p.HDDVD.x264-SiNNERS/Blade.Runner.1982.FiNAL.CUT.720p.HDDVD.x264-SiNNERS.mkv"}, "id": 1}
		JSONRPC2Request request = connection.createJSONRPC2Request("XBMC.Play");
		if (request != null) {
			request.setParam("file", file);
			Log.v(TAG, "SendRequest:" + request.serialize());
			connection.sendRequest(request, null);
		}     	
    }
    
    private void getDirectory() {
    	BoxeeRemoteDeviceConnection connection = device.getConnection();
    	
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){

			public void onResponse(JSONRPC2Response response) {
				Log.v(TAG, response.serialize());
				
				try {
					files.clear();
					JSONArray array = response.getJSONArrayResult("files");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String label = object.getString("label");
						Map<String, String> details = new HashMap<String, String>();
						files.put(label, details);
						details.put("filetype", object.getString("filetype"));
						details.put("file", object.getString("file"));
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				handler.sendEmptyMessage(0);
			}
    		
    	};
    	
    	
		//{"jsonrpc": "2.0", "method": "Files.GetDirectory","params":{"media": "video", "directory": "smb://192.168.0.1/share/Movies/HD/Blade.Runner.1982.FiNAL.CUT.720p.HDDVD.x264-SiNNERS/" }, "id": 1}
		JSONRPC2Request request = connection.createJSONRPC2Request("Files.GetDirectory");
		if (request != null) {
			request.setParam("media", "video");
			request.setParam("directory", directory);
			Log.v(TAG, "SendRequest:" + request.serialize());
			connection.sendRequest(request, responseHandler);
		}    		
		connection.sendRequest(request, responseHandler);    	
    }
    
    private void updateList() {
    	if (listView != null && files != null) {
    		Log.v(TAG, "Update List " + files.keySet().size());
    		String[] array = new String[files.keySet().size()];
    		array = files.keySet().toArray(array);
    		listView.setAdapter(new ArrayAdapter<String>(this.getActivity().getApplicationContext(), R.layout.boxee_browser_list_item, R.id.boxee_browser_text_view, array));
    	}    	
    }
    
	public String getIdentifier() {
		return "browser";
	}

	public Fragment getFragment() {
		return this;
	}    
    
}
