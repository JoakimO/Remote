package se.newbie.remote.main;

/**
 * This instances of this interface is used for view object to be able to 
 * send values to the handlers and handlers to send values to the view.
 * 
 * Instances of this class is created by the remote model with 
 * getRemoteModelParameters(device, key). Device is the identifier
 * of a remote device and the key is the identifier of a remote command
 */
public interface RemoteModelParameters {
	
	/**
	 * Returns true if the given parameter exists. 
	 */
	public boolean containsParam(String key);
	
	/**
	 * Returns the given parameter as a int. If the parameter was not
	 * able to convert to a int or don't exists NULL is returned. 
	 */
	public int getIntParam(String key);

	/**
	 * Returns the given parameter as a float. If the parameter was not
	 * able to convert to a float or don't exists NULL is returned. 
	 */	
	public float getFloatParam(String key);
	
	/**
	 * Returns the given parameter as a String. If the parameter was not
	 * able to convert to a String or don't exists NULL is returned. 
	 */	
	public String getStringParam(String key);

	/**
	 * Returns the given parameter as a Object. If the parameter was not
	 * able to convert to a Object or don't exists NULL is returned. 
	 */
	public Object getObjectParam(String key);
	
	public void putIntParam(String key, Integer value);
	
	public void putFloatParam(String key, Float value);
	
	public void putStringParam(String key, String value);
	
	public void putObjectParam(String key, Object value);
	
	/**
	 * Returns true if no values are different from the given
	 * parameters. 
	 */
	public boolean isChanged(RemoteModelParameters params);
}
