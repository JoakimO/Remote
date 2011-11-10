package se.newbie.remote.predefined;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RemoteDeviceBaseAdapter extends BaseAdapter {

	private List<RemoteDevice> devices = new ArrayList<RemoteDevice>();
	private int resource;
	
	public RemoteDeviceBaseAdapter(int resource) {
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
	
	public void setRemoteDevices(List<RemoteDevice> remoteDevices) {
		devices.clear();
		devices.addAll(remoteDevices);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)RemoteApplication.getInstance().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(resource, null);
		
		TextView textView = (TextView)view.findViewById(R.id.standard_device_spinner_text);
		RemoteDevice device = devices.get(position);
		
		textView.setText(device.getRemoteDeviceName());
		return view;
	}
}
