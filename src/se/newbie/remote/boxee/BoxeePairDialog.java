package se.newbie.remote.boxee;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Client;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class BoxeePairDialog extends DialogFragment {
	private final static String TAG = "BoxeePairDialog";
	
	private static boolean isInUse = false;
	private BoxeeRemoteDeviceDetails details;
	private View view;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.v(TAG, "Response failed");
				showError();
				break;
			case 1:
				Log.v(TAG, "Response succeeded");
				dismissDialog();
				break;			
			}
			
		}
	};
	
	private BoxeePairDialog() {
		super();
	}

	synchronized static BoxeePairDialog newInstance(final BoxeeRemoteDeviceDetails details) {
		BoxeePairDialog dialog = null;
		if (!isInUse) {
			isInUse = true;
			dialog = new BoxeePairDialog();
	        Bundle args = new Bundle();
	        args.putString("details", details.serialize());
	        dialog.setArguments(args);
		}
		return dialog;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	String s = getArguments().getString("details");
    	try {
			details = new BoxeeRemoteDeviceDetails(s);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
    }
    
    public void dismissDialog() {
    	isInUse = false;
    	BoxeeRemoteDevice device = (BoxeeRemoteDevice)RemoteApplication.getInstance().getRemoteDeviceFactory().getRemoteDevice(details.getIdentifier());
    	device.resume();
    	this.dismiss();
    }
    
    public void showError() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        view = inflater.inflate(R.layout.boxee_pair_dialog, container, false);

        // Watch for button clicks.
        Button button = (Button)view.findViewById(R.id.boxee_pair_dialog_next_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText text = (EditText)view.findViewById(R.id.boxee_pair_dialog_text);
                Thread thread = new SendPairResponseRequestThread(text.getText().toString());
                thread.start();
            }
        });
    	return view;
    }
    
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	super.onResume();

    	Thread thread = new SendPairChallengeRequestThread();
    	thread.start();
    }    
    
    private class SendPairResponseRequestThread extends Thread {
    	private String pairResponse;
    	
    	public SendPairResponseRequestThread(String pairResponse) {
    		this.pairResponse = pairResponse;
    	}
    	
		@Override
        public void run() {
			try {
				BoxeeRemoteDeviceDetails remoteDeviceDetails = details;
				JSONRPC2Client jsonRPC2Client = new JSONRPC2Client();
				jsonRPC2Client.connect(remoteDeviceDetails.getHost(), 9090);
				
				JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler() {

					public void onResponse(JSONRPC2Response response) {
						Log.v(TAG, response.serialize());
						if (response.isError() && response.getErrorMessage().equals("Failed to execute method.")) {
							handler.sendEmptyMessage(0);
						} else if (response.getBooleanResult("success")) {
							handler.sendEmptyMessage(1);
						}
					}				
				};	
				//{"jsonrpc": "2.0", "method": "Device.PairResponse", "params":{"deviceid": "android", "code": "4479"},"id": 1}
				JSONRPC2Request request = jsonRPC2Client.createJSONRPC2Request("Device.PairResponse");
				if (request != null) {
					request.setParam("deviceid", RemoteApplication.getInstance().getDeviceId());
					request.setParam("code", pairResponse);
					jsonRPC2Client.sendRequest(request, responseHandler);
				}
			} catch (Exception innerException) {
				Log.e(TAG, innerException.getMessage()); 
			}
		}	  	
    }
    
    private class SendPairChallengeRequestThread extends Thread {
		private static final String TAG = "SendPairRequestThread";
		
		@Override
        public void run() {
			try {
				BoxeeRemoteDeviceDetails remoteDeviceDetails = details;
				JSONRPC2Client jsonRPC2Client = new JSONRPC2Client();
				jsonRPC2Client.connect(remoteDeviceDetails.getHost(), 9090);    
				
				//{"jsonrpc": "2.0", "method": "Device.PairChallenge", "params":{"deviceid": "android", "applicationid": "firemote", "label": "MyRemote", "type": "tablet"},"id": 1}
				JSONRPC2Request request = jsonRPC2Client.createJSONRPC2Request("Device.PairChallenge");
				if (request != null) {
					request.setParam("deviceid", RemoteApplication.getInstance().getDeviceId());
					request.setParam("applicationid", RemoteApplication.getInstance().getApplicationId());
					request.setParam("label", "Android remote controller");
					request.setParam("type", "tablet");
					jsonRPC2Client.sendRequest(request, null);
				}
			} catch (Exception innerException) {
				Log.e(TAG, innerException.getMessage()); 
			}
		}		
	}    
    
    
    
//{"jsonrpc": "2.0", "method": "Device.PairChallenge", "params":{"deviceid": "android", "applicationid": "firemote", "label": "MyRemote", "type": "tablet"},"id": 1}

//{"jsonrpc": "2.0", "method": "Device.PairResponse", "params":{"deviceid": "android", "code": "4479"},"id": 1}

//{"jsonrpc": "2.0", "method": "Device.Connect", "params":{"deviceid": "android"},"id": 1}
	
}
