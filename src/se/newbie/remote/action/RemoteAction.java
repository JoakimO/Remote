package se.newbie.remote.action;

public interface RemoteAction {
	
	public enum RemoteActionType {
		Click, Seek
	}
	
	/**
	 * Returns the string value of the command about to be executed.
	 */
	public String getCommand();

	/**
	 * Optional remote device if action targets a specifically device.
	 */
	public String getDevice();
	
	/**
	 * This method returns the action type.
	 */
	public RemoteActionType getRemoteActionType();
}