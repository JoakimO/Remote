package se.newbie.remote.device;

/**
 * A remote device represent a device which the system communicate with.
 * Example of systems are "BOXEE", "XBMC" and "Squeezbox".
 *
 * Remote devices are dynamically created on application start or added from the discovery
 * services.
 */
public interface RemoteDevice {
	
	public enum RemoteDeviceType {
		Boxee, TelldusLive;
	}
	
	/**
	 * This method returns the identifier of the remote device instance and is always the
	 * same. 
	 * 
	 * The identifier should not contain any information that might change on the device and
	 * is only used internally. Bad example would be to use IP-address since it might
	 * change for next time.
	 * 
	 * This is the same name as the name given from remote device detail get name method.
	 * 
	 * Good guideline is to use follow "[APPLICATION]" + "-" + "[MAC-ADDRESS]"
	 */
	public String getIdentifier();
	
	/**
	 * Returns the what type of remote device this instance is. 
	 */
	public RemoteDeviceType getRemoteDeviceType();
	
	/**
	 * Returns the name associated with this remote device instance;
	 */
	public String getRemoteDeviceName();
	
	/**
	 * Returns the details for this instance.
	 */
	public RemoteDeviceDetails getRemoteDeviceDetails();
	
	/**
	 * This method is called when the factory founds a already registered device.
	 * 
	 * This will happen when the discoverer find the device again.
	 * 
	 * Devices should only check for essential changes in the details. Changes
	 * can be that host has changed since last run. 
	 * 
	 * Method returns true if properties have changed.
	 */
	public boolean update(RemoteDeviceDetails details);
	
	/**
	 * This method will be invoked when the service is closing or pausing.
	 */	
	public void pause();
	
    /**
     * This method is invoked when the service is starting or resuming from pause.
     */	
	public void resume();
}