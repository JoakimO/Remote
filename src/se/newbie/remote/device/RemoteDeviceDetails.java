package se.newbie.remote.device;

/**
 * This class is used to store all settings and instance related details for a
 * remote device.
 */
public interface RemoteDeviceDetails {

	/**
	 * This method returns the identifier of the remote device instance and is always the
	 * same. 
	 * 
	 * The identifier should not contain any information that might change on the device and
	 * is only used internally. Bad example would be to use IP-address since it might
	 * change for next time.
	 * 
	 * This is the same name as the name given from remote device get name method.
	 * 
	 * Good guideline is to use follow "[APPLICATION]" + "-" + "[MAC-ADDRESS]"
	 */
	public String getIdentifier();
	
	/**
	 * This method serialize all the needed details to be able to recreate the
	 * remote device on startup.
	 */
	public String serialize();
}