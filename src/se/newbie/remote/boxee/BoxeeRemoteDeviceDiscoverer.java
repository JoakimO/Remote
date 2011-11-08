package se.newbie.remote.boxee;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDeviceDetails;
import se.newbie.remote.device.RemoteDeviceDiscoverer;
import se.newbie.remote.device.RemoteDeviceFactory;
import se.newbie.remote.display.RemoteDisplayFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class BoxeeRemoteDeviceDiscoverer implements RemoteDeviceDiscoverer{
	private static final String TAG = "BoxeeRemoteDeviceDiscoverer";
	
	protected static final String APPLICATION = "Boxee";
	
	private static final String REMOTE_KEY = "b0xeeRem0tE!";
	private static final int DISCOVERY_PORT = 2562;
	private static final int TIMEOUT_MS = 1000 * 60;	
	
	private String challenge = "myChallenge"; 
	private boolean paused = false;
	
	private RemoteDeviceFactory remoteDeviceFactory;
	private BoxeeDiscovererThread boxeeDiscovererThread; 
	private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;
	
	public BoxeeRemoteDeviceDiscoverer(RemoteDeviceFactory remoteDeviceFactory) {
		this.remoteDeviceFactory = remoteDeviceFactory;
		
		Context context = RemoteApplication.getInstance().getContext();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,String key) {
				if (key.equals("is_broadcast") || key.equals("is_boxee_broadcast")) {
					if (prefs.getBoolean("is_broadcast", true) && prefs.getBoolean("is_boxee_broadcast", true) && !boxeeDiscovererThread.isAlive()) {
						Log.v(TAG, "Starting broadcast thread");
						boxeeDiscovererThread = new BoxeeDiscovererThread();
						boxeeDiscovererThread.start();
					} 
				}
			}
		};		
		preferences.registerOnSharedPreferenceChangeListener(preferenceListener);		
	}
	
	public void create() {
		
	}
	
	public void resume() {
		paused = false;
		if (isBroadcast() && !(boxeeDiscovererThread != null && boxeeDiscovererThread.isAlive())) {
			boxeeDiscovererThread = new BoxeeDiscovererThread();
			boxeeDiscovererThread.start();
		}	
	}
	
	public void pause() {
		paused = true;
	}
	
	public RemoteDeviceDetails createRemoteDeviceDetails(String details) {
		RemoteDeviceDetails remoteDeviceDetails = null;
		try {
			remoteDeviceDetails = new BoxeeRemoteDeviceDetails(details);
		} catch (Exception e) {
			Log.v(TAG, "Not able to handle incomming details\n");
		}		
		return remoteDeviceDetails;
	}
	
	public RemoteDevice createRemoteDevice(RemoteDeviceDetails details) {
		BoxeeRemoteDevice device = null;
		if (details.getIdentifier().startsWith(APPLICATION)) {
			device = new BoxeeRemoteDevice((BoxeeRemoteDeviceDetails)details);
			
			List<RemoteCommand> commands = new ArrayList<RemoteCommand>();
			for (BoxeeRemoteCommand.Command command : BoxeeRemoteCommand.Command.values()) {
				commands.add(new BoxeeRemoteCommand(device, command));
			}
			RemoteApplication remoteApplication = RemoteApplication.getInstance();
			remoteApplication.getRemoteCommandFactory().registerCommands(device.getIdentifier(), commands);
			
			RemoteDisplayFactory displayFactory = remoteApplication.getRemoteDisplayFactory();
			BoxeeRemoteDisplayFragment remoteDisplay = new BoxeeRemoteDisplayFragment(device);
			displayFactory.registerDisplay(device.getIdentifier(), remoteDisplay);
			BoxeeBrowserFragment browserFragment = new BoxeeBrowserFragment(device);
			displayFactory.registerDisplay(device.getIdentifier(), browserFragment);
			
			device.addNotificationListener(remoteDisplay);
		}
		return device;
	}
	

	private void registerDevice(InetAddress inetAddress, String response) {
		BoxeeRemoteDeviceDetails details = new BoxeeRemoteDeviceDetails();
		details.populateFromDiscovery(inetAddress.getHostAddress(), response);
		if (details.getIdentifier() != null) {
			remoteDeviceFactory.initRemoteDevice(this, details);
		}
	}
	
	private InetAddress getBroadcastInetAddress() {
	    try {
	    	if (RemoteApplication.getInstance().isEmulator()) {
	    		byte[] quads = new byte[]{(byte)192, (byte)168, (byte)0, (byte)101};
	    		//byte[] quads = new byte[]{(byte)192, (byte)168, (byte)0, (byte)159};
	    		//byte[] quads = new byte[]{(byte)192, (byte)168, (byte)0, (byte)4};
	    		return InetAddress.getByAddress(quads); 
	    	} else {
	    	    WifiManager wifi = (WifiManager)RemoteApplication.getInstance().getContext().getSystemService(Context.WIFI_SERVICE);
	    	    DhcpInfo dhcp = wifi.getDhcpInfo();

	    	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    	    byte[] quads = new byte[4];
	    	    for (int k = 0; k < 4; k++)
	    	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    	    return InetAddress.getByAddress(quads);
	    	}
	    } catch (Exception ex) {
	        Log.e(TAG, ex.toString());
	    }
	    return null;
	}	
	
	private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
	    String data = String.format("<?xml version=\"1.0\">\n<BDP1 cmd=\"discover\" application=\"iphone_remote\" challenge=\"%s\" signature=\"%s\"/>", this.challenge, getSignature(this.challenge));
	    Log.v(TAG, "Sending data " + data);

	    DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), getBroadcastInetAddress(), DISCOVERY_PORT);
	    socket.send(packet);
	}
	
	private void listenForResponses(DatagramSocket socket) throws IOException {
	    byte[] buf = new byte[1024];
	    try {
	      while (true) {
	        DatagramPacket packet = new DatagramPacket(buf, buf.length);
	        socket.receive(packet);
	        
	        String s = new String(packet.getData(), 0, packet.getLength());
	        Log.v(TAG, "Received response " + s);
	        
	        registerDevice(packet.getAddress(), s);	        
	      }
	    } catch (SocketTimeoutException e) {
	      Log.v(TAG, "Receive timed out");
	    }
	}	
	
	private String getSignature(String challenge) {
	    MessageDigest digest;
	    byte[] md5sum = null;
	    try {
	      digest = java.security.MessageDigest.getInstance("MD5");
	      digest.update(challenge.getBytes());
	      digest.update(REMOTE_KEY.getBytes());
	      md5sum = digest.digest();
	    } catch (NoSuchAlgorithmException e) {
	      Log.e(TAG, e.getMessage());
	    }

	    StringBuffer hexString = new StringBuffer();
	    for (int k = 0; k < md5sum.length; ++k) {
	      String s = Integer.toHexString((int) md5sum[k] & 0xFF);
	      if (s.length() == 1)
	        hexString.append('0');
	      hexString.append(s);
	    }
	    return hexString.toString();
	}

	/**
	 * This method will check the shared preferences if the thread should be broadcasting.
	 * @return
	 */
	protected boolean isBroadcast() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RemoteApplication.getInstance().getContext());
		boolean b = preferences.getBoolean("is_boxee_broadcast", true);
		b &= preferences.getBoolean("is_broadcast", true);
		return b;
	}
	
	class BoxeeDiscovererThread extends Thread {
		private static final String TAG = "BoxeeDiscovererThread";
		
		private DatagramSocket socket;
		
		@Override
        public void run() {
			try {
				while (isBroadcast() && !paused) {
					socket = new DatagramSocket(DISCOVERY_PORT);
					socket.setBroadcast(true);
					socket.setSoTimeout(TIMEOUT_MS);
					
					sendDiscoveryRequest(socket);
					listenForResponses(socket);
					
					socket.close();
					Thread.sleep(500);
				}
			} catch (Exception innerException) {
				Log.e(TAG, innerException.getMessage());
			}
		}
	}
}

