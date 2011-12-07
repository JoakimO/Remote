package se.newbie.remote.tellduslive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.tellduslive.TelldusLiveRemoteCommand.Command;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveResponseHandler;
import android.util.Log;

public class TelldusLiveRemoteDevice implements RemoteDevice {
	private static final String TAG = "TelldusLiveRemoteDevice";
	
	TelldusLiveRemoteDeviceDetails details;
	TelldusLiveRemoteDeviceConnection connection;
	private Map<Long, TelldusLiveDevice> telldusLiveDeviceMap;
	
	public TelldusLiveRemoteDevice(RemoteDeviceDetails details) {
		this.details = (TelldusLiveRemoteDeviceDetails)details;
		connection = new TelldusLiveRemoteDeviceConnection(this);
	}
	
	public String getIdentifier() {
		return details.getIdentifier();
	}

	public RemoteDeviceType getRemoteDeviceType() {
		return RemoteDeviceType.TelldusLive;
	}

	public String getRemoteDeviceName() {
		return TelldusLiveRemoteDeviceDiscoverer.APPLICATION;
	}

	public void setRemoteDeviceDetails(TelldusLiveRemoteDeviceDetails details) {
		this.details = details;
	}
	
	public RemoteDeviceDetails getRemoteDeviceDetails() {
		return details;
	}

	public void pause() {
		Log.v(TAG, "Pause");
	}

	public void resume() {
		Log.v(TAG, "Resume");	
		if (connection.isConnected()) {	
			synchronizeDevices(this);
		}
	}


	public boolean update(RemoteDeviceDetails details) {
		boolean b = false;
		if (details.getIdentifier().startsWith(TelldusLiveRemoteDeviceDiscoverer.APPLICATION)) {
			if (((TelldusLiveRemoteDeviceDetails)details).getAccessToken() != null &&
					!((TelldusLiveRemoteDeviceDetails)details).getAccessToken().equals(this.details.getAccessToken())) {
				this.details.setAccessToken(((TelldusLiveRemoteDeviceDetails)details).getAccessToken());
				b = true;
			}		
		}
		return b;
	}	
	
	public TelldusLiveRemoteDeviceConnection getConnection() {
		return connection;
	}
	
	private void synchronizeDevice(final TelldusLiveRemoteDevice device, final Long telldusLiveDeviceId) {
		device.getConnection();
		Map<String, String> params = new HashMap<String, String>();	

		params.put(
				"supportedMethods",
				Integer.toString(TelldusLiveDevice.METHOD_ON
						| TelldusLiveDevice.METHOD_OFF
						| TelldusLiveDevice.METHOD_BELL
						| TelldusLiveDevice.METHOD_DIM
						| TelldusLiveDevice.METHOD_LEARN
						| TelldusLiveDevice.METHOD_EXECUTE
						| TelldusLiveDevice.METHOD_UP
						| TelldusLiveDevice.METHOD_DOWN
						| TelldusLiveDevice.METHOD_STOP));	
		params.put("id", Long.toString(telldusLiveDeviceId));
		
		connection.request("/device/info", params, new TelldusLiveResponseHandler() {
			public void onResponse(JSONObject json) {
				try {
					Log.v(TAG, "Device response: " + json.toString());
					RemoteApplication remoteApplication = RemoteApplication.getInstance();

					Long deviceId = json.optLong("id", -1);
					if (deviceId > 0) {
						if (device.setTelldusLiveDevice(new TelldusLiveDevice(json))) {
							RemoteDisplay display = remoteApplication.getRemoteDisplayFactory().getRemoteDisplay(TelldusLiveMainRemoteDisplay.IDENTIFIER, device.getIdentifier());
							if (display != null) {
								display.invalidate();
							}
						}
					}
				} catch (Exception e) {
					Log.e(TAG,  "Error on getting device data: " + e.getMessage());
				}
			}
		});				
	}
	
	private void synchronizeDevices(final TelldusLiveRemoteDevice device) {
		device.getConnection();
		Map<String, String> params = new HashMap<String, String>();

		params.put(
				"supportedMethods",
				Integer.toString(TelldusLiveDevice.METHOD_ON
						| TelldusLiveDevice.METHOD_OFF
						| TelldusLiveDevice.METHOD_BELL
						| TelldusLiveDevice.METHOD_DIM
						| TelldusLiveDevice.METHOD_LEARN
						| TelldusLiveDevice.METHOD_EXECUTE
						| TelldusLiveDevice.METHOD_UP
						| TelldusLiveDevice.METHOD_DOWN
						| TelldusLiveDevice.METHOD_STOP));		
		
		connection.request("/devices/list", params, new TelldusLiveResponseHandler() {
			public void onResponse(JSONObject json) {
				try {
					Log.v(TAG, "Devices response: " + json.toString());
					RemoteApplication remoteApplication = RemoteApplication.getInstance();
					List<RemoteCommand> commands = new ArrayList<RemoteCommand>();		
					Map<Long, TelldusLiveDevice> telldusLiveDevices = new HashMap<Long, TelldusLiveDevice>();
					JSONArray array = json.optJSONArray("device");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject deviceObject = array.getJSONObject(i);
							Long deviceId = deviceObject.optLong("id", -1);
							if (deviceId > 0) {
								commands.addAll(createCommands(device, deviceObject));
								telldusLiveDevices.put(deviceId, new TelldusLiveDevice(deviceObject));
							}
						}
					}
					device.setTelldusLiveDeviceMap(telldusLiveDevices);
					remoteApplication.getRemoteCommandFactory().unregisterCommand(device.getIdentifier());
					remoteApplication.getRemoteCommandFactory().registerCommands(device.getIdentifier(), commands);	
					RemoteDisplay display = remoteApplication.getRemoteDisplayFactory().getRemoteDisplay(TelldusLiveMainRemoteDisplay.IDENTIFIER, device.getIdentifier());
					if (display != null) {
						display.invalidate();
					}
				} catch (Exception e) {
					Log.e(TAG,  "Error on getting devices data: " + e.getMessage());
				}
			}
		});		
	}

	protected List<RemoteCommand> createCommands(TelldusLiveRemoteDevice device, JSONObject object) {
		List<RemoteCommand> commands = new ArrayList<RemoteCommand>();
		
		Long deviceId = object.optLong("id");
		int methods = object.optInt("methods", 0);
		
		if ((methods & TelldusLiveDevice.METHOD_ON) == TelldusLiveDevice.METHOD_ON) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.turnOn, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_OFF) == TelldusLiveDevice.METHOD_OFF) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.turnOff, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_DIM) == TelldusLiveDevice.METHOD_DIM) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.dim, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_LEARN) == TelldusLiveDevice.METHOD_LEARN) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.learn, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_BELL) == TelldusLiveDevice.METHOD_BELL) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.bell, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_UP) == TelldusLiveDevice.METHOD_UP) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.up, deviceId.toString()));
		}
		if ((methods & TelldusLiveDevice.METHOD_DOWN) == TelldusLiveDevice.METHOD_DOWN) {
			commands.add(new TelldusLiveRemoteCommand(device, Command.down, deviceId.toString()));
		}		
		return commands;
	}
	

	
	protected void setTelldusLiveDeviceMap(
			Map<Long, TelldusLiveDevice> telldusLiveDevices) {
		this.telldusLiveDeviceMap = telldusLiveDevices;
	}
	
	protected boolean setTelldusLiveDevice(TelldusLiveDevice device) {
		boolean b = true;
		if (telldusLiveDeviceMap.containsKey((long)device.getId()) &&
				device.compare(telldusLiveDeviceMap.get((long)device.getId()))) {
			b = false;
		}
		
		Log.v(TAG, "Set device: " + device.toString());
		this.telldusLiveDeviceMap.put((long)device.getId(), device);
		return b;
	}

	public List<TelldusLiveDevice> getTelldusLiveDevices() {
		List<TelldusLiveDevice> telldusLiveDevices = new ArrayList<TelldusLiveDevice>();
		if (telldusLiveDeviceMap != null) {
			for (Long id : telldusLiveDeviceMap.keySet()) {
				telldusLiveDevices.add(telldusLiveDeviceMap.get(id));
			}
		}
		return telldusLiveDevices;
	}
	
	public void invalidateTelldusLiveDevice(Long id) {
		synchronizeDevice(this, id);
	}
}
