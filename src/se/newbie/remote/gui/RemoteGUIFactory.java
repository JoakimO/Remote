package se.newbie.remote.gui;

import android.content.Context;

/**
 * 
 */
public interface RemoteGUIFactory {
	
	/**
	 * Holds the application context object.
	 */
	public Context getContext();

	public RemoteButton createButton(String text);			
}