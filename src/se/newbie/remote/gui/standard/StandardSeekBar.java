package se.newbie.remote.gui.standard;

import se.newbie.remote.action.ClickRemoteAction;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.main.RemoteModel;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class StandardSeekBar extends RemoteSeekBar implements OnSeekBarChangeListener   {

	public StandardSeekBar(Context context) {
		super(context);
		
		
		//this.setBackgroundResource(R.drawable.standard_seek_bar);
		//this.setSecondaryProgress(R.drawable.standard_seek_bar);
		
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
		RemoteApplication.getInstance().getRemoteModel().setDeviceParameter(getDevice(), getCommand(), new Integer(value));
		ClickRemoteAction action = new ClickRemoteAction(getCommand(), getDevice());
		actionPerformed(action);
	}

	public void update(RemoteModel model) {
		//TODO!!!
		//model.getIntDeviceParameter(getDevice(), getCommand());
		
	}	

}
