package se.newbie.remote.tellduslive;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.tellduslive.gui.TelldusLiveDeviceListView;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TelldusLiveMainRemoteDisplay implements RemoteDisplay {
	private final static String TAG = "TelldusLiveMainRemoteDisplay";
	protected final static String IDENTIFIER = "MainDisplay";

	private TelldusLiveRemoteDevice device;
	private Fragment fragment;
	private TelldusLiveDeviceListView deviceListView;
	
	public TelldusLiveMainRemoteDisplay(TelldusLiveRemoteDevice device) {
		this.device = device;		
	}
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public Fragment createFragment() {
		return new TelldusLiveMainRemoteDisplayFragment();
	}
	
	public void invalidate() {
		Log.v(TAG, "Invalidate display");
		if (deviceListView != null) {
			deviceListView.update();
		}
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
		View view = this.fragment.getView();
		deviceListView = (TelldusLiveDeviceListView)view.findViewById(R.id.telldus_live_device_list);
		if (deviceListView != null) {
			Log.v(TAG, "Update display");
			deviceListView.setDevice(device.getIdentifier());
			deviceListView.update();
		}
		
		Button addDeviceButton = (Button)view.findViewById(R.id.telldus_live_add_device_button);
		addDeviceButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			TelldusLiveRemoteDeviceDetails details = (TelldusLiveRemoteDeviceDetails)device.getRemoteDeviceDetails();
    			RemoteApplication.getInstance().showDialog(TelldusLiveAddDeviceDialog.newInstance(details));	
    		}
    	}); 
	}
}
