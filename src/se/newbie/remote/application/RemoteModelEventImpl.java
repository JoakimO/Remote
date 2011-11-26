package se.newbie.remote.application;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;

public class RemoteModelEventImpl implements RemoteModelEvent {
	
	private Object source;
	private RemoteModelEventType eventType;
	private RemoteModel remoteModel;
	
	public RemoteModelEventImpl(Object source, RemoteModelEventType eventType, RemoteModel remoteModel) {
		this.source = source;
		this.eventType = eventType;
		this.remoteModel = remoteModel;
	}
	
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	public RemoteModelEventType getEventType() {
		return eventType;
	}
	public void setEventType(RemoteModelEventType eventType) {
		this.eventType = eventType;
	}
	public RemoteModel getRemoteModel() {
		return remoteModel;
	}
	public void setRemoteModel(RemoteModel remoteModel) {
		this.remoteModel = remoteModel;
	}

}
