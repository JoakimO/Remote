package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.application.RemoteApplication;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class RemoteButton extends Button implements RemoteGUIComponent {
	private static final String TAG = "RemoteButton";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public RemoteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
		
        if (!this.isInEditMode()) {
        	setOnClickListener(new View.OnClickListener() {
        		public void onClick(View v) {
        			clickPerformed();
        		}
	        });
	        addListener(RemoteApplication.getInstance().getRemoteView());
        }
	}
	
    private final void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		CharSequence s = a.getString(R.styleable.remote_command);
		command = s.toString();
		s = a.getString(R.styleable.remote_device);
		device = s.toString();		
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

	public String getCommand() {
		return this.command;
	}

	public String getDevice() {
		return this.device;
	}
}