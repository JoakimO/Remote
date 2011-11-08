package se.newbie.remote.boxee;

import se.newbie.remote.util.jsonrpc2.JSONRPC2Client;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.util.Log;

public class BoxeeRemoteDeviceConnection {
	BoxeeRemoteDevice boxeeRemoteDevice;
	//private boolean isKeepAlive;
	private JSONRPC2Client jsonRPC2Client;
	private boolean isConnected = false;
	
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
	
	public void sendRequest(JSONRPC2Request request, JSONRPC2ResponseHandler handler) {
		BoxeeRemoteDeviceSendRequestThread thread = new BoxeeRemoteDeviceSendRequestThread(request, handler);
		thread.start();
	}
	
	public JSONRPC2Request createJSONRPC2Request(String method) {
		return jsonRPC2Client.createJSONRPC2Request(method);
	}	
	
	private class BoxeeRemoteDeviceSendRequestThread extends Thread {
		private static final String TAG = "BoxeeRemoteDeviceSendRequestThread"; 
		
		private JSONRPC2Request request;
		private JSONRPC2ResponseHandler handler;
		private long timeout = 3000L;
		private static final long threadSleep = 100L;

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}		
		
		public BoxeeRemoteDeviceSendRequestThread(JSONRPC2Request request, JSONRPC2ResponseHandler handler) {
			this.request = request;
			this.handler = handler;
		}
		
		@Override
        public void run() {
			long runTime = 0L;
			while (!isConnected && !jsonRPC2Client.isConnected()) {
				try {
					Thread.sleep(threadSleep);
				} catch (InterruptedException e) {
					Log.v(TAG, e.getMessage());
				}
				runTime += threadSleep;
				if (runTime > timeout) {
					Log.w(TAG, "Request Timed out:" + request.serialize());
					return;
				}				
			}
			Log.v(TAG, "SendRequest: " + request.serialize());
			jsonRPC2Client.sendRequest(request, handler);
		}
	}
	
	private class BoxeeRemoteDeviceDisconnectThread extends Thread {
		@Override
        public void run() {
			isConnected = false;
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
				isConnected = true;
			} catch (Exception innerException) {
				Log.e(TAG, innerException.getMessage()); 
			}
		}		
	}
}
