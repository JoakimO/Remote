package se.newbie.remote.gui.standard;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.gui.RemoteImageButton;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.gui.RemoteSpinner;
import android.content.Context;

/**
 * 
 */
public class StandardRemoteGUIFactory implements RemoteGUIFactory{
	
	public StandardRemoteGUIFactory() {
		
	}
	
	public RemoteButton createButton(Context context, String text, String device, String command) {
		RemoteButton remoteButton = new StandardRemoteButton(context, text, device, command);
		
		return remoteButton;
	}
	
	public RemoteImageButton createImageButton(Context context, int resource, String device, String command) {
		RemoteImageButton remoteButton = new StandardRemoteImageButton(context, resource, device, command);
		return remoteButton;
	}	
	
	public RemoteSeekBar createSeekBar(Context context, String device, String command) {
		return new StandardRemoteSeekBar(context, device, command);
	}
	
	public RemoteSpinner createSpinner(Context context, String device, String command) {
		StandardRemoteSpinner spinner = new StandardRemoteSpinner(context, device, command);
		RemoteApplication.getInstance().getRemoteModel().addListener(spinner);
		spinner.update(RemoteApplication.getInstance().getRemoteModel());
		return spinner;
	}
}