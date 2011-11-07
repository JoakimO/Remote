package se.newbie.remote.application;

import android.util.Log;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteController;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteView;
import se.newbie.remote.main.RemoteViewListener;

public class RemoteControllerImpl implements RemoteController, RemoteViewListener {
	private static final String TAG = "RemoteControllerImpl";

	private RemoteModel model;
	private RemoteView view;

	public RemoteControllerImpl( RemoteModel model, RemoteView view) {
		this.model = model;
		this.view = view;
		this.view.addListener(this);
		this.model.addListener(this.view);
	}

	public void executeCommand(String command, String device) {
		Log.v(TAG, "executeCommand: " + command + ", " + device);
		RemoteApplication remoteApplication = RemoteActivity.getRemoteApplication();
		RemoteCommand remoteCommand = remoteApplication.getRemoteCommandFactory().getRemoteCommand(command, device);
		if (remoteCommand != null) {
			remoteCommand.execute();
		} else {
			Log.w(TAG, "No command found " + command + ", " + device);
		}
	}

	public void setSelectedRemoteDevice(String device) {
		Log.v(TAG, "setSelectedRemoteDevice: " + device);
		RemoteApplication remoteApplication = RemoteActivity.getRemoteApplication();
		RemoteDevice remoteDevice = remoteApplication.getRemoteDeviceFactory().getRemoteDevice(device);
		this.model.setSelectedRemoteDevice(remoteDevice);
	}
}