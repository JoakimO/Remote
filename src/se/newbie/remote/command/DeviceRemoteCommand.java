package se.newbie.remote.command;

import java.util.Map;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;

/**
 * This command executes the command depending on selected device.
 * Used for universal commands like "Play"-button
 */
public class DeviceRemoteCommand implements RemoteCommand, RemoteModelListener {
	private Map<String, RemoteCommand> commandMap;
	String selectedDevice = null;
	private String identifier;
	
	public DeviceRemoteCommand(String identifier) {
		this.identifier = identifier;
	}
	
	public void setName(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}	
	
	public void putCommand(String device, RemoteCommand command) {
		this.commandMap.put(device, command);
	}

	public int execute(RemoteCommandArguments arguments) {
		int status = 1;
		if (commandMap.containsKey(selectedDevice)) {
			status = commandMap.get(selectedDevice).execute(arguments);
		}
		return status;
	}
	
	public void update(RemoteModel remoteModel) {
		selectedDevice = remoteModel.getSelectedRemoteDevice().getIdentifier();
	}
}