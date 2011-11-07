package se.newbie.remote.boxee;

import se.newbie.remote.util.jsonrpc2.JSONRPC2Client;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import android.util.Log;

public class BoxeeRemoteDeviceConnection {
	BoxeeRemoteDevice boxeeRemoteDevice;
	//private boolean isKeepAlive;
	private JSONRPC2Client jsonRPC2Client;
	
	public BoxeeRemoteDeviceConnection(BoxeeRemoteDevice remoteDevice) {
		this.boxeeRemoteDevice = remoteDevice;
		jsonRPC2Client = new JSONRPC2Client();
	}
	
	public void addNotificationListener(JSONRPC2NotificationListener listener) {
		jsonRPC2Client.addNotificationListener(listener);
	}
	
	public void pause() {
		BoxeeRemoteDeviceDisconnectThread thread = new BoxeeRemoteDeviceDisconnectThread();
		thread.start();
	} 
	
	public void resume() {
		BoxeeRemoteDeviceConnectionThread thread = new BoxeeRemoteDeviceConnectionThread();
		thread.start();
	}
	
	private class BoxeeRemoteDeviceDisconnectThread extends Thread {
		@Override
        public void run() {
			jsonRPC2Client.disconnect();
		}
	}
	
	private class BoxeeRemoteDeviceConnectionThread extends Thread {
		private static final String TAG = "BoxeeRemoteDeviceConnectionThread";
		
		@Override
        public void run() {
			BoxeeRemoteDeviceDetails remoteDeviceDetails = (BoxeeRemoteDeviceDetails)boxeeRemoteDevice.getRemoteDeviceDetails();
			try {
				jsonRPC2Client.connect(remoteDeviceDetails.getHost(), 9090);

				JSONRPC2Request request = jsonRPC2Client.createJSONRPC2Request("Device.connect");
				if (request != null) {
					request.setParam("deviceid", "android");
					jsonRPC2Client.sendRequest(request, null);
				}
			} catch (Exception innerException) {
				Log.e(TAG, innerException.getMessage()); 
			}
		}		
	}
}
