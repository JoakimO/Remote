package se.newbie.remote.util.jsonrpc2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONRPC2Client {
	private static final String TAG = "JSONRPC2Client";
	private int requestIndex;
	
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private JSONRPC2ClientThread thread;
	private boolean isThreadRunning = true;
	
	private Map<Integer, JSONRPC2ResponseHandler> handlers;
	private List<JSONRPC2NotificationListener> notificationListeners = new ArrayList<JSONRPC2NotificationListener>();
	
	
	public boolean isConnected() {
		return (socket != null) ? socket.isConnected() : false;
	}
	
	/**
	 * Opens a JSON RPC 2 connection to the given host and port.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void connect(String host, int port) {
		requestIndex = 0;
		isThreadRunning = true;
		handlers = new HashMap<Integer, JSONRPC2ResponseHandler>();
		try {
			Log.d(TAG, "Connecting: " + host + ":" + port);
			socket = new Socket(host, port);
			Log.d(TAG, "Connected");
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}		
		thread = new JSONRPC2ClientThread();
		thread.start();				
	}
	
	/**
	 * Disconnect from the server.
	 */
	public void disconnect() {
		isThreadRunning = false;
		try {
			if (socket != null && socket.isConnected()) {
				socket.close();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
	}
	
	/**
	 * Creates a JSONRPC2Request template with set ID and Method.
	 * @return
	 * @throws JSONException 
	 */
	public synchronized JSONRPC2Request createJSONRPC2Request(String method) {
		try {
			return new JSONRPC2Request(method, requestIndex++);
		} catch (JSONException e) {
			return null;
		}
	}
	
	/**
	 * Serialize the JSON RPC 2 message and writes it to the open connection.
	 * 
	 * If response handler 
	 */
	public void sendRequest(JSONRPC2Request request, JSONRPC2ResponseHandler handler) {
		if (socket != null && socket.isConnected()) {
			String s = request.serialize();
			if (s != null) {
				if (handler != null) {
					handlers.put(request.getId(), handler);
				}
				
				Log.d(TAG, "sendRequest: " + s);
				byte[] buffer = s.getBytes();
				try {
					outputStream.write(buffer, 0, buffer.length);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}
	
	private void notifyHandler(JSONRPC2Response response) {
		Integer id = response.getId();
		if (handlers.containsKey(id)) {
			Log.v(TAG, "sendResponse: " + response.serialize());
			JSONRPC2ResponseHandler handler = handlers.get(id);
			handlers.remove(id);
			handler.onResponse(response);
		} else {
			Log.v(TAG, "No handler for response: " + response.serialize());
		}
	}
	
	/**
	 * Adds a notification listener for this connection, a notification is a request with out ID.
	 */
	public void addNotificationListener(JSONRPC2NotificationListener listener) {
		notificationListeners.add(listener);
	}
	
	/**
	 * 
	 */
	public void notifyListener(JSONRPC2Notification notification) {
		Log.v(TAG, "Received notification");
		Log.v(TAG, notification.serialize());
		for (JSONRPC2NotificationListener listener : notificationListeners) {
			listener.onNotification(notification);
		}
	}
	
	/**
	 * This thread listen for notification and responses from the server
	 */
	protected class JSONRPC2ClientThread extends Thread {
		private static final String TAG = "JSONRPC2ClientThread";
		
		public void run() {
			try
			{
				BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));				
				while (isThreadRunning) {
					try
					{
						if (socket.isConnected()) {
							String s = buf.readLine();
							if (s != null) {
								Log.v(TAG, "Received: " + s);
								JSONObject jsonObject = new JSONObject(s);
								if (jsonObject.has("id")) {
									JSONRPC2Response response = new JSONRPC2Response(jsonObject);
									notifyHandler(response);
								} else {
									JSONRPC2Notification notification = new JSONRPC2Notification(jsonObject);
									notifyListener(notification);
								}
							}
						} else {
							Thread.sleep(1000);
						}
					} catch (Exception innerException) {
						Log.e(TAG, innerException.getMessage());
					}
				}
			} catch (Exception exception) {
				Log.e(TAG, exception.getMessage());
			}
		}
	
	}
}
