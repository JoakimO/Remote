package se.newbie.remote.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class RemoteDisplayFactory {
	private static final String TAG = "RemoteDisplaydFactory";
	private Map<String, List<RemoteDisplay>> displays = new HashMap<String, List<RemoteDisplay>>();	
	
	public RemoteDisplay getRemoteDisplay(String display, String device) {
		RemoteDisplay remoteDisplay = null;
		if (this.displays.containsKey(device)) {
			for (RemoteDisplay internalRemoteDisplay : this.displays.get(device)) {
				if (display.equals(internalRemoteDisplay.getIdentifier())) {
					remoteDisplay = internalRemoteDisplay;
					break;
				}
			}
		}
		return remoteDisplay;
	}	
	
	public void unregisterDisplay(String device, RemoteDisplay remoteDisplay) {
		List<RemoteDisplay> list = getDeviceDisplays(device);
		list.remove(remoteDisplay);
	}
	
	public void registerDisplay(String device, RemoteDisplay remoteDisplay) {
		List<RemoteDisplay> list = getDeviceDisplays(device);
		list.add(remoteDisplay);
		Log.v(TAG, "Display registered : " + remoteDisplay.getIdentifier() + "," + device);
	}
	

	
	private List<RemoteDisplay> getDeviceDisplays(String device) {
		List<RemoteDisplay> list;
		if (this.displays.containsKey(device)) {
			list = this.displays.get(device);
		} else {
			list = new ArrayList<RemoteDisplay>();
			this.displays.put(device, list);
		}
		return list;
	}	
}
