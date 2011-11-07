package se.newbie.remote.gui;

import android.content.Context;

/**
 * 
 */
public abstract class AbstractRemoteGUIFactory implements RemoteGUIFactory{
	
	private Context context;
	
	protected AbstractRemoteGUIFactory(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return this.context;
	}
}