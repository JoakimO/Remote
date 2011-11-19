package se.newbie.remote.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.boxee.BoxeeRemoteDeviceDiscoverer;
import se.newbie.remote.predefined.SelectRemoteDeviceRemoteCommand;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceDiscoverer;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 
 * @author joakim
 */
public class RemoteDeviceFactory {
	private static final String TAG = "RemoteDeviceFactory";
	
	private static final String REMOTE_DEVICE_IDENTITY_SET_PROPERTY = "rdf.rdis.";
	private static final String REMOTE_DEVICE_SERIALIZED_PROPERTY = "rdf.rd";
	
	private static final String PREDEFINED = "Predefined";

	private List<RemoteDeviceListener> listeners = new ArrayList<RemoteDeviceListener>();
	private List<RemoteDeviceDiscoverer> discoverers = new ArrayList<RemoteDeviceDiscoverer>();
	private Map<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
	
	public RemoteDeviceFactory() {
	}
	
	/**
	 * Do we need this?!? 
	 */
	public void addListener(RemoteDeviceListener listener) {
		this.listeners.add(listener);		
	}
	
	/**
	 * Checks if there are a remote devices registered on the
	 * given key. 
	 */
	public boolean containsRemoteDevice(String device) {
		return remoteDevices.containsKey(device);
	}
	
	/**
	 * Returns the registered remote device on the given key. 
	 * 
	 * If no device where found NULL will be returned. 
	 */
	public RemoteDevice getRemoteDevice(String device) {
		RemoteDevice remoteDevice = null;
		if (remoteDevices.containsKey(device)) {
			remoteDevice = remoteDevices.get(device);
		}
		return remoteDevice;
	}
	
	/**
	 * Starts a remote device from the given serialized remote device 
	 * details.
	 * 
	 * This method iterates all the discoverers and calls 
	 * createRemoteDeviceDetails method.
	 * 
	 * If the device can handle the serialized string a details object is
	 * returned, if it could not handle the request null is returned.
	 * 
	 * This method is only used during startup of the application to
	 * restore old known devices.
	 * 
	 * Discovered devices uses initRemoteDevice(:RemoteDeviceDetails).
	 * 
	 */
	public void initRemoteDevice(String details) {
		for (RemoteDeviceDiscoverer discoverer : discoverers) {
			RemoteDeviceDetails remoteDeviceDetails = discoverer.createRemoteDeviceDetails(details);
			if (remoteDeviceDetails != null) {
				RemoteDevice remoteDevice = discoverer.createRemoteDevice(remoteDeviceDetails);
				if (remoteDevice != null) {
					registerRemoteDevice(remoteDevice);
				}				
			}
		}
	}

	/**
	 * This method is called by the discoverers to initialize the remote device.  
	 *
	 * The discoverers don't start the devices by them self but threw the factory,
	 * this to keep track of already known devices.
	 * 
	 * If the given details already is initialized the update details methods will
	 * be called on the devices.
	 * 
	 * This could happen when a device has a new host address from the stored value
	 * in their details and needs to be change.
	 */
	public void initRemoteDevice(final RemoteDeviceDiscoverer discoverer, final RemoteDeviceDetails details) {
		if (!containsRemoteDevice(details.getIdentifier())) {
			RemoteApplication.getInstance().getActivity().runOnUiThread( new Runnable() {
				public void run() {
					Log.v(TAG, "New device found: " + details.getIdentifier());
					RemoteDevice remoteDevice = discoverer.createRemoteDevice(details);
					if (remoteDevice != null) {
						registerRemoteDevice(remoteDevice);
						storeRemoteDeviceDetails(remoteDevice.getRemoteDeviceDetails());
					}
				}
			});
		} else {
			Log.v(TAG, "Device already registered send update: " + details.getIdentifier());
			RemoteDevice remoteDevice = getRemoteDevice(details.getIdentifier());

			if (remoteDevice.update(details)) {
				Log.v(TAG, "Device details has changed: " + details.getIdentifier());
				storeRemoteDeviceDetails(details); 
			}
		}
	}
	
	/**
	 * Serialize and store the given details into the shared preferences. 
	 * 
	 * This is later used upon start up to initialize the known devices.
	 */
	private void storeRemoteDeviceDetails(RemoteDeviceDetails details) {
		Set<String> remoteDeviceSet = this.remoteDevices.keySet();
		String remoteDeviceSerialized = details.serialize();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		SharedPreferences.Editor editor = preferences.edit();

		editor.putStringSet(REMOTE_DEVICE_IDENTITY_SET_PROPERTY, remoteDeviceSet);
		editor.putString(REMOTE_DEVICE_SERIALIZED_PROPERTY + details.getIdentifier(), remoteDeviceSerialized);

		editor.commit();
		
		Log.v(TAG, "Serialized and stored remote device:" + remoteDeviceSerialized);
	}
	
	
	/**
	 * 
	 */
	public void create() {
		Log.v(TAG, "Remote Device Factory Starting...");
		createPredefined();
		
		discoverers.add(new BoxeeRemoteDeviceDiscoverer(this));
		discoverers.add(new TelldusLiveRemoteDeviceDiscoverer(this));
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		Set<String> remoteDevices = preferences.getStringSet(REMOTE_DEVICE_IDENTITY_SET_PROPERTY, null);
		if (remoteDevices != null) {
			for (String remoteDeviceIdentity : remoteDevices) {
				String remoteDeviceSerialized = preferences.getString(REMOTE_DEVICE_SERIALIZED_PROPERTY + remoteDeviceIdentity, null);
				initRemoteDevice(remoteDeviceSerialized);
			}
		}

		for (RemoteDeviceDiscoverer discoverer : discoverers) {
			discoverer.create();
		}
	}
	
	/**
	 * Create all the predefined commands and functions.
	 */
	public void createPredefined() {
		Log.v(TAG, "Create predefined objects");
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		
		SelectRemoteDeviceRemoteCommand selectRemoteDevice = new SelectRemoteDeviceRemoteCommand(PREDEFINED);
		addListener(selectRemoteDevice);
		remoteApplication.getRemoteCommandFactory().registerCommand(PREDEFINED, selectRemoteDevice);
	}
	
	public void pause() {
		for (String device : remoteDevices.keySet()) {
			RemoteDevice remoteDevice = this.getRemoteDevice(device);
			remoteDevice.pause();
		}
		for (RemoteDeviceDiscoverer discoverer : discoverers) {
			discoverer.pause();
		}		
	}
	
	public void resume() {
		for (String device : remoteDevices.keySet()) {
			RemoteDevice remoteDevice = this.getRemoteDevice(device);
			remoteDevice.resume();
		}		
		for (RemoteDeviceDiscoverer discoverer : discoverers) {
			discoverer.resume();
		}	
	}

	/**
	 * The device will be added on the main UI thread
	 */
	private void registerRemoteDevice(RemoteDevice device) {
		Log.v(TAG, "registerRemoteDevice: " + device.getIdentifier());
		if (!remoteDevices.containsKey(device.getIdentifier())) {
			remoteDevices.put(device.getIdentifier(), device);
			notifyObservers(device);
		} else {
			Log.e(TAG, "Remote device already exists: " + device.getIdentifier());
		}		
	}
	 
	private void notifyObservers(RemoteDevice device) {
		Log.v(TAG, "notifyObservers: " + device.getIdentifier());
		for (RemoteDeviceListener listener : listeners) {
			listener.remoteDeviceInitialized(device);
		}		
	}		
}
