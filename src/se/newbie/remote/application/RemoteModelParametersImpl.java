package se.newbie.remote.application;

import java.util.HashMap;
import java.util.Map;

import se.newbie.remote.main.RemoteModelParameters;

public class RemoteModelParametersImpl implements RemoteModelParameters {
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public boolean containsParam(String key) {
		return parameters.containsKey(key);
	}
	
	public int getIntParam(String key) {
		try {
			return (Integer)parameters.get(key);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public float getFloatParam(String key) {
		try {
			return (Float)parameters.get(key);
		} catch (Exception e) {
			return 0F;
		}			
	}
	
	public String getStringParam(String key) {
		try {
			return (String)parameters.get(key);
		} catch (Exception e) {
			return null;
		}			
	}

	public Object getObjectParam(String key) {
		try {
			return (Object)parameters.get(key);
		} catch (Exception e) {
			return null;
		}					
	}	
	
	public void putIntParam(String key, Integer value) {
		parameters.put(key, value);
	}
	
	public void putFloatParam(String key, Float value) {
		parameters.put(key, value);
	}	
	
	public void putStringParam(String key, String value) {
		parameters.put(key, value);
	}	
	
	public void putObjectParam(String key, Object value) {
		parameters.put(key, value);
	}	
	
	/**
	 * TODO We will probably have to improve this. 
	 */
	public boolean isChanged(RemoteModelParameters parameters) {
		for (String key : this.parameters.keySet()) {
			if (!parameters.containsParam(key)) {
				return false;
			}			
			if (!this.parameters.get(key).equals(parameters.getObjectParam(key))) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		int i = 0;
		for (String key : this.parameters.keySet()) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("\"").append(key).append("\": ");
			sb.append(this.parameters.get(key).toString());
			i++;
		}
		sb.append("}");
		return sb.toString();
	}

}
