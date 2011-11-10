package se.newbie.remote.gui;

import android.content.Context;

/**
 * 
 */
public interface RemoteGUIFactory {
	public RemoteImageButton createImageButton(Context context, int resource, String device, String command);
	
	public RemoteButton createButton(Context context, String text, String device, String command);			
	
	public RemoteSeekBar createSeekBar(Context context, String device, String command);
	
	public RemoteSpinner createSpinner(Context context, String device, String command);
}