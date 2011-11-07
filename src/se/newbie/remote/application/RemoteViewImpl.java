package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteView;
import se.newbie.remote.main.RemoteViewListener;
import se.newbie.remote.action.RemoteAction;

public class RemoteViewImpl implements RemoteView {
	private static final String TAG = "RemoteViewImpl";
	
	List<RemoteViewListener> listeners;

	public RemoteViewImpl() {
		this.listeners = new ArrayList<RemoteViewListener>();
	}
	
	public void addListener(RemoteViewListener listener) {
		this.listeners.add(listener);		
	}

	public void actionPerformed(RemoteAction action) {
		Log.v(TAG, "actionPerformed: " + action.getCommand() + ", " + action.getDevice() + " to " + listeners.size() + " listeners");
		for (RemoteViewListener listener : listeners) {
			listener.executeCommand(action.getCommand(), action.getDevice());
		}
	}

	public void update(RemoteModel model) {
	}
}