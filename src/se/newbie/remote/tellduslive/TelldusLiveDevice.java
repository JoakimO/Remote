package se.newbie.remote.tellduslive;

import org.json.JSONObject;

public class TelldusLiveDevice {
	private int id;
	private String name;
	private int state;
	private String stateValue;
	private String methods;
	private int client;
	private String clientName;
	private boolean isOnline;
	private boolean isEditable;

	public TelldusLiveDevice() {
	}
	
	public TelldusLiveDevice(JSONObject jsonObject) {
		id = jsonObject.optInt("id", -1);
		name = jsonObject.optString("name", null);
		state = jsonObject.optInt("state", -1);
		stateValue = jsonObject.optString("statevalue", null);
		methods = jsonObject.optString("methods", null);
		client = jsonObject.optInt("client", -1);
		clientName = jsonObject.optString("clientName", null);
		isOnline = jsonObject.optBoolean("online", false);
		isEditable = jsonObject.optBoolean("editable", false);		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateValue() {
		return stateValue;
	}

	public void setStateValue(String stateValue) {
		this.stateValue = stateValue;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
