package se.newbie.remote.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class RemoteCommandFactory {
	private static final String TAG = "RemoteCommandFactory";
	private Map<String, List<RemoteCommand>> commands;

	public RemoteCommandFactory() {
		this.commands = new HashMap<String, List<RemoteCommand>>();
	}
	
	public RemoteCommand getRemoteCommand(String command, String device) {
		RemoteCommand remoteCommand = null;
		if (this.commands.containsKey(device)) {
			for (RemoteCommand internalRemoteCommand : this.commands.get(device)) {
				if (command.equals(internalRemoteCommand.getIdentifier())) {
					remoteCommand = internalRemoteCommand;
					break;
				}
			}
		}
		return remoteCommand;
	}	
	
	/**
	 * Unregister all commands for the given device
	 */
	public void unregisterCommand(String device) {
		this.commands.remove(device);
	}
	
	public void unregisterCommand(String device, RemoteCommand remoteCommand) {
		List<RemoteCommand> list = getDeviceCommands(device);
		list.remove(remoteCommand);
	}
	
	public void registerCommand(String device, RemoteCommand remoteCommand) {
		Log.v(TAG, "registerCommand: " + device + ", " + remoteCommand.getIdentifier());
		List<RemoteCommand> list = getDeviceCommands(device);
		list.add(remoteCommand);
	}
	
	public void registerCommands(String device, List<RemoteCommand> commands) {
		Log.v(TAG, "registerCommands: " + device + ", " + commands.size());
		List<RemoteCommand> list = getDeviceCommands(device);
		list.addAll(commands);
	}
	
	private List<RemoteCommand> getDeviceCommands(String device) {
		List<RemoteCommand> list;
		if (this.commands.containsKey(device)) {
			list = this.commands.get(device);
		} else {
			list = new ArrayList<RemoteCommand>();
			this.commands.put(device, list);
		}
		return list;
	}
	
	public MacroRemoteCommand createMacroRemoteCommand(String name) {
		// TODO
		return null;
	}
	
	public SwitchRemoteCommand createSwitchRemoteCommand(String name) {
		// TODO
		return null;
	}
	
	public DeviceRemoteCommand createDeviceDependentRemoteCommand(String name) {
		// TODO
		// Add to model listener.
		return null;
	}
}