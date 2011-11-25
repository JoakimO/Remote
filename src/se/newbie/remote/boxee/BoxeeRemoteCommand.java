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
		,videoPlayerPlay("VideoPlayer", "PlayPause")
		,videoPlayerStop("VideoPlayer", "Stop")
		,videoPlayerSkipNext("VideoPlayer", "SkipNext")
		,videoPlayerSkipPrevious("VideoPlayer", "SkipPrevious")
		,videoPlayerSeek("VideoPlayer", "SeekPercentage")
		,audioPlayerPlay("AudioPlayer", "PlayPause")
		,audioPlayerStop("AudioPlayer", "Stop")
		,audioPlayerSkipNext("AudioPlayer", "SkipNext")
		,audioPlayerSkipPrevious("AudioPlayer", "SkipPrevious")
		,audioPlayerSeek("AudioPlayer", "SeekPercentage")		
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
			case videoPlayerSeek:
				params = remoteModel.getRemoteModelParameters(boxeeRemoteDevice.getIdentifier(), command.name());
				request = boxeeRemoteDevice.getConnection().createJSONRPC2Request(getMethod(command));
				request.setParam("value", params.getIntParam("value"));
				break;
			case audioPlayerSeek:
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
					if (playerVolume > 100) {
						playerVolume = 100;
					}
					if (playerVolume < 0) {
						playerVolume = 0;
					}					
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
		Log.v(TAG, "Method: " + method);
		return method;
	}
}
