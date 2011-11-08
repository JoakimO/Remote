package se.newbie.remote.action;

public class ClickRemoteAction implements RemoteAction {

	String command;
	String device;
	
	public ClickRemoteAction(String command, String device) {
		this.command = command;
		this.device = device;
	}

	public String getCommand() {
		return this.command;
	}

	public String getDevice() {
		return this.device;
	}

	public RemoteActionType getRemoteActionType() {
		return RemoteActionType.Click;
	}
}