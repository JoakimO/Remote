package se.newbie.remote.tellduslive;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TelldusLiveDeviceType {
	private static final String TAG = "TelldusLiveDeviceType";
	private String name;
	private String group;
	private String protocol;
	private String model;
	private int widget;
	
	public TelldusLiveDeviceType(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			name = jsonObject.optString("name");
			group = jsonObject.optString("group");
			protocol = jsonObject.optString("protocol");
			model = jsonObject.optString("model");
			widget = jsonObject.optInt("widget", -1);
		} catch (JSONException e) {
			Log.e(TAG, "Error during creating JSON: " + e.getMessage());
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getWidget() {
		return widget;
	}
	public void setWidget(int widget) {
		this.widget = widget;
	}
}
