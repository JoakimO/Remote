package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceListener;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;
import se.newbie.remote.main.RemoteModelParameters;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.util.Log;

public class RemoteModelImpl implements RemoteModel, RemoteDeviceListener  {
	private static final String TAG = "RemoteModelImpl";
	
	private List<RemoteModelEventListener> listeners = new ArrayList<RemoteModelEventListener>();
	private Mode mode = RemoteModel.Mode.Remote;

	private RemoteDevice selectedRemoteDevice;
	private List<RemoteDevice> remoteDevices = new ArrayList<RemoteDevice>();

	private Map<String, RemotePlayerState> remotePlayerStates = new HashMap<String, RemotePlayerState>();
	private Map<String, RemoteModelParameters> remoteModelParameters = new HashMap<String, RemoteModelParameters>();
	
	
	public RemoteModelImpl(Context context) {
	}
	
	public void notifyObservers(Object source, RemoteModelEventType eventType) {
		Log.v(TAG, "notifyObservers: " + listeners.size());
		RemoteModelEvent event = new RemoteModelEventImpl(source, eventType, this);
		
		for (RemoteModelEventListener listener : listeners) {
			Log.v(TAG, "notifyObservers: " + listener.getClass());
			listener.onRemoteModelEvent(event);
		}
	}
	
	public void addListener(RemoteModelEventListener listener) {
		this.listeners.add(listener);		
	}
	
	public void removeListener(RemoteModelEventListener listener) {
		Log.v(TAG, "Remove listener : " + listeners.contains(listener));
		this.listeners.remove(listener);
	}
	
	public Mode getMode() {
		return this.mode;
	}	

	public void setSelectedRemoteDevice(Object source, RemoteDevice selectedRemoteDevice) {
		this.selectedRemoteDevice = selectedRemoteDevice;
		notifyObservers(source, RemoteModelEventType.SelectedDeviceChanged);
	}

	public RemoteDevice getSelectedRemoteDevice() {
		return this.selectedRemoteDevice;
	}
	

	public void setRemoteDevices(Object source, List<RemoteDevice> remoteDevices) {
		this.remoteDevices = remoteDevices;
		notifyObservers(source, RemoteModelEventType.RemoteDevicesChanged);
	}
	
	public List<RemoteDevice> getRemoteDevices() {
		return this.remoteDevices;
	}
	
	public void remoteDeviceInitialized(RemoteDevice device) {
		Log.v(TAG, "Add remote device: " + device.getIdentifier());
		this.remoteDevices.add(device);
		this.notifyObservers(this, RemoteModelEventType.RemoteDevicesChanged);
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
	public void setRemoteModelParameters(Object source, String device, String key, RemoteModelParameters params) {
		String internalKey = device + "-" + key;
		
		Log.v(TAG, "Model parameters changed : " + internalKey + ", " + params.toString());
		remoteModelParameters.put(internalKey, params);
		this.notifyObservers(source, RemoteModelEventType.ParameterChanged);	
	}

	public void updateRemotePlayerState(Object source, RemotePlayerState remotePlayerState) {
		Log.v(TAG, "Update remote player state: " + remotePlayerState.getIdentification());
		
			
		if (remotePlayerState.getIdentification() != null) {
			if (!remotePlayerState.isPlaying()) {
				remotePlayerStates.remove(remotePlayerState.getIdentification());
			} else {
				remotePlayerStates.put(remotePlayerState.getIdentification(), remotePlayerState);
			}
		}
		this.notifyObservers(source, RemoteModelEventType.PlayerStateChanged);	
	}

	public RemotePlayerState getRemotePlayerState(String identification) {
		return remotePlayerStates.get(identification);
	}

	public Set<String> getRemotePlayerStates() {
		return remotePlayerStates.keySet();
	}
}