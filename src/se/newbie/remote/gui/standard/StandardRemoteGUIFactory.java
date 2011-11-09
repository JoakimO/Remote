package se.newbie.remote.gui.standard;

import se.newbie.remote.gui.AbstractRemoteGUIFactory;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteSeekBar;
import android.content.Context;

/**
 * 
 */
public class StandardRemoteGUIFactory extends AbstractRemoteGUIFactory{
	
	public StandardRemoteGUIFactory() {
		
	}
	
	public RemoteButton createButton(Context context, String text) {
		RemoteButton remoteButton = new StandardRemoteButton(context, text);
		
		return remoteButton;
	}
	
	public RemoteSeekBar createSeekBar(Context context) {
		return new StandardSeekBar(context);
	}
}