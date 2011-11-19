package se.newbie.remote.tellduslive;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveTokenResponseHandler;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class TelldusLiveAuthenticateDialog extends DialogFragment {
	private final static String TAG = "TelldusLiveAuthenticateDialog";

	private static OAuthService service;
	private static boolean isInUse;
	private static Token requestToken;
	
	private boolean authInProgress = false;
	private TelldusLiveRemoteDeviceDetails details; 
	//private static TelldusLiveAuthenticateDialog instance;
	
	private TelldusLiveAuthenticateDialog() {
		super();
	}
	
	synchronized static TelldusLiveAuthenticateDialog newInstance(TelldusLiveRemoteDeviceDetails details) {
		TelldusLiveAuthenticateDialog dialog = null;		
		Activity activity = RemoteApplication.getInstance().getActivity();
		Uri uri = activity.getIntent().getData(); 
		Log.v(TAG, "Activity: " + activity.getIntent() + ";Data: " + uri);

		if (activity.getIntent() != null && (uri != null && uri.toString().startsWith("callback://remoteApplication"))) { 
			dialog = new TelldusLiveAuthenticateDialog();
			Bundle args = new Bundle();
			args.putString("details", details.serialize());
			dialog.setArguments(args);
			dialog.authInProgress = true;
			
		} else if (!isInUse) { 
			isInUse = true;
			dialog = new TelldusLiveAuthenticateDialog();
			Bundle args = new Bundle();
			args.putString("details", details.serialize());
			dialog.setArguments(args);
			//instance = dialog;
		}
		return dialog;
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
		
		TelldusLiveRemoteDevice device = (TelldusLiveRemoteDevice)RemoteApplication.getInstance()
				.getRemoteDeviceFactory().getRemoteDevice(details.getIdentifier());
		
		final TelldusLiveRemoteDeviceConnection connection = device.getConnection();
		
		/*if (savedInstanceState != null) {
			 requestToken = new Token(savedInstanceState.getString("TelldusLiveAuthenticateDialog.requestTokenPublic"),
					 savedInstanceState.getString("TelldusLiveAuthenticateDialog.requestTokenPrivate"));
			 authInProgress = true;
		} 		*/
		
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
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  /*outState.putString("TelldusLiveAuthenticateDialog.requestTokenPublic", requestToken.getToken());
	  outState.putString("TelldusLiveAuthenticateDialog.requestTokenPrivate", requestToken.getSecret());*/  
	} 
	
    @Override
    public void onResume() { 
		super.onResume();
		Log.v(TAG, "onResume");
		Activity activity = RemoteApplication.getInstance().getActivity();
		Uri uri = activity.getIntent().getData();  
		if (activity.getIntent() != null && (uri != null && uri.toString().startsWith("callback://remoteApplication"))) {  
			final Verifier verifier = new Verifier ( uri.getQueryParameter("oauth_verifier") );
			
			TelldusLiveRemoteDevice device = (TelldusLiveRemoteDevice)RemoteApplication.getInstance()
					.getRemoteDeviceFactory().getRemoteDevice(details.getIdentifier());
			
			final TelldusLiveRemoteDeviceConnection connection = device.getConnection();
			
			Log.v(TAG, "Requesting access token...");
			connection.getAccessToken(requestToken, verifier, new TelldusLiveTokenResponseHandler() {
				public void onResponse(Token token) {
					Log.v(TAG, "Token received: " + token);
				}
			});
		}
    } 	
}