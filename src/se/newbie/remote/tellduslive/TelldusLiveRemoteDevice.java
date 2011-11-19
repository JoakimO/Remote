package se.newbie.remote.tellduslive;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import android.util.Log;

public class TelldusLiveRemoteDevice implements RemoteDevice {
	private static final String TAG = "TelldusLiveRemoteDevice";
	
	TelldusLiveRemoteDeviceDetails details;
	TelldusLiveRemoteDeviceConnection connection;
	
	public TelldusLiveRemoteDevice(RemoteDeviceDetails details) {
		this.details = (TelldusLiveRemoteDeviceDetails)details;
		connection = new TelldusLiveRemoteDeviceConnection(this);
	}
	
	public String getIdentifier() {
		return details.getIdentifier();
	}

	public RemoteDeviceType getRemoteDeviceType() {
		return RemoteDeviceType.TelldusLive;
	}

	public String getRemoteDeviceName() {
		return TelldusLiveRemoteDeviceDiscoverer.APPLICATION;
	}

	public RemoteDeviceDetails getRemoteDeviceDetails() {
		return details;
	}

	public void pause() {
		Log.v(TAG, "Pause");
	}

	public void resume() {
		Log.v(TAG, "Resume");
		
		TelldusLiveAuthenticateDialog dialog = TelldusLiveAuthenticateDialog.newInstance((TelldusLiveRemoteDeviceDetails)this.getRemoteDeviceDetails());
		Log.v(TAG, "Dialog: " + dialog);
		if (dialog != null) {
			RemoteApplication.getInstance().showDialog(dialog);
		}
	}

	public boolean update(RemoteDeviceDetails details) {
		boolean b = false;
		if (details.getIdentifier().startsWith(TelldusLiveRemoteDeviceDiscoverer.APPLICATION)) {
			if (((TelldusLiveRemoteDeviceDetails)details).getAccessToken() != null &&
					!((TelldusLiveRemoteDeviceDetails)details).getAccessToken().equals(this.details.getAccessToken())) {
				this.details.setAccessToken(((TelldusLiveRemoteDeviceDetails)details).getAccessToken());
				b = true;
			}		
		}
		return b;
	}	
	
	public TelldusLiveRemoteDeviceConnection getConnection() {
		return connection;
	}
}
