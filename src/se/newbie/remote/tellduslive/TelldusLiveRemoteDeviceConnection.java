package se.newbie.remote.tellduslive;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import se.newbie.remote.application.RemoteApplication;
import android.util.Log;

public class TelldusLiveRemoteDeviceConnection {
	private final static String TAG = "TelldusLiveRemoteDeviceConnection";
	private OAuthService service;
	private TelldusLiveRemoteDevice device;

	private static final String RESOURCE_DOMAIN = "https://api.telldus.com/json";
	
	public TelldusLiveRemoteDeviceConnection(TelldusLiveRemoteDevice device) {
		this.device = device;
        service = new ServiceBuilder()
        .provider(TelldusLiveAPI.class)
        .apiKey("FEHUVEW84RAFR5SP22RABURUPHAFRUNU")
        .apiSecret("ZUXEVEGA9USTAZEWRETHAQUBUR69U6EF")
        .callback("callback://remoteApplication")
        .build();
        
	}
	
	public void request(final String resource, final Map<String, String> params, final TelldusLiveResponseHandler responseHandler) {
        new Thread() {
        	public void run() {		
				OAuthRequest request = new OAuthRequest(Verb.GET, RESOURCE_DOMAIN + resource);
				request.setConnectTimeout(9, TimeUnit.SECONDS);
				request.setReadTimeout(3, TimeUnit.SECONDS);
				
				TelldusLiveRemoteDeviceDetails details = (TelldusLiveRemoteDeviceDetails)device.getRemoteDeviceDetails();
				if (params != null) {
					for (String key : params.keySet()) {
						request.addQuerystringParameter(key, params.get(key));
					}					
				}				
				try {
					Token accessToken = details.getAccessToken();
					Log.v(TAG, "Sending request: " + request.getUrl() + "; Token: " + accessToken);
					if (accessToken != null) {
						service.signRequest(accessToken, request);
						Response response = request.send();
						Log.v(TAG, response.getBody());
						if (responseHandler != null) {
							responseHandler.onResponse(new JSONObject(response.getBody()));
						}
					} else {
						Log.w(TAG, "Trying to send request without access token");
						showDialog();	
					}
				} catch (Exception e) { 
					Log.v(TAG, "Error during request: " + e.getMessage());
					showDialog();
				}
        	}
        }.start();
	}
	
	public void getRequestToken(final TelldusLiveTokenResponseHandler handler) {
        new Thread() {
        	public void run() {
        		Token requestToken =  service.getRequestToken();
        		handler.onResponse(requestToken);
        	}
        }.start();
		
	}
	
	public void getAccessToken(final Token requestToken, final Verifier verifier, final TelldusLiveTokenResponseHandler handler) {
        new Thread() {
        	public void run() {
        		Token accessToken = service.getAccessToken(requestToken, verifier); 
				handler.onResponse(accessToken);
        	}
        }.start();		
	}	
	
	public String getAuthorizationUrl(Token requestToken) {
		return service.getAuthorizationUrl(requestToken);
	}
	
	public void showDialog() {
		RemoteApplication.getInstance().getActivity().runOnUiThread(new Runnable(){
			public void run() {
				Log.v(TAG, "Opening login dialog...");
				TelldusLiveAuthenticateDialog dialog = TelldusLiveAuthenticateDialog.newInstance((TelldusLiveRemoteDeviceDetails)device.getRemoteDeviceDetails());
				if (dialog != null) {
					RemoteApplication.getInstance().showDialog(dialog);
				}					
			}
		});
	}		

	
	public interface TelldusLiveResponseHandler {
		public void onResponse(JSONObject json);
	}
	
	
	public interface TelldusLiveTokenResponseHandler {
		public void onResponse(Token token);
	}	
	
}