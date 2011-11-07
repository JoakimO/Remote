package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceListener;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
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
	
	private boolean isBroadcast;
	
	public RemoteModelImpl(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		isBroadcast = preferences.getBoolean("is_broadcast", true);
	}
	
	public void notifyObservers() {
		Log.v(TAG, "notifyObservers");
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
}