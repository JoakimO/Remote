package se.newbie.remote.action;

public interface RemoteAction {
	/**
	 * Returns the string value of the command about to be executed.
	 */
	String getCommand();

	/**
	 * Optional remote device if action targets a specifically device.
	 */
	String getDevice();
}