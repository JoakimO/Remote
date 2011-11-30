package se.newbie.remote.tellduslive.gui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.tellduslive.TelldusLiveDevice;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDevice;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDeviceConnection.TelldusLiveResponseHandler;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceListView extends LinearLayout {
	private final static String TAG = "DeviceListView";
	private String device;
	
	private List<TelldusLiveDevice> devices = new ArrayList<TelldusLiveDevice>();

	public DeviceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs); 
	}
	
    private final void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		device = a.getString(R.styleable.remote_device);
    }
    
    private void updateView() {
    	this.post(new Runnable() {
			public void run() {
				for (TelldusLiveDevice device : devices) {
					TextView textView = new TextView(getContext());
					textView.setText(device.getName());
					addView(textView);
				}
			}
    	});
    }
    
    public void update() {
    	RemoteDeviceFactory factory = RemoteApplication.getInstance().getRemoteDeviceFactory();
    	TelldusLiveRemoteDevice telldusLiveRemoteDevice = (TelldusLiveRemoteDevice)factory.getRemoteDevice(device); 
    	telldusLiveRemoteDevice.getConnection().request("/devices/list", null, new TelldusLiveResponseHandler() {
			public void onResponse(JSONObject json) {
				try {
					Log.v(TAG, "Devices response: " + json.toString());
					devices.clear();
					JSONArray array = json.optJSONArray("device");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject = array.getJSONObject(i);
							TelldusLiveDevice device = new TelldusLiveDevice(jsonObject);
							devices.add(device);
						}
					}
					updateView();
				} catch (Exception e) {
					Log.e(TAG,  "Error on getting device data: " + e.getMessage());
				}
			}
			
		});	
    }

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
