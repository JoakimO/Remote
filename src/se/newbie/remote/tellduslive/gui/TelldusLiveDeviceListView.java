package se.newbie.remote.tellduslive.gui;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.tellduslive.TelldusLiveDevice;
import se.newbie.remote.tellduslive.TelldusLiveRemoteCommand;
import se.newbie.remote.tellduslive.TelldusLiveRemoteDevice;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TelldusLiveDeviceListView extends ListView {
	private final static String TAG = "DeviceListView";
	private String device;

	private List<TelldusLiveDevice> devices;
	private TelldusLiveDeviceAdapter adapter;

	public TelldusLiveDeviceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.v(TAG, "Create TelldusLive device list");
		initAttributes(attrs);
		adapter = new TelldusLiveDeviceAdapter(context,	R.layout.telldus_live_device_list_adapter_item);
		this.setAdapter(adapter);
	}

	private final void initAttributes(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.remote);
		device = a.getString(R.styleable.remote_device);
	}

	private void updateView() {
		this.post(new Runnable() {
			public void run() {
				Log.v(TAG, "Update TelldusLive device list: " + devices.size());
				adapter.clear();
				if (devices != null) {
					adapter.addDevices(devices);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void update() {
		if (device != null) {
			Log.v(TAG, "Update device list");
			RemoteDeviceFactory factory = RemoteApplication.getInstance()
					.getRemoteDeviceFactory();
			TelldusLiveRemoteDevice telldusLiveRemoteDevice = (TelldusLiveRemoteDevice) factory
					.getRemoteDevice(device);
			devices = telldusLiveRemoteDevice.getTelldusLiveDevices();
			updateView();
		}
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
		this.adapter.setRemoteDeviceIdentifier(device);
	}

	class TelldusLiveDeviceAdapter extends BaseAdapter {
		private static final String TAG = "TelldusLiveDeviceAdapter"; 
		
		private List<TelldusLiveDevice> devices = new ArrayList<TelldusLiveDevice>();
		private int resource;
		private Context context;
		private String remoteDeviceIdentifier;

		public TelldusLiveDeviceAdapter(Context context, int resource) {
			this.resource = resource;
			this.context = context;
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

		public void clear() {
			devices.clear();
		}

		public void addDevice(TelldusLiveDevice device) {
			devices.add(device);
		}

		public void addDevices(List<TelldusLiveDevice> devices) {
			this.devices.addAll(devices);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = inflater.inflate(resource, null);
			TelldusLiveDevice device = devices.get(position);
			TextView textView = (TextView) view
					.findViewById(R.id.telldus_live_device_list_item_headline);
			textView.setText(device.getName());

			TelldusLiveActionButton onButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_on);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_ON) == TelldusLiveDevice.METHOD_ON) {
				onButton.setDevice(remoteDeviceIdentifier);
				onButton.setCommand(TelldusLiveRemoteCommand.Command.turnOn.name() + "-" + device.getId());
			} else {
				onButton.setVisibility(INVISIBLE);
			}				

			TelldusLiveActionButton offButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_off);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_OFF) == TelldusLiveDevice.METHOD_OFF) {
				offButton.setDevice(remoteDeviceIdentifier);
				offButton.setCommand(TelldusLiveRemoteCommand.Command.turnOff.name() + "-" + device.getId());
			} else {
				offButton.setVisibility(INVISIBLE);
			}
			
			TelldusLiveActionButton learnButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_learn);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_LEARN) == TelldusLiveDevice.METHOD_LEARN) {
				learnButton.setDevice(remoteDeviceIdentifier);
				learnButton.setCommand(TelldusLiveRemoteCommand.Command.learn.name() + "-" + device.getId());
			} else {
				learnButton.setVisibility(INVISIBLE);
			}
			
			TelldusLiveActionButton bellButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_bell);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_BELL) == TelldusLiveDevice.METHOD_BELL) {
				bellButton.setDevice(remoteDeviceIdentifier);
				bellButton.setCommand(TelldusLiveRemoteCommand.Command.bell.name() + "-" + device.getId());
			} else {
				bellButton.setVisibility(INVISIBLE);
			}
			
			TelldusLiveActionButton upButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_up);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_UP) == TelldusLiveDevice.METHOD_UP) {
				upButton.setDevice(remoteDeviceIdentifier);
				upButton.setCommand(TelldusLiveRemoteCommand.Command.up.name() + "-" + device.getId());
			} else {
				upButton.setVisibility(INVISIBLE);
			}
			
			TelldusLiveActionButton downButton = (TelldusLiveActionButton) view
					.findViewById(R.id.telldus_live_device_list_item_down);
			if ((device.getMethods() & TelldusLiveDevice.METHOD_DOWN) == TelldusLiveDevice.METHOD_DOWN) {
				downButton.setDevice(remoteDeviceIdentifier);
				downButton.setCommand(TelldusLiveRemoteCommand.Command.down.name() + "-" + device.getId());
			} else {
				downButton.setVisibility(INVISIBLE);
			}									

			Log.v(TAG, "Device: " + device.getId() + ";State: " + device.getState());
			
			ImageView stateImage = (ImageView) view
					.findViewById(R.id.telldus_live_device_list_item_state);
			if ((device.getState() & TelldusLiveDevice.METHOD_ON) == TelldusLiveDevice.METHOD_ON) {
				stateImage.setImageResource(R.drawable.ic_light_on);
			} else {
				stateImage.setImageResource(R.drawable.ic_light_off);
			}

			return view;
		}

		public String getRemoteDeviceIdentifier() {
			return remoteDeviceIdentifier;
		}

		public void setRemoteDeviceIdentifier(String remoteDeviceIdentifier) {
			this.remoteDeviceIdentifier = remoteDeviceIdentifier;
		}
	}

}
