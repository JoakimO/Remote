package se.newbie.remote.util.jsonrpc2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class JSONRPC2Response extends JSONRPC2Message {
	
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
	
	public Integer getIntResult() {
		try {
			return jsonObject.getInt("result");
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
	
	public JSONArray getJSONArrayResult(String key) {
		try {
			return super.getArrayData(key);
		} catch (JSONException e) {
			return null;
		}	
	}
	
	public JSONObject getJSONObject(String key) {
		try {
			return super.getJSONObjectData(key);
		} catch (JSONException e) {
			return null;
		}	
	}
}
