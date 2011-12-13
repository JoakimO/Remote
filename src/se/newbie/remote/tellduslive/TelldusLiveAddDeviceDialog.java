package se.newbie.remote.tellduslive;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class TelldusLiveAddDeviceDialog extends DialogFragment{
	private static final String TAG = "TelldusLiveAddDeviceDialog";

	private TelldusLiveRemoteDeviceDetails details; 
	private List<TelldusLiveDeviceType> deviceTypes;
	private TelldusLiveDeviceTypeBaseAdapter adapter;
	
	/**
	 * Creates a new dialog for creating new telldus devices. 
	 */
	public static TelldusLiveAddDeviceDialog newInstance(TelldusLiveRemoteDeviceDetails details) {
		
		TelldusLiveAddDeviceDialog dialog = new TelldusLiveAddDeviceDialog();
		
		Bundle args = new Bundle();
		args.putString("details", details.serialize());
		dialog.setArguments(args);
		
		return dialog;
	}
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		String s = getArguments().getString("details");
		try {
			details = new TelldusLiveRemoteDeviceDetails(s);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}		
		
		Resources res = getResources();
		String[] deviceTypeArray = res.getStringArray(R.array.TelldusLiveDeviceTypes);
		deviceTypes = new ArrayList<TelldusLiveDeviceType>();
		for (String deviceTypeJson : deviceTypeArray) {
			TelldusLiveDeviceType deviceType = new TelldusLiveDeviceType(deviceTypeJson);
			deviceTypes.add(deviceType);
		}
	}
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	} 

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.telldus_live_add_device_dialog, container, false);
    	
    	
    	adapter = new TelldusLiveDeviceTypeBaseAdapter();
    	adapter.setTelldusLiveDeviceTypes(deviceTypes);
    	
    	Spinner spinner = (Spinner)view.findViewById(R.id.telldus_live_add_device_type_spinner); 
        spinner.setAdapter(adapter);    	
    	
        Button cancelButton = (Button)view.findViewById(R.id.telldus_live_add_device_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	dismissDialog();
            }
        });
        Button submitButton = (Button)view.findViewById(R.id.telldus_live_add_device_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	submit();
            }
        });        
    	
    	return view;
    }	
    
    public void dismissDialog() {
    	Log.v(TAG, "DismissDialog");
    	this.dismiss();
    }
    
    public void submit() {
    	Log.v(TAG, "Submit");
    }
 
    
    public class TelldusLiveDeviceTypeBaseAdapter extends BaseAdapter {

    	private List<TelldusLiveDeviceType> deviceTypes = new ArrayList<TelldusLiveDeviceType>();
    	
    	public TelldusLiveDeviceTypeBaseAdapter() {
    	}

    	public int getCount() {
    		return deviceTypes.size();
    	}

    	public Object getItem(int position) {
    		return deviceTypes.get(position);
    	}

    	public long getItemId(int position) {
    		return position;
    	}
    	
    	public void setTelldusLiveDeviceTypes(List<TelldusLiveDeviceType> remoteDeviceTypes) {
    		deviceTypes.clear();
    		deviceTypes.addAll(remoteDeviceTypes);
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = (LayoutInflater)RemoteApplication.getInstance().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View view = inflater.inflate(R.layout.default_simple_spinner_dropdown_item, null);
    		TextView textView = (TextView)view.findViewById(R.id.default_simple_spinner_dropdown_text_view);
    		TelldusLiveDeviceType deviceType = deviceTypes.get(position);
    		
    		textView.setText(deviceType.getGroup() + " - " + deviceType.getName());
    		return view;
    	}
    }    
    
}
