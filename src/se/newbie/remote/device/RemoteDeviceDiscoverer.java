package se.newbie.remote.device;

public interface RemoteDeviceDiscoverer {
	
	/**
	 * This method starts the discoverer and is called from the factory
	 * on start up.
	 */
	public void create();

	/**
	 * This method will be invoked when the service is closing or pausing.
	 */
    public void pause();
    
    /**
     * This method is invoked when the service is starting or resuming from pause.
     */
    public void resume();  
	
	/**
	 * This method will try to parse the given details, if it could
	 * not be handled NULL must be returned.
	 * 
	 * This method is used during start up of the application to recreate
	 * known remote devices.
	 */
	public RemoteDeviceDetails createRemoteDeviceDetails(String details);
	
	/**
	 * This method will create a remote device from the given details. 
	 */
	public RemoteDevice createRemoteDevice(RemoteDeviceDetails details);
	
	/**
	 * Signals if this device is enabled. Users have the ability to turn on and off each remote device.
	 *
	 * All devices are by default not enabled.
	 *
	 * Best practice is to have a preference on the preference fragment to enable and disable the device.
	 *
	 * <CheckBoxPreference
	 *   android:key="is_[device]_enabled"
	 *   android:title="@string/title_toggle_enabled"
	 *   android:summary="@string/summary_title_toggle_enabled" /> 
	 *
	 * SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
	 * return preferences.getBoolean("is_[device]_enabled", false);
	 *
	 */
	public boolean isEnabled();
	
}