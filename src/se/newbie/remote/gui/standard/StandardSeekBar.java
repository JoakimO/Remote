package se.newbie.remote.gui.standard;

import se.newbie.remote.action.SeekRemoteAction;
import se.newbie.remote.gui.RemoteSeekBar;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class StandardSeekBar extends RemoteSeekBar implements OnSeekBarChangeListener {

	public StandardSeekBar(Context context) {
		super(context);
		this.setMax(100);
		this.setOnSeekBarChangeListener(this);
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		seekPerformed(seekBar.getProgress());
	}
	
	private void seekPerformed(int value) {
		SeekRemoteAction action = new SeekRemoteAction(getCommand(), getDevice(), value);
		actionPerformed(action);
	}	

}
