package se.newbie.remote.tellduslive.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.application.RemoteApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 *
 */
public class TelldusLiveActionButton extends ImageButton {
	private static final String TAG = "TelldusLiveActionButton";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public TelldusLiveActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        if (!this.isInEditMode()) {
        	setOnClickListener(new View.OnClickListener() {
        		public void onClick(View v) {
        			clickPerformed();
        		}
        	});   
	        addListener(RemoteApplication.getInstance().getRemoteView());
        }
	}	
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.v(TAG, "onDetachedFromwindow");
		listeners = null;
	}		
    
	private void clickPerformed() {
		ClickRemoteAction action = new ClickRemoteAction(getCommand(), getDevice());
		actionPerformed(action);
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