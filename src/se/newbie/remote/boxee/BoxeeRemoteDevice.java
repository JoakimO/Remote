package se.newbie.remote.boxee;

import java.io.InputStream;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.util.HttpRequestTask;
import se.newbie.remote.util.HttpRequestTaskHandler;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BoxeeRemoteDevice implements RemoteDevice {
	private static final String TAG = "BoxeeRemoteDevice";
	
	private BoxeeRemoteDeviceDetails details;
	private BoxeeRemoteDeviceConnection connection;
	
	public BoxeeRemoteDevice(BoxeeRemoteDeviceDetails details) {
		this.details = details;
		this.connection = new BoxeeRemoteDeviceConnection(this);
	}
	
	public String getIdentifier() {
		return details.getIdentifier();
	}
	
	public RemoteDeviceType getRemoteDeviceType() {
		return RemoteDeviceType.Boxee;
	}
	
	public String getRemoteDeviceName() {
		return details.getName();
	}	

	public RemoteDeviceDetails getRemoteDeviceDetails() {
		return details;
	}

	public int sendKey(int key) {
		sendHttpRequestTask(String.format("http://%s:%s/xbmcCmds/xbmcHttp?command=SendKey(%s)", details.getHost(), details.getPort(), key), null);
		return 1;
	}
	
	public int sendCommand(String command, String argument) {
		sendHttpRequestTask(String.format("http://%s:%s/xbmcCmds/xbmcHttp?command=%s(%s)", details.getHost(), details.getPort(), command, argument), null);
		return 1;	
	}
	
	public int sendCommand(final BoxeeRemoteCommand.Command command) {
		String value = "";
		for (String argument : command.getArguments()) {
			if (!value.equals("") && !value.endsWith(",")) {
				value += ",";
			}
			if (!argument.equals("")) {
				value += argument;
			}
		}
		
		HttpRequestTaskHandler handler = new HttpRequestTaskHandler() {
			public void onSuccess(InputStream out) {
			}		
		};
		
		
		sendHttpRequestTask(String.format("http://%s:%s/xbmcCmds/xbmcHttp?command=%s(%s)", details.getHost(), details.getPort(), command.getMethod(), value), handler);
		
		
		

		return 1;	
	}	
	
	private void sendHttpRequestTask(String url, HttpRequestTaskHandler handler) {
		HttpRequestTask request = new HttpRequestTask();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		
		if (preferences.contains(getIdentifier() + ".user")) {
			request.setCredentials(preferences.getString(getIdentifier() + ".user", "")
					, preferences.getString(getIdentifier() + ".password", ""));
		}
		if (handler != null) {
			request.setHttpRequestTaskHandler(handler);
		}
		request.execute(url);
	}
	
	/**
	 * Only update the host information if the device was found again.
	 */
	public boolean update(RemoteDeviceDetails details) {
		boolean b = false;
		if (details.getIdentifier().startsWith(BoxeeRemoteDeviceDiscoverer.APPLICATION)) {
			if (!((BoxeeRemoteDeviceDetails)details).getHost().equals(this.details.getHost())) {
				this.details.setHost(((BoxeeRemoteDeviceDetails)details).getHost());
				b = true;
			}
			if (!((BoxeeRemoteDeviceDetails)details).getPort().equals(this.details.getPort())) {
				this.details.setPort(((BoxeeRemoteDeviceDetails)details).getPort());
				b = true;
			}			
		}	
		return b;
	}

	public void pause() {
		getConnection().pause();
		
	}

	public void resume() {
		getConnection().resume();
		
	}
	
	public void addNotificationListener(JSONRPC2NotificationListener listener) {
		getConnection().addNotificationListener(listener);
	}

	public BoxeeRemoteDeviceConnection getConnection() {
		return connection;
	}
}