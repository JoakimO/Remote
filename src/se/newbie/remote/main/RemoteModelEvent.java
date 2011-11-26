package se.newbie.remote.main;

public interface RemoteModelEvent {

	public enum RemoteModelEventType {
		SelectedDeviceChanged, ParameterChanged, RemoteDevicesChanged, PlayerStateChanged
	}
	
	public Object getSource();
	
	public RemoteModelEventType getEventType();
	
	public RemoteModel getRemoteModel();
}
