package se.newbie.remote.tellduslive;

import se.newbie.remote.R;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.tellduslive.gui.DeviceListView;
import android.app.Fragment;
import android.view.View;

public class TelldusLiveMainRemoteDisplay implements RemoteDisplay {
	private final static String TAG = "TelldusLiveMainRemoteDisplay";
	private final static String IDENTIFIER = "MainDisplay";

	private TelldusLiveRemoteDevice device;
	private Fragment fragment;
	
	public TelldusLiveMainRemoteDisplay(TelldusLiveRemoteDevice device) {
		this.device = device;		
	}
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public Fragment createFragment() {
		return new TelldusLiveMainRemoteDisplayFragment();
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
		View view = fragment.getView();
		DeviceListView deviceListView = (DeviceListView)view.findViewById(R.id.telldus_live_device_list);
		if (deviceListView != null) {
			deviceListView.setDevice(device.getIdentifier());
			deviceListView.update();
		}
	}
}