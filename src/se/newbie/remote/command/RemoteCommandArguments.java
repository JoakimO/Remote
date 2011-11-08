package se.newbie.remote.command;

import java.util.HashMap;
import java.util.Map;

public class RemoteCommandArguments {
	private Map<String, Object> arguments = new HashMap<String, Object>();
	
	public void setArgument(String key, Object value) {
		arguments.put(key, value);
	}
	
	public int getIntArgument(String key) {
		return (Integer)arguments.get(key);
	}
	
	public String getStringArgument(String key) {
		return (String)arguments.get(key);
	}	
	
	public Object getObjectArgument(String key) {
		return arguments.get(key);
	}	
}
