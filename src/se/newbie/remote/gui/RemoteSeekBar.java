package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;
import se.newbie.remote.main.RemoteModelParameters;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 *
 */
public class RemoteSeekBar extends SeekBar implements RemoteGUIComponent, RemoteModelEventListener, OnSeekBarChangeListener  {
	private static final String TAG = "RemoteSeekBar";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	
	public RemoteSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        
		this.setMax(100);
        if (!this.isInEditMode()) {
        	setOnSeekBarChangeListener(this);
			addListener(RemoteApplication.getInstance().getRemoteView());
			RemoteApplication.getInstance().getRemoteModel().addListener(this);
			update(RemoteApplication.getInstance().getRemoteModel());
        }
	}	
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.v(TAG, "onDetachedFromwindow");
		RemoteApplication.getInstance().getRemoteModel().removeListener(this);
		listeners = new ArrayList<RemoteActionListener>();
	}
	
    private final void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		CharSequence s = a.getString(R.styleable.remote_command);
		command = s.toString();
		s = a.getString(R.styleable.remote_device);
		device = s.toString();		
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
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		seekPerformed(seekBar.getProgress());
	}
	
	private void seekPerformed(int value) {
		RemoteModel remoteModel = RemoteApplication.getInstance().getRemoteModel();
		RemoteModelParameters params = remoteModel.getRemoteModelParameters(getDevice(), getCommand());
		params.putIntParam("value", value);
		remoteModel.setRemoteModelParameters(this, getDevice(), getCommand(), params);
		
		ClickRemoteAction action = new ClickRemoteAction(getCommand(), getDevice());
		actionPerformed(action);
	}

	
	public void onRemoteModelEvent(final RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.ParameterChanged && event.getSource() != this) {
			Log.v(TAG, "onRemoteModelEvent");
			update(event.getRemoteModel());
		}
	}	
	
	private void update(final RemoteModel remoteModel) {
		this.post(new Runnable() {
			public void run() {
				RemoteModelParameters params = remoteModel.getRemoteModelParameters(getDevice(), getCommand());
				setProgress(params.getIntParam("value"));
			}
		});		
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setDevice(String device) {
		this.device = device;
	}	
	
	public String getCommand() {
		return this.command;
	}

	public String getDevice() {
		return this.device;
	}
}