package se.newbie.remote.tellduslive;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.device.RemoteDeviceDiscoverer;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.tellduslive.TelldusLiveRemoteCommand.Command;
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
		
		List<RemoteCommand> commands = new ArrayList<RemoteCommand>();
		commands.add(new TelldusLiveRemoteCommand(device, Command.turnOn, "41321"));
		commands.add(new TelldusLiveRemoteCommand(device, Command.turnOff, "41321"));
		
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		remoteApplication.getRemoteCommandFactory().registerCommands(device.getIdentifier(), commands);		
		
		return device;
	}
}