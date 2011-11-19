package se.newbie.remote.tellduslive;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class TelldusLiveAPI extends DefaultApi10a {
	private static final String AUTHORIZATION_URL = "https://api.telldus.com/oauth/authorize?oauth_token=%s";
	private static final String REQUEST_TOKET_URL = "https://api.telldus.com/oauth/requestToken";
	private static final String ACCESS_TOKEN_URL = "https://api.telldus.com/oauth/accessToken";
	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}
	@Override
	public String getAuthorizationUrl(Token token) {
		return String.format(AUTHORIZATION_URL, token.getToken());
	}
	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKET_URL; 
	}
	
	/*
	@Override
	public String getAccessTokenEndpoint()
	{
		return ACCESS_TOKEN_URL;
	}	
	
	@Override
	public Verb getAccessTokenVerb()
	{
		return Verb.POST;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(AUTHORIZATION_URL, config.getApiKey(), formURLEncode(config.getCallback()));
	}*/	
}