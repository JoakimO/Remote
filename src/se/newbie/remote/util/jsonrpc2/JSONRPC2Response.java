package se.newbie.remote.util.jsonrpc2;

import org.json.JSONException;
import org.json.JSONObject;



public class JSONRPC2Response extends JSONRPC2Message {
	private final static String TAG = "JSONRPC2Response";
	
	public JSONRPC2Response(JSONObject response) throws JSONException {
		super(JSONRPC2MessageType.RESPONSE, response);
	}
	
	public Integer getId() {
		try {
			return jsonObject.getInt("id");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Integer getIntResult(String key) {
		try {
			return super.getIntegerData(key);
		} catch (JSONException e) {
			return null;
		}		
	}
	
	public String getStringResult(String key) {
		try {
			return super.getStringData(key);
		} catch (JSONException e) {
			return null;
		}		
	}
	
	public Boolean getBooleanResult(String key) {
		try {
			return super.getBooleanData(key);
		} catch (JSONException e) {
			return null;
		}		
	}	
}
