package se.newbie.remote.universal;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.device.RemoteDevice;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RemoteDeviceBaseAdapter extends BaseAdapter {

	private Activity activity;
	private List<RemoteDevice> devices;
	private int resource;
	
	public RemoteDeviceBaseAdapter(Activity activity, List<RemoteDevice> devices, int resource) {
		this.activity = activity;
		this.devices = new ArrayList<RemoteDevice>();
		this.devices.addAll(devices);
		this.resource = resource;
	}

	public int getCount() {
		return devices.size();
	}

	public Object getItem(int position) {
		return devices.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(resource, null);
		
		TextView textView = (TextView)view.findViewById(R.id.standard_device_spinner_text);
		RemoteDevice device = devices.get(position);
		
		textView.setText(device.getRemoteDeviceName());
		return view;
	}
	
	
}
