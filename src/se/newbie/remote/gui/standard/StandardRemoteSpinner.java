package se.newbie.remote.gui.standard;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.gui.RemoteSpinner;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import se.newbie.remote.main.RemoteModelParameters;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

public class StandardRemoteSpinner extends RemoteSpinner implements OnItemSelectedListener, RemoteModelListener   {
	private final static String TAG = "StandardRemoteSpinner";

	public StandardRemoteSpinner(Context context, String device, String command) {
		super(context);
		setDevice(device);
		setCommand(command);
		setBackgroundResource(R.drawable.standard_spinner);
		setOnItemSelectedListener(this);
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

	public void update(RemoteModel model) {
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
	
	
}
