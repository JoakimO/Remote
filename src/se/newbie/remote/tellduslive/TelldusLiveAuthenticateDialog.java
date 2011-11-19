package se.newbie.remote.tellduslive;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import se.newbie.remote.application.RemoteApplication;
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
		/*if (instance != null) {
			Log.v(TAG, "Token:" + instance.requestToken + ";InUse: " + instance.isInUse + ";authInProgress: " + instance.authInProgress);
		}*/
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
		
		
		/*if (savedInstanceState != null) {
			 requestToken = new Token(savedInstanceState.getString("TelldusLiveAuthenticateDialog.requestTokenPublic"),
					 savedInstanceState.getString("TelldusLiveAuthenticateDialog.requestTokenPrivate"));
			 authInProgress = true;
		} 		*/
		
		if (!authInProgress) {
            authInProgress = true;

            new Thread() {
            	public void run() {
				//This might have to be done in a thread...
	            service = new ServiceBuilder()
	                .provider(TelldusLiveAPI.class)
	                .apiKey("FEHUVEW84RAFR5SP22RABURUPHAFRUNU")
	                .apiSecret("ZUXEVEGA9USTAZEWRETHAQUBUR69U6EF")
	                .callback("callback://remoteApplication")
	                .build();
	
				Log.v(TAG, "Requesting token..."); 
				requestToken = service.getRequestToken();
				Log.v(TAG, "Token received: " + requestToken);
				
				//Maybe there should be some sort of information and trigger this with a button.
				Log.v(TAG, "Authorization URL: " + service.getAuthorizationUrl(requestToken));
				Log.v(TAG, "Starting HTTP Intent...");
				RemoteApplication.getInstance().getActivity()
					.startActivity (new Intent ( Intent.ACTION_VIEW, Uri.parse(service.getAuthorizationUrl(requestToken))));
            	}
            }.start();
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
            new Thread() {
            	public void run() {
            		Log.v(TAG, "Requesting access token...");
            		Log.v(TAG, "Request token: " + requestToken);
            		Log.v(TAG, "Request verifier: " + verifier);
            		Token accessToken = service.getAccessToken(requestToken, verifier);
            		Log.v(TAG, "Token received: " + accessToken);
            	}
            }.start();
		}
    } 	
}