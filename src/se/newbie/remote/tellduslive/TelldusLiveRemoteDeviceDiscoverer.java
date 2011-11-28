package se.newbie.remote.tellduslive;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.device.RemoteDeviceDiscoverer;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.tellduslive.TelldusLiveRemoteCommand.Command;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveResponseHandler;
import se.newbie.remote.tellduslive.database.TelldusLiveClientAdapter;
import android.database.Cursor;
import android.util.Log;

public class TelldusLiveRemoteDeviceDiscoverer implements RemoteDeviceDiscoverer {
	public static final String TAG = "TelldusLiveRemoteDeviceDiscoverer";
	public final static String APPLICATION = "TelldusLive";
	
	private TelldusLiveRemoteDevice device;
	
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
		if (device == null) {
			remoteDeviceFactory.initRemoteDevice(this, new TelldusLiveRemoteDeviceDetails());
		}
		
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
		
		device = new TelldusLiveRemoteDevice(details);
		
		new TelldusLiveRemoteDeviceDiscovererThread(device).start();
		
		List<RemoteCommand> commands = new ArrayList<RemoteCommand>();
		commands.add(new TelldusLiveRemoteCommand(device, Command.turnOn, "41321"));
		commands.add(new TelldusLiveRemoteCommand(device, Command.turnOff, "41321"));
		
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		remoteApplication.getRemoteCommandFactory().registerCommands(device.getIdentifier(), commands);		
		
		return device;
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
		}
	}
}