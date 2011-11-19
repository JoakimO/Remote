package se.newbie.remote.tellduslive;

import java.util.HashMap;

import se.newbie.remote.command.RemoteCommand;

public class TelldusLiveRemoteCommand implements RemoteCommand {
	private final static String TAG = "TelldusLiveRemoteCommand";
	
	public enum Command {
		bell
		, dim
		, down
		, up
		, turnOff
		, turnOn
	}
	
	private TelldusLiveRemoteDevice device;
	private Command command;
	private String telldusLiveDeviceId;
	
	public TelldusLiveRemoteCommand(TelldusLiveRemoteDevice device, Command command, String telldusLiveDeviceId) {
		this.device = device;
		this.command = command;
		this.telldusLiveDeviceId = telldusLiveDeviceId;
	}	
	
	public String getIdentifier() {
		return command.name() + "-" + telldusLiveDeviceId;
	}

	public int execute() {
		switch (command) {
		case turnOn :
			sendCommand("/device/turnOn", telldusLiveDeviceId);
			break;
		case turnOff :
			sendCommand("/device/turnOff", telldusLiveDeviceId);
			break;
		case up :
			sendCommand("/device/up", telldusLiveDeviceId);
			break;
		case down :
			sendCommand("/device/down", telldusLiveDeviceId);
			break;
		case bell :
			sendCommand("/device/bell", telldusLiveDeviceId);
			break;			
		}
		return 1;
	}
	
	private void sendCommand(String command, String deviceId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", deviceId);
		
		device.getConnection().request(command, params);
	}

}
