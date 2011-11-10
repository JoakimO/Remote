package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.main.RemoteModelListener;
import android.content.Context;
import android.util.Log;
import android.widget.Spinner;

/**
 *
 */
public abstract class RemoteSpinner extends Spinner implements RemoteGUIComponent, RemoteModelListener  {
	private static final String TAG = "RemoteSpinner";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public RemoteSpinner(Context context) {
		super(context);
	}	

	public void addListener(RemoteActionListener listener) {
		this.listeners.add(listener);
	}

	protected void actionPerformed(ClickRemoteAction action) {		
		Log.v(TAG, "actionPerformed: " + action.getCommand() + ", " + action.getDevice());
		for (RemoteActionListener listener : listeners) {
			listener.actionPerformed(action);
		}
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return this.device;
	}
}