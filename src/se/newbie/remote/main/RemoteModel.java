package se.newbie.remote.main;

import java.util.List;
import java.util.Set;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;

public interface RemoteModel {
	
	public enum Mode {Remote, Design};
	
	public Mode getMode();
	public void addListener(RemoteModelEventListener listener);
	public void removeListener(RemoteModelEventListener listener);
	
	public void notifyObservers(Object source, RemoteModelEventType eventType);

	public void setSelectedRemoteDevice(Object source, RemoteDevice remoteDevice);
	
	public RemoteDevice getSelectedRemoteDevice();
	
	public void setRemoteDevices(Object source, List<RemoteDevice> remoteDevices);
	
	public List<RemoteDevice> getRemoteDevices();

	/**
	 * This method returns the parameters set for the given device and key.
	 * 
	 * If no parameters set was found a new is created.
	 */
	public RemoteModelParameters getRemoteModelParameters(String device, String key);
	
	/**
	 * If any parameters have been change the parameters should be set back even
	 * if it is the same instance.
	 * 
	 * This will notify the observers of the changes.
	 */
	public void setRemoteModelParameters(Object source, String device, String key, RemoteModelParameters params);

	/**
	 * Updates the playerState. 
	 */
	public void updateRemotePlayerState(Object source, RemotePlayerState playerState);
	
	/**
	 * 
	 */
	public RemotePlayerState getRemotePlayerState(String identification);

	/**
	 * 
	 */
	public Set<String> getRemotePlayerStates();
	
	//public void setRemotePlayerState(String device, RemotePlayerState state);
	
	//public RemotePlayerState getRemotePlayerState(String device);
}