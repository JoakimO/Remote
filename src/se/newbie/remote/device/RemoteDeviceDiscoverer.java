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
}