package se.newbie.remote.main;

import se.newbie.remote.action.RemoteActionListener;


public interface RemoteView extends RemoteModelListener, RemoteActionListener {
	public void addListener(RemoteViewListener listener);
}