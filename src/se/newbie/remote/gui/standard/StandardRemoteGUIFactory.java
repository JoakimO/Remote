package se.newbie.remote.gui.standard;

import se.newbie.remote.gui.AbstractRemoteGUIFactory;
import se.newbie.remote.gui.RemoteButton;
import android.content.Context;

/**
 * 
 */
public class StandardRemoteGUIFactory extends AbstractRemoteGUIFactory{
	
	public StandardRemoteGUIFactory(Context context) {
		super(context);
	}
	
	public RemoteButton createButton(String text) {
		return new StandardRemoteButton(getContext(), text);
	}
}