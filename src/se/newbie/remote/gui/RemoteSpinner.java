package se.newbie.remote.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.action.RemoteActionListener;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import se.newbie.remote.main.RemoteModelParameters;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;

/**
 *
 */
public class RemoteSpinner extends Spinner implements RemoteGUIComponent, RemoteModelListener, OnItemSelectedListener  {
	private static final String TAG = "RemoteSpinner";
	
	String command;
	String device;
	List<RemoteActionListener> listeners = new ArrayList<RemoteActionListener>();
	
	public RemoteSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        if (!this.isInEditMode()) {
        	setOnItemSelectedListener(this);
        	addListener(RemoteApplication.getInstance().getRemoteView());
        	RemoteApplication.getInstance().getRemoteModel().addListener(this);
        }
        Log.v(TAG, "Remote spinner initialized: " + command + ";" + device);
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
	
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		Log.v(TAG, "onItemSelected: " + position);
		selectionPerformed(parent.getItemAtPosition(position));
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Log.v(TAG, "onNothingSelected");
	}
	
	private void selectionPerformed(Object value) {
		RemoteModel remoteModel = RemoteApplication.getInstance().getRemoteModel();
		RemoteModelParameters params = remoteModel.getRemoteModelParameters(getDevice(), getCommand());
		params.putObjectParam("value", value);
		remoteModel.setRemoteModelParameters(getDevice(), getCommand(), params);
		
		ClickRemoteAction action = new ClickRemoteAction(getCommand(), getDevice());
		actionPerformed(action);
	}

	public void update(final RemoteModel model) {
		this.post(new Runnable() {
			public void run() {
				RemoteModelParameters params = model.getRemoteModelParameters(getDevice(), getCommand());
				if (params.containsParam("adapter")) {
					BaseAdapter adapter = (BaseAdapter)params.getObjectParam("adapter");
					if (getAdapter() == null || (getAdapter() != null && !getAdapter().equals(adapter))) {
						Log.v(TAG, "setAdapter");
						setAdapter(adapter);		
					}
				}
				if (params.containsParam("selectionPosition")) {
					int position = params.getIntParam("selectionPosition");
					if (getSelectedItemPosition() != position) {
						setSelection(position);
					}
				}				
			}			
		});
	}		

	public String getCommand() {
		return this.command;
	}

	public String getDevice() {
		return this.device;
	}
}