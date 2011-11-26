package se.newbie.remote.command;

import java.util.Map;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;

/**
 * This command executes the command depending on selected device.
 * Used for universal commands like "Play"-button
 */
public class DeviceRemoteCommand implements RemoteCommand, RemoteModelEventListener {
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

	public int execute() {
		int status = 1;
		if (commandMap.containsKey(selectedDevice)) {
			status = commandMap.get(selectedDevice).execute();
		}
		return status;
	}
	public void onRemoteModelEvent(RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.SelectedDeviceChanged)
		{
			RemoteModel model = event.getRemoteModel();
			selectedDevice = model.getSelectedRemoteDevice().getIdentifier();
		}
		
	}
}