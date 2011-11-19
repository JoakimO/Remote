package se.newbie.remote.tellduslive;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

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
	
	public void request(String resource) {
		OAuthRequest request = new OAuthRequest(Verb.GET, RESOURCE_DOMAIN + resource);
		TelldusLiveRemoteDeviceDetails details = (TelldusLiveRemoteDeviceDetails)device.getRemoteDeviceDetails();
		service.signRequest(details.getAccessToken(), request);	
		Response response = request.send();
		Log.v(TAG, response.getBody());
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
		
	
	
	public interface TelldusLiveTokenResponseHandler {
		public void onResponse(Token token);
	}
}