package se.newbie.remote.main;


public interface RemoteViewListener {
	public void executeCommand(String command, String device);
	public void setSelectedRemoteDevice(String device);
}