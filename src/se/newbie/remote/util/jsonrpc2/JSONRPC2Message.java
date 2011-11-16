package se.newbie.remote.util.jsonrpc2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class JSONRPC2Message {
	private static final String TAG = "JSONRPC2Message";
	private static final String version = "2.0";
	
	protected enum JSONRPC2MessageType {
		REQUEST, RESPONSE, NOTIFICATION
	}	
	JSONRPC2MessageType type;
	protected JSONObject jsonObject;
	private JSONObject jsonData;
	
	private boolean isError;
	private int errorCode;
	private String errorMessage;

	
	
	protected JSONRPC2Message(JSONRPC2MessageType type) throws JSONException {
		this.type = type;
		jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", version);
		if (type == JSONRPC2MessageType.REQUEST) {
			jsonData = new JSONObject();
			jsonObject.put("params", jsonData);
		}
	}
	
	protected JSONRPC2Message(JSONRPC2MessageType type, String json) throws JSONException {
		this.type = type;
		jsonObject = new JSONObject(json);
		if (type == JSONRPC2MessageType.RESPONSE) {
			if (jsonObject.has("result")) {
				jsonData = jsonObject.optJSONObject("result");
				//jsonObject.remove("result");
			}
		}
	}
	
	protected JSONRPC2Message(JSONRPC2MessageType type, JSONObject json) throws JSONException {
		jsonObject = json;
		if (type == JSONRPC2MessageType.NOTIFICATION) {
			if (jsonObject.has("params")) {
				jsonData = jsonObject.optJSONObject("params");
				//jsonObject.remove("params");
			}			
		} else if (type == JSONRPC2MessageType.RESPONSE) {
			if (jsonObject.has("result")) {
				jsonData = jsonObject.optJSONObject("result");
				//jsonObject.remove("result");
			} else if (jsonObject.has("error")) {
				createErrorResponse(jsonObject);
			}
		}
	}
	
	private void createErrorResponse(JSONObject jsonObject) {
		try {
			isError = true;
			JSONObject error = jsonObject.getJSONObject("error");
			errorCode = error.getInt("code");
			errorMessage = error.getString("message");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	
	protected boolean containsData(String key) {
		if (jsonData != null) {
			return jsonData.has(key);
		}
		return false;
	}
	
	protected String getStringData(String key) throws JSONException {
		if (jsonData != null) {
			return jsonData.getString(key);
		}
		return null;
	}
	
	protected Integer getIntegerData(String key) throws JSONException {
		if (jsonData != null) {
			return jsonData.getInt(key);
		}
		return null;
	}
	
	protected Boolean getBooleanData(String key) throws JSONException {
		if (jsonData != null) {
			return jsonData.getBoolean(key);
		}
		return null;
	}

	protected JSONArray getArrayData(String key) throws JSONException {
		if (jsonData != null) {
			return jsonData.getJSONArray(key);
		}
		return null;
	}	
	
	protected JSONObject getJSONObjectData(String key) throws JSONException {
		if (jsonData != null) {
			return jsonData.getJSONObject(key);
		}
		return null;
	}
	
	protected void setStringData(String key, String value) throws JSONException {
		if (jsonData != null) {
			jsonData.put(key, value);
		}
	}

	protected void setIntegerData(String key, Integer value) throws JSONException {
		if (jsonData != null) {
			jsonData.put(key, value);
		}
	}
	
	protected void setBooleanData(String key, Boolean value) throws JSONException {
		if (jsonData != null) {
			jsonData.put(key, value);
		}
	}		
	
	protected void setJSONArrayData(String key, JSONArray array) throws JSONException {
		if (jsonData != null) {
			jsonData.put(key, array);
		}
	}
	
	public String serialize() {
		/*try {
			if (jsonData.length() > -1) {
				if (this.type == JSONRPC2MessageType.REQUEST) {
					jsonObject.put("params", jsonData);
				} else if (this.type == JSONRPC2MessageType.RESPONSE) {
					jsonObject.put("result", jsonData);
				}			
			}
		} catch (JSONException e) {
			Log.v(TAG, e.getMessage());
		}*/
		return jsonObject.toString();		
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isError() {
		return isError;
	}
}
