package se.newbie.remote.predefined;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceListener;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelParameters;
import android.util.Log;

public class SelectRemoteDeviceRemoteCommand implements RemoteCommand,
		RemoteDeviceListener {
	private final static String TAG = "SelectRemoteDeviceRemoteCommand";
	private final static String IDENTIFIER = "selectRemoteDevice";

	private String device;
	private List<RemoteDevice> remoteDevices = new ArrayList<RemoteDevice>();

	public SelectRemoteDeviceRemoteCommand(String device) {
		this.device = device;
	}

	public String getIdentifier() {
		return IDENTIFIER;
	}

	public int execute() {
		Log.v(TAG, "Execute: " + device);
		RemoteModel remoteModel = RemoteApplication.getInstance()
				.getRemoteModel();
		RemoteModelParameters params = remoteModel.getRemoteModelParameters(
				device, IDENTIFIER);
		if (params.containsParam("value")) {
			RemoteDevice remoteDevice = (RemoteDevice) params
					.getObjectParam("value");
			if (!remoteDevice.equals(remoteModel.getSelectedRemoteDevice())) {
				remoteModel.setSelectedRemoteDevice(this, remoteDevice);
			}
			return 1;
		}
		return 0;
	}

	public void remoteDeviceInitialized(RemoteDevice remoteDevice) {
		Log.v(TAG, "remoteDeviceInitialized: " + remoteDevice.getIdentifier());
		remoteDevices.add(remoteDevice);

		RemoteDeviceBaseAdapter adapter = new RemoteDeviceBaseAdapter(
				R.layout.standard_device_spinner_item);
		adapter.setRemoteDevices(remoteDevices);

		RemoteModel remoteModel = RemoteApplication.getInstance()
				.getRemoteModel();
		RemoteModelParameters params = remoteModel.getRemoteModelParameters(
				device, IDENTIFIER);

		params.putObjectParam("adapter", adapter);

		if (remoteModel.getSelectedRemoteDevice() != null) {
			params.putObjectParam("selectionPosition", remoteDevices
					.indexOf(remoteModel.getSelectedRemoteDevice()));
		}
		remoteModel.setRemoteModelParameters(this, device, IDENTIFIER, params);
	}

}
