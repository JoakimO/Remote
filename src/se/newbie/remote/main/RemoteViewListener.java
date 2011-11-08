package se.newbie.remote.main;

import se.newbie.remote.command.RemoteCommandArguments;

public interface RemoteViewListener {
	public void executeCommand(String command, String device, RemoteCommandArguments arguments);
	public void setSelectedRemoteDevice(String device);
}