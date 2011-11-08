package se.newbie.remote.application;

import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.command.RemoteCommandArguments;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteController;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteView;
import se.newbie.remote.main.RemoteViewListener;
import android.util.Log;

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

	public void executeCommand(String command, String device, RemoteCommandArguments arguments) {
		Log.v(TAG, "executeCommand: " + command + ", " + device);
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		RemoteCommand remoteCommand = remoteApplication.getRemoteCommandFactory().getRemoteCommand(command, device);
		if (remoteCommand != null) {
			remoteCommand.execute(arguments);
		} else {
			Log.w(TAG, "No command found " + command + ", " + device);
		}
	}

	public void setSelectedRemoteDevice(String device) {
		Log.v(TAG, "setSelectedRemoteDevice: " + device);
		RemoteApplication remoteApplication = RemoteApplication.getInstance();
		RemoteDevice remoteDevice = remoteApplication.getRemoteDeviceFactory().getRemoteDevice(device);
		this.model.setSelectedRemoteDevice(remoteDevice);
	}
}