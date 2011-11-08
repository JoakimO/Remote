package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.main.RemoteModelListener;
import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;

/**
 *
 */
public abstract class RemoteSeekBar extends SeekBar implements RemoteGUIComponent, RemoteModelListener  {
	private static final String TAG = "RemoteSeekBar";
	
	String command;
	String device;
	private int value;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public RemoteSeekBar(Context context) {
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}