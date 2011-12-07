package se.newbie.remote.tellduslive;

import org.json.JSONObject;

public class TelldusLiveDevice {
	public final static int METHOD_ON = 1;
	public final static int METHOD_OFF = 2;
	public final static int METHOD_BELL = 4;
	public final static int METHOD_TOGGLE = 8;
	public final static int METHOD_DIM = 16;
	public final static int METHOD_LEARN = 32;
	public final static int METHOD_EXECUTE = 64;
	public final static int METHOD_UP = 128;
	public final static int METHOD_DOWN = 256;
	public final static int METHOD_STOP = 512;	
	
	private int id;
	private String name;
	private int state;
	private String stateValue;
	private int methods;
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
		methods = jsonObject.optInt("methods", 0);
		client = jsonObject.optInt("client", -1);
		clientName = jsonObject.optString("clientName", null);
		isOnline = jsonObject.optBoolean("online", false);
		isEditable = jsonObject.optBoolean("editable", false);		
	}
	
	public boolean compare(TelldusLiveDevice otherDevice) {
		boolean b = true;
		b &= (id == otherDevice.id); 
		b &= (name.equals(otherDevice.name));
		b &= (state == otherDevice.state);
		b &= (stateValue.equals(otherDevice.stateValue));
		b &= (methods == otherDevice.methods);
		return b;
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

	public int getMethods() {
		return methods;
	}

	public void setMethods(int methods) {
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
