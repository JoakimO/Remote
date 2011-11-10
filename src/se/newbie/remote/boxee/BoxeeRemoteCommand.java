package se.newbie.remote.boxee;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.command.RemoteCommand;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelParameters;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import android.util.Log;
//http://192.168.0.101:8800/xbmcCmds/xbmcHttp?command=SendKey%28271%29
public class BoxeeRemoteCommand implements RemoteCommand {
	private static final String TAG = "BoxeeRemoteCommand";

	
	public enum Command {
		select("Input.Select")
		,back("Input.Back")
		,up("Input.Up")
		,down("Input.Down")
		,left("Input.Left")
		,right("Input.Right")
		,home("Input.Home")
		,mute("Mute")
		,pause("Pause")
		,stop("Stop")
		,playNext("PlayNext")
		,playPrev("PlayPrev")
		,seek("VideoPlayer.SeekPercentage")
		;
		
		
		private String method;
		
		Command(String method) {
			this.method = method;
		}
		
		String getMethod() {
			return method;
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
			case seek:
				params = remoteModel.getRemoteModelParameters(boxeeRemoteDevice.getIdentifier(), command.name());
				request = boxeeRemoteDevice.getConnection().createJSONRPC2Request(command.getMethod());
				request.setParam("value", params.getIntParam("value"));
				break;
			default:
				request = boxeeRemoteDevice.getConnection().createJSONRPC2Request(command.getMethod());
				break;
		}
		return boxeeRemoteDevice.sendCommand(request);
	}
}
