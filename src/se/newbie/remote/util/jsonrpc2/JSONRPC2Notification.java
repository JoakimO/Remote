package se.newbie.remote.util.jsonrpc2;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONRPC2Notification extends JSONRPC2Message {
	public JSONRPC2Notification(JSONObject json) throws JSONException {
		super(JSONRPC2MessageType.NOTIFICATION, json);
	}
	
	public String getMethod() {
		try {
			return jsonObject.getString("method");
		} catch (JSONException e) {
			return null;
		}		
	}
	
	public Integer getIntegerParam(String key) {
		try {
			return super.getIntegerData(key);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getStringParam(String key) {
		try {
			return super.getStringData(key);
		} catch (JSONException e) {
			return null;
		}		
	}
	
	public Boolean getBooleanParam(String key) {
		try {
			return super.getBooleanData(key);
		} catch (JSONException e) {
			return null;
		}		
	}	
}
