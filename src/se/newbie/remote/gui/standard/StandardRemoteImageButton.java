package se.newbie.remote.gui.standard;

import se.newbie.remote.R;
import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.gui.RemoteImageButton;
import android.content.Context;
import android.view.View;

/**
 *
 */
public class StandardRemoteImageButton extends RemoteImageButton {

	public StandardRemoteImageButton(Context context, int resource, String device, String command) {
		super(context);
		setImageResource(resource);
		setCommand(command);
		setDevice(device);
		setBackgroundResource(R.drawable.standard_remote_button);

		
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