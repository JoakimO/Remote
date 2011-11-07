package se.newbie.remote.main;

import java.util.List;

import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.gui.RemoteGUIFactory;

public interface RemoteModel {
	
	public enum Mode {Remote, Design};
	
	public Mode getMode();
	public void addListener(RemoteModelListener listener);
	public void notifyObservers();

	public void setSelectedRemoteDevice(RemoteDevice remoteDevice);
	public RemoteDevice getSelectedRemoteDevice();
	
	public void setRemoteDevices(List<RemoteDevice> remoteDevices);
	public List<RemoteDevice> getRemoteDevices();
	
	public void setRemoteGUIFactory(RemoteGUIFactory remoteGUIFactory);
	public RemoteGUIFactory getRemoteGUIFactory();
}