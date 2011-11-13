package se.newbie.remote.boxee;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class BoxeeBrowserFragment extends Fragment implements RemoteDisplay {
	private final static String TAG = "BoxeeBrowserFragment";
	
	private BoxeeRemoteDevice device;
	private Handler handler;
	
	
	private BoxeeMediaType mediaType;
	private String directory; // = "smb://192.168.0.1/share";
	
	//private List<String> directory = new ArrayList();
	BoxeeBrowserFileAdapter fileAdapter;
	private List<BoxeeBrowserFile> files = new ArrayList<BoxeeBrowserFile>();

	
	private ListView listView;
	private SlidingDrawer slidingDrawer;
	
	
	/**
	 * Need a empty constructor to handle recreate on orientation change
	 */
	public BoxeeBrowserFragment() {
		super();
	}	
	
	public BoxeeBrowserFragment(BoxeeRemoteDevice device) {
		super();
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
    	
        if (savedInstanceState != null) {
        	device = (BoxeeRemoteDevice)RemoteApplication.getInstance()
       			.getRemoteDeviceFactory().getRemoteDevice(savedInstanceState.getString("BoxeeBrowserFragment.remoteDevice"));
        }         	
    	
    	handler = new Handler() { 
            @Override
            public void handleMessage(Message msg) {
            	//updateBrowser();
            	Log.v(TAG, "handleMessage");

            	fileAdapter.clear();
				fileAdapter.addFiles(files);            	
				fileAdapter.notifyDataSetChanged();
            }
        };	  	
   
        
        View view = inflater.inflate(R.layout.standard_browser_layout, container, false);
        
        
        ImageButton videoButton = (ImageButton)view.findViewById(R.id.standard_browser_button_video);
        ImageButton musicButton = (ImageButton)view.findViewById(R.id.standard_browser_button_music);
        ImageButton picturesButton = (ImageButton)view.findViewById(R.id.standard_browser_button_pictures);
        ImageButton filesButton = (ImageButton)view.findViewById(R.id.standard_browser_button_files);
        
        videoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mediaType = BoxeeMediaType.video;
            	directory = null;
            	slidingDrawer.close();
            	getDirectory();
            	handler.sendEmptyMessage(0);
            }
        });
        
        musicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mediaType = BoxeeMediaType.music;
            	directory = null;
            	slidingDrawer.close();
            	getDirectory();
            	handler.sendEmptyMessage(0);
            }
        });		        
        
        picturesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mediaType = BoxeeMediaType.pictures;
            	directory = null;
            	slidingDrawer.close();
            	getDirectory();
            	handler.sendEmptyMessage(0);
            }
        });
        
        filesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mediaType = BoxeeMediaType.files;
            	directory = null;
            	slidingDrawer.close();
            	getDirectory();
            	handler.sendEmptyMessage(0);
            }
        });	        
        
        
    	listView = (ListView)view.findViewById(R.id.standard_browser_list_view);
    	listView.setBackgroundResource(R.drawable.standard_browser_list_background);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BoxeeBrowserFile file = (BoxeeBrowserFile)parent.getItemAtPosition(position);
				if (file != null) {
					if (file.getFileType() == BoxeeBrowserFile.FileType.directory) {
						directory = file.getFile();
						getDirectory();					
					} else { 
						playMedia(file.getFile());
					}
				}
				handler.sendEmptyMessage(0);
			}
    	});
   		fileAdapter = new BoxeeBrowserFileAdapter(R.layout.standard_browser_list_item);   		
   		listView.setAdapter(fileAdapter);
   		
   		slidingDrawer = (SlidingDrawer)view.findViewById(R.id.standard_browser_media_slide);
   		slidingDrawer.open();
   		
    	return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putString("BoxeeBrowserFragment.remoteDevice", device.getIdentifier());
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
    	final BoxeeRemoteDeviceConnection connection = device.getConnection();
    	
    	if (mediaType != null && directory == null) {
	    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
				public void onResponse(JSONRPC2Response response) {
					Log.v(TAG, response.serialize());  
					try {
						files.clear();
						JSONArray array = response.getJSONArrayResult("shares");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);						
								BoxeeBrowserFile file = new BoxeeBrowserFile(BoxeeBrowserFile.FileType.directory
											, object.getString("label"), object.getString("file"));
								files.add(file);
							}
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
					handler.sendEmptyMessage(0);					
		    	}
	    	};
			//{"jsonrpc": "2.0", "method": "Files.GetSources", "params":{"media": "video"}, "id": 1}
			JSONRPC2Request request = connection.createJSONRPC2Request("Files.GetSources");
			if (request != null) {
				request.setParam("media", mediaType.name());
				Log.v(TAG, "SendRequest:" + request.serialize());
				connection.sendRequest(request, responseHandler);
			}
    	} else if (directory != null) {
	    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
				public void onResponse(JSONRPC2Response response) {
					Log.v(TAG, response.serialize());
					try {
						files.clear();
						JSONArray array = response.getJSONArrayResult("files");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);						
								BoxeeBrowserFile file;
								if (object.getString("filetype").equals("directory")) {
									file = new BoxeeBrowserFile(BoxeeBrowserFile.FileType.directory
											, object.getString("label"), object.getString("file"));
								} else {
									file = new BoxeeBrowserFile(BoxeeBrowserFile.FileType.file
											, object.getString("label"), object.getString("file"));							
								}
								files.add(file);
							}			
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
				request.setParam("media", mediaType.name());
				request.setParam("directory", directory);
				Log.v(TAG, "SendRequest:" + request.serialize());
				connection.sendRequest(request, responseHandler);
			}
    	}
    }
    
	public String getIdentifier() {
		return "browser";
	}

	public Fragment getFragment() {
		return this;
	}    
    
	
	
	
	
	
	
	class BoxeeBrowserFileAdapter extends BaseAdapter {

		private List<BoxeeBrowserFile> files = new ArrayList<BoxeeBrowserFile>();
		private int resource;
		
		public BoxeeBrowserFileAdapter(int resource) {
			this.resource = resource;
		}

		public int getCount() {
			return files.size();
		}

		public Object getItem(int position) {
			return files.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public void clear() {
			files.clear();
		}
		
		public void addFile(BoxeeBrowserFile file) {
			files.add(file);
		}
		
		public void addFiles(List<BoxeeBrowserFile> files) {
			this.files.addAll(files);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View view = inflater.inflate(resource, null);
			BoxeeBrowserFile file = files.get(position);
			TextView textView = (TextView)view.findViewById(R.id.standard_browser_label);
			textView.setText(file.getLabel());
			
			return view;
		}
	}	
	
}
