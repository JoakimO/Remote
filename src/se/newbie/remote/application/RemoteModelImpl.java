package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceListener;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import se.newbie.remote.main.RemoteModelParameters;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class RemoteModelImpl implements RemoteModel, RemoteDeviceListener  {
	private static final String TAG = "RemoteModelImpl";
	
	private List<RemoteModelListener> listeners = new ArrayList<RemoteModelListener>();
	private Mode mode = RemoteModel.Mode.Remote;

	private RemoteDevice selectedRemoteDevice;
	private List<RemoteDevice> remoteDevices = new ArrayList<RemoteDevice>();
	private RemoteGUIFactory remoteGUIFactory;

	private Map<String, RemotePlayerState> remotePlayerStates = new HashMap<String, RemotePlayerState>();
	private Map<String, RemoteModelParameters> remoteModelParameters = new HashMap<String, RemoteModelParameters>();
	
	private boolean isBroadcast;
	
	public RemoteModelImpl(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		isBroadcast = preferences.getBoolean("is_broadcast", true);
	}
	
	public void notifyObservers() {
		Log.v(TAG, "notifyObservers: " + listeners.size());
		for (RemoteModelListener listener : listeners) {
			listener.update(this);
		}
	}
	
	public void addListener(RemoteModelListener listener) {
		this.listeners.add(listener);		
	}
	
	public Mode getMode() {
		return this.mode;
	}	

	public void setSelectedRemoteDevice(RemoteDevice selectedRemoteDevice) {
		this.selectedRemoteDevice = selectedRemoteDevice;
		notifyObservers();
	}

	public RemoteDevice getSelectedRemoteDevice() {
		return this.selectedRemoteDevice;
	}
	
	public void setRemoteGUIFactory(RemoteGUIFactory remoteGUIFactory) {
		this.remoteGUIFactory = remoteGUIFactory;
		notifyObservers();
	}

	public RemoteGUIFactory getRemoteGUIFactory() {
		return this.remoteGUIFactory;
	}	
	
	public void setRemoteDevices(List<RemoteDevice> remoteDevices) {
		this.remoteDevices = remoteDevices;
		notifyObservers();
	}
	
	public List<RemoteDevice> getRemoteDevices() {
		return this.remoteDevices;
	}
	
	public void remoteDeviceInitialized(RemoteDevice device) {
		Log.v(TAG, "Add remote device: " + device.getIdentifier());
		this.remoteDevices.add(device);
		this.notifyObservers();
	}

	public RemoteModelParameters getRemoteModelParameters(String device, String key) {
		String internalKey = device + "-" + key;
		
		RemoteModelParameters params;
		if (remoteModelParameters.containsKey(internalKey)) {
			params = remoteModelParameters.get(internalKey);
		} else {
			params = new RemoteModelParametersImpl();
			remoteModelParameters.put(internalKey, params);
		}
		return params;
	}

	/**
	 * Sets the parameters and notifies the observers only if the parameters have changed.
	 */
	public void setRemoteModelParameters(String device, String key, RemoteModelParameters params) {
		String internalKey = device + "-" + key;
		
		Log.v(TAG, "Model parameters changed : " + internalKey + ", " + params.toString());
		remoteModelParameters.put(internalKey, params);
		this.notifyObservers();	
	}

	public RemotePlayerState getRemotePlayerState(String device) {
		return remotePlayerStates.get(device);
	}

	public void setRemotePlayerState(String device, RemotePlayerState remotePlayerState) {
		this.remotePlayerStates.put(device, remotePlayerState);
		this.notifyObservers();	
	}
}