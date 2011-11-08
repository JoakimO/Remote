package se.newbie.remote.gui;

import android.content.Context;

/**
 * 
 */
public interface RemoteGUIFactory {
	public RemoteButton createButton(Context context, String text);			
	
	public RemoteSeekBar createSeekBar(Context context);
}