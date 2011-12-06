package se.newbie.remote.tellduslive;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.device.RemoteDeviceDiscoverer;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.display.RemoteDisplayFactory;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveResponseHandler;
import se.newbie.remote.tellduslive.database.TelldusLiveClientAdapter;
import se.newbie.remote.tellduslive.database.TelldusLiveDeviceAdapter;
import se.newbie.remote.tellduslive.database.TelldusLiveJobAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class TelldusLiveRemoteDeviceDiscoverer implements RemoteDeviceDiscoverer {
	public static final String TAG = "TelldusLiveRemoteDeviceDiscoverer";
	public final static String APPLICATION = "TelldusLive";
	private boolean isCreateNewInstance = true;
	
	RemoteDeviceFactory remoteDeviceFactory;
	
	public TelldusLiveRemoteDeviceDiscoverer(RemoteDeviceFactory remoteDeviceFactory) {
		this.remoteDeviceFactory = remoteDeviceFactory;
	}
	
	public void create() {
		Log.v(TAG, "Create");
	}

	public void pause() {
		Log.v(TAG, "Pause"); 
	}

	public void resume() {
		Log.v(TAG, "Resume");

		// Start up a new device if telldus is enabled and no other telldus devices is created.
		if (isEnabled() && isCreateNewInstance) {
			remoteDeviceFactory.initRemoteDevice(this, new TelldusLiveRemoteDeviceDetails());
		} 
	}

	public RemoteDeviceDetails createRemoteDeviceDetails(String details) {
		RemoteDeviceDetails remoteDeviceDetails = null;
		try {
			remoteDeviceDetails = new TelldusLiveRemoteDeviceDetails(details);
			isCreateNewInstance = false;
		} catch (Exception e) {
			Log.v(TAG, "Failed to create device details: " + details);
		}
		return remoteDeviceDetails;
	}

	public RemoteDevice createRemoteDevice(RemoteDeviceDetails details) {
		TelldusLiveRemoteDevice device = new TelldusLiveRemoteDevice(details);
		
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		RemoteDisplayFactory displayFactory = remoteApplication.getRemoteDisplayFactory();
		TelldusLiveMainRemoteDisplay remoteDisplay = new TelldusLiveMainRemoteDisplay(device);
		displayFactory.registerDisplay(device.getIdentifier(), remoteDisplay);	

		Activity activity = RemoteApplication.getInstance().getActivity();
		Uri uri = activity.getIntent().getData();  
		if (activity.getIntent() != null && (uri != null && uri.toString().startsWith("callback://remoteApplication"))) { 
			TelldusLiveAuthenticateDialog.requestAccessToken(device);
		}else if (!device.getConnection().isConnected()) {
			device.getConnection().showDialog();
		}
		return device;
	}
	
	public boolean isEnabled() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		return preferences.getBoolean("is_telldus_live_enabled", false);
	}	
	
	class TelldusLiveRemoteDeviceDiscovererThread extends Thread {
		private static final String TAG = "TelldusLiveRemoteDeviceDiscovererThread";
		
		TelldusLiveRemoteDevice device;
		
		public TelldusLiveRemoteDeviceDiscovererThread(TelldusLiveRemoteDevice device) {
			this.device = device;
		}
		
		@Override
        public void run() {
			TelldusLiveRemoteDeviceConnection connection = device.getConnection();
			
			connection.request("/clients/list", null, new TelldusLiveResponseHandler() {
				public void onResponse(JSONObject json) {
					try {
						Log.v(TAG, "Clients response: " + json.toString());
						TelldusLiveClientAdapter adapter = new TelldusLiveClientAdapter(RemoteApplication.getInstance().getContext()).open();
						try {
							
							JSONArray array = json.optJSONArray("client");
							if (array != null) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject client = array.getJSONObject(i);
									Cursor cursor = adapter.fetchTelldusLiveClientByClientId(client.getLong("id"));
									if (cursor != null) {
										adapter.updateTelldusLiveClient(cursor.getColumnIndex(TelldusLiveClientAdapter.KEY_ROWID), client.getLong("id"), client.getString("uuid"), client.getString("name"), client.getString("online"), client.getInt("editable"), client.getString("version"), client.getString("type"));
									} else {
										adapter.createTelldusLiveClient(client.getLong("id"), client.getString("uuid"), client.getString("name"), client.getString("online"), client.getInt("editable"), client.getString("version"), client.getString("type"));
									}
								}
							}
						} finally {
							adapter.close();
						}
					} catch (Exception e) {
						Log.e(TAG,  "Error on getting client data: " + e.getMessage());
					}
				}
				
			});
			
			connection.request("/devices/list", null, new TelldusLiveResponseHandler() {
				public void onResponse(JSONObject json) {
					try {
						Log.v(TAG, "Devices response: " + json.toString());
						TelldusLiveDeviceAdapter adapter = new TelldusLiveDeviceAdapter(RemoteApplication.getInstance().getContext()).open();
						try {
							
							JSONArray array = json.optJSONArray("device");
							if (array != null) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject device = array.getJSONObject(i);
									Cursor cursor = adapter.fetchTelldusLiveDeviceByDeviceId(device.getLong("id"));
									if (cursor != null) {
										adapter.updateTelldusLiveDevice(cursor.getColumnIndex(TelldusLiveDeviceAdapter.KEY_ROWID), device.getLong("id"), device.getString("name"), device.getLong("state"), device.getString("statevalue"), device.getString("methods"), device.getString("client"), device.getString("clientName"), device.getString("online"), device.getLong("editable"));
									} else {
										adapter.createTelldusLiveDevice(device.getLong("id"), device.getString("name"), device.getLong("state"), device.getString("statevalue"), device.getString("methods"), device.getString("client"), device.getString("clientName"), device.getString("online"), device.getLong("editable"));
									}
								}
							}
						} finally {
							adapter.close();
						}
					} catch (Exception e) {
						Log.e(TAG,  "Error on getting device data: " + e.getMessage());
					}
				}
				
			});		
			
			connection.request("/scheduler/jobList", null, new TelldusLiveResponseHandler() {
				public void onResponse(JSONObject json) {
					try {
						Log.v(TAG, "Jobs response: " + json.toString());
						TelldusLiveJobAdapter adapter = new TelldusLiveJobAdapter(RemoteApplication.getInstance().getContext()).open();
						try {
							
							JSONArray array = json.optJSONArray("job");
							if (array != null) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject job = array.getJSONObject(i);
									Cursor cursor = adapter.fetchTelldusLiveJobByJobId(job.getLong("id"));
									if (cursor != null) {
										adapter.updateTelldusLiveJob(cursor.getColumnIndex(TelldusLiveJobAdapter.KEY_ROWID), job.getLong("id"), job.getLong("deviceId"), job.getString("method"), job.getString("methodValue"), job.getLong("nextRunTime"), job.getString("type"), job.getLong("hour"), job.getLong("minute"), job.getLong("offset"), job.getLong("randomInterval"), job.getLong("retries"), job.getLong("retryInterval"), job.getString("weekdays"));
									} else {
										adapter.createTelldusLiveJob(job.getLong("id"), job.getLong("deviceId"), job.getString("method"), job.getString("methodValue"), job.getLong("nextRunTime"), job.getString("type"), job.getLong("hour"), job.getLong("minute"), job.getLong("offset"), job.getLong("randomInterval"), job.getLong("retries"), job.getLong("retryInterval"), job.getString("weekdays"));
									}
								}
							}
						} finally {
							adapter.close();
						}
					} catch (Exception e) {
						Log.e(TAG,  "Error on getting job data: " + e.getMessage());
					}
				}
				
			});			
		}
	}
}