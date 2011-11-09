package se.newbie.remote.gui.standard;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.gui.RemoteButton;
import android.content.Context;
import android.view.View;

/**
 *
 */
public class StandardRemoteButton extends RemoteButton {

	public StandardRemoteButton(Context context, String text) {
		super(context);
		setBackgroundResource(R.drawable.standard_remote_button);
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