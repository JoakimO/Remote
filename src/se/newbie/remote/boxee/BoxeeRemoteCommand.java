package se.newbie.remote.boxee;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelParameters;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.util.Log;
//http://192.168.0.101:8800/xbmcCmds/xbmcHttp?command=SendKey%28271%29
public class BoxeeRemoteCommand implements RemoteCommand {
	private static final String TAG = "BoxeeRemoteCommand";

	
	public enum Command {
		select("Input", "Select")
		,back("Input", "Back")
		,up("Input", "Up")
		,down("Input", "Down")
		,left("Input", "Left")
		,right("Input", "Right")
		,home("Input", "Home")
		,muteToggle("XBMC", "ToggleMute")
		,volumeUp("XBMC", "SetVolume")
		,volumeDown("XBMC", "SetVolume")
		,play("%player", "PlayPause")
		,stop("%player", "Stop")
		,skipNext("%player", "SkipNext")
		,skipPrevious("%player", "SkipPrevious")
		,seek("%player", "SeekPercentage")
		;
		
		private String namespace;
		private String method;
		
		Command(String namespace, String method) {
			this.namespace = namespace;
			this.method = method;
		}
		
		String getMethod() {
			return method;
		}
		
		String getNamespace() {
			return namespace;
		}		
	}
	
	private Command command;
	private BoxeeRemoteDevice boxeeRemoteDevice;
	
	public BoxeeRemoteCommand(BoxeeRemoteDevice device, Command command) {
		this.boxeeRemoteDevice = device;
		this.command = command;
	}
	
	public String getIdentifier() {
		return command.name();
	}

	public int execute() {
		Log.v(TAG, "execute: " + getIdentifier());
		
		RemoteModel remoteModel = RemoteApplication.getInstance().getRemoteModel();
		RemoteModelParameters params;
		
		JSONRPC2Request request;
		switch (command) {
			case volumeUp:
				return setVolume(10);			
			case volumeDown:
				return setVolume(-10);				
			case seek:
				params = remoteModel.getRemoteModelParameters(boxeeRemoteDevice.getIdentifier(), command.name());
				request = boxeeRemoteDevice.getConnection().createJSONRPC2Request(getMethod(command));
				request.setParam("value", params.getIntParam("value"));
				break;
			default:
				request = boxeeRemoteDevice.getConnection().createJSONRPC2Request(getMethod(command));
				break;
		}
		return boxeeRemoteDevice.sendCommand(request);
	}
	
	public int setVolume(final int volume) {
    	final BoxeeRemoteDeviceConnection connection = boxeeRemoteDevice.getConnection();
    	
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				try {
					int playerVolume = response.getIntResult();
					playerVolume = playerVolume + volume;
					JSONRPC2Request request = connection.createJSONRPC2Request("XBMC.SetVolume");
					if (request != null) {
						request.setParam("value",  playerVolume);
						connection.sendRequest(request, null);
					}  					
					
					
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
    		
    	};    	
		JSONRPC2Request request = connection.createJSONRPC2Request("XBMC.GetVolume");
		if (request != null) {
			connection.sendRequest(request, responseHandler);
		}  		
		return 1;
	}
	
	public String getMethod(Command command) {
		String method = command.getNamespace() + "." + command.getMethod();
		if (boxeeRemoteDevice.getBoxeePlayerState() != null && 
				boxeeRemoteDevice.getBoxeePlayerState().getActivePlayer() != null) {
			String activePlayer = boxeeRemoteDevice.getBoxeePlayerState().getActivePlayer().name();
			method = method.replace("%player", activePlayer);
		}
		Log.v(TAG, "Method: " + method);
		return method;
	}
}
