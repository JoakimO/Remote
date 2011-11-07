package se.newbie.remote.gui.standard;

import android.content.Context;
import android.view.View;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.gui.RemoteButton;

/**
 *
 */
public class StandardRemoteButton extends RemoteButton {

	public StandardRemoteButton(Context context, String text) {
		super(context);
		setText(text);
		
        setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 clickPerformed();
             }
         });		
	}
	
	private void clickPerformed() {
		ClickRemoteAction action = new ClickRemoteAction(getCommand(), getDevice());
		actionPerformed(action);
	}
}