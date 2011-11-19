package se.newbie.remote.tellduslive;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.device.RemoteDeviceDiscoverer;
import se.newbie.remote.device.RemoteDeviceFactory;
import android.util.Log;

public class TelldusLiveRemoteDeviceDiscoverer implements RemoteDeviceDiscoverer {
	public static final String TAG = "TelldusLiveRemoteDeviceDiscoverer";
	public final static String APPLICATION = "TelldusLive";
	
	RemoteDeviceFactory remoteDeviceFactory;
	
	public TelldusLiveRemoteDeviceDiscoverer(RemoteDeviceFactory remoteDeviceFactory) {
		this.remoteDeviceFactory = remoteDeviceFactory;
	}
	
	private void registerDevice(TelldusLiveRemoteDeviceDetails details) {
		if (details.getIdentifier() != null) {
			remoteDeviceFactory.initRemoteDevice(this, details);
		}
	}	
	

	public void create() {
		Log.v(TAG, "Create");
		registerDevice(new TelldusLiveRemoteDeviceDetails());		
	}

	public void pause() {
		Log.v(TAG, "Pause");
	}

	public void resume() {
		Log.v(TAG, "Resume");
	}

	public RemoteDeviceDetails createRemoteDeviceDetails(String details) {
		RemoteDeviceDetails remoteDeviceDetails = null;
		try {
			remoteDeviceDetails = new TelldusLiveRemoteDeviceDetails(details);
		} catch (Exception e) {
			Log.v(TAG, "Failed to create device details: " + details);
		}
		return remoteDeviceDetails;
	}

	public RemoteDevice createRemoteDevice(RemoteDeviceDetails details) {
		return new TelldusLiveRemoteDevice(details);
	}
}