package se.newbie.remote.util.jsonrpc2;

import org.json.JSONException;

import android.util.Log;


public class JSONRPC2Request extends JSONRPC2Message{
	private static final String TAG = "JSONRPC2Request";

	public JSONRPC2Request(String method, int id) throws JSONException {
		super(JSONRPC2MessageType.REQUEST);
		jsonObject.put("method", method);
		jsonObject.put("id", id);
	}	
	
	public Integer getId() {
		try {
			return jsonObject.getInt("id");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public void setParam(String key, int value) {
		try {
			super.setIntegerData(key, value);
		} catch (JSONException e) {
			Log.v(TAG, "Not able to set " + key + ", " + value);
		}
	}	
	
	public void setParam(String key, String value) {
		try {		
			super.setStringData(key, value);
		} catch (JSONException e) {
			Log.v(TAG, "Not able to set " + key + ", " + value);
		}		
	}		
	
	public void setParam(String key, boolean value) {
		try {		
			super.setBooleanData(key, value);
		} catch (JSONException e) {
			Log.v(TAG, "Not able to set " + key + ", " + value);
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
