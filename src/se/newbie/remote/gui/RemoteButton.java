package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.action.RemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

/**
 *
 */
public abstract class RemoteButton extends Button implements RemoteGUIComponent {
	private static final String TAG = "RemoteButton";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public RemoteButton(Context context) {
		super(context);
	}	

	public void addListener(RemoteActionListener listener) {
		this.listeners.add(listener);
	}

	protected void actionPerformed(RemoteAction action) {
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