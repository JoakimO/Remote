package se.newbie.remote.action;

public class SeekRemoteAction implements RemoteAction {
	String command;
	String device;
	int	value;
	
	public SeekRemoteAction(String command, String device, int value) {
		this.command = command;
		this.device = device;
		this.value = value;
	}

	public String getCommand() {
		return this.command;
	}

	public String getDevice() {
		return this.device;
	}
	
	public int getValue() {
		return this.value;
	}

	public RemoteActionType getRemoteActionType() {
		return RemoteActionType.Seek;
	}	
}
