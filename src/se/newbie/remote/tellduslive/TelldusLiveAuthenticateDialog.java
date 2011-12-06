package se.newbie.remote.tellduslive;

import org.scribe.model.Token;
import org.scribe.model.Verifier;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveTokenResponseHandler;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TelldusLiveAuthenticateDialog extends DialogFragment {
	private final static String TAG = "TelldusLiveAuthenticateDialog";

	private static boolean isInUse;
	private static Token requestToken;
	
	private boolean authInProgress = false;
	private TelldusLiveRemoteDeviceDetails details; 
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					dismissDialog();
					break;
			}
		}
	};	
	
	private TelldusLiveAuthenticateDialog() {
		super();
	}
	
	synchronized static TelldusLiveAuthenticateDialog newInstance(TelldusLiveRemoteDeviceDetails details) {
		TelldusLiveAuthenticateDialog dialog = null;		
		Activity activity = RemoteApplication.getInstance().getActivity();
		Uri uri = activity.getIntent().getData(); 
		Log.v(TAG, "Activity: " + activity.getIntent() + ";Data: " + uri);

		if (!isInUse) { 
			isInUse = true;
			dialog = new TelldusLiveAuthenticateDialog();
			dialog.authInProgress = false;
			Bundle args = new Bundle();
			args.putString("details", details.serialize());
			dialog.setArguments(args);
		}
		return dialog;
	}	
	
	public static void requestAccessToken(final TelldusLiveRemoteDevice device) {
		Activity activity = RemoteApplication.getInstance().getActivity();
		Uri uri = activity.getIntent().getData();  
		if (activity.getIntent() != null && (uri != null && uri.toString().startsWith("callback://remoteApplication"))) {  
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");
			if (oauthVerifier != null) {
				final Verifier verifier = new Verifier ( oauthVerifier );
				
				final TelldusLiveRemoteDeviceConnection connection = device.getConnection();
				
				Log.v(TAG, "Requesting access token...");
				connection.getAccessToken(requestToken, verifier, new TelldusLiveTokenResponseHandler() {
					public void onResponse(Token token) {
						Log.v(TAG, "Token received: " + token);
						if (token != null) {
							TelldusLiveRemoteDeviceDetails details = (TelldusLiveRemoteDeviceDetails)device.getRemoteDeviceDetails();
							details.setAccessToken(token);
							device.setRemoteDeviceDetails(details);					
							RemoteApplication.getInstance().getRemoteDeviceFactory().updateRemoteDeviceDetails(details);
							RemoteApplication.getInstance().showToast(RemoteApplication.getInstance().getContext().getString(R.string.telldus_live_authentication_succeeded));
						} else {
							RemoteApplication.getInstance().showToast(RemoteApplication.getInstance().getContext().getString(R.string.telldus_live_authentication_failed));
						}
				    	isInUse = false;
				    	requestToken = null;					
					}
				});
			} else {
				RemoteApplication.getInstance().showToast(RemoteApplication.getInstance().getContext().getString(R.string.telldus_live_authentication_failed));
		    	isInUse = false;
		    	requestToken = null;				
			}
		}		
	}
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		
		String s = getArguments().getString("details");
		try {
			details = new TelldusLiveRemoteDeviceDetails(s);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}		
	}
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	} 
	
    @Override
    public void onResume() { 
		super.onResume();
		Log.v(TAG, "onResume");
    } 	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.telldus_authentication_dialog, container, false);

        // Watch for button clicks.
        Button continueButton = (Button)view.findViewById(R.id.telldus_authentication_dialog_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		TelldusLiveRemoteDevice device = (TelldusLiveRemoteDevice)RemoteApplication.getInstance()
        				.getRemoteDeviceFactory().getRemoteDevice(details.getIdentifier());
        		
        		final TelldusLiveRemoteDeviceConnection connection = device.getConnection();
        		
        		if (!authInProgress) {
                    authInProgress = true;
                    
                    connection.getRequestToken(new TelldusLiveTokenResponseHandler() {
        				public void onResponse(Token token) {
        					Log.v(TAG, "Requesting token..."); 
        					requestToken = token;
        					Log.v(TAG, "Token received: " + requestToken);
        					
        					Log.v(TAG, "Starting HTTP Intent...");
        					RemoteApplication.getInstance().getActivity()
        					.startActivity (new Intent ( Intent.ACTION_VIEW, Uri.parse(connection.getAuthorizationUrl(requestToken))));					
        				}
        			});
        		}
            }
        });
        Button cancelButton = (Button)view.findViewById(R.id.telldus_authentication_dialog_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dismissDialog();
            }
        });        
    	return view;
    }    
    
    public void dismissDialog() {
    	isInUse = false;
    	authInProgress = false;
    	requestToken = null;
    	this.dismiss();
    }    
}