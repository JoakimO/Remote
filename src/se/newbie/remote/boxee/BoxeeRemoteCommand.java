package se.newbie.remote.boxee;

import java.util.HashSet;
import java.util.Set;

import se.newbie.remote.command.RemoteCommand;
import android.util.Log;
//http://192.168.0.101:8800/xbmcCmds/xbmcHttp?command=SendKey%28271%29
public class BoxeeRemoteCommand implements RemoteCommand {
	private static final String TAG = "BoxeeRemoteCommand";

	
	public enum Command {
		select("SendKey", "256")
		,back("SendKey", "275")
		,up("SendKey", "270")
		,down("SendKey", "271")
		,left("SendKey", "272")
		,right("SendKey", "273")
		,mute("Mute", "")
		,pause("Pause", "")
		,stop("Stop", "")
		,playNext("PlayNext","")
		,playPrev("PlayPrev","");
		
		
		private String method;
		private Set<String> arguments = new HashSet<String>();
		
		Command(String method, String ... arguments) {
			this.method = method;
			for (String argument : arguments) {
				this.arguments.add(argument);
			}
		}
		
		String getMethod() {
			return method;
		}
		
		Set<String> getArguments() {
			return arguments;
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
		return boxeeRemoteDevice.sendCommand(command);
	}
}
