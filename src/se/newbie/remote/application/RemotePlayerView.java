package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * This is the remote player for devices with API 11+
 * 
 * It is used to display the current playing progress with stop, play, pause and skip
 * buttons.
 */
public class RemotePlayerView extends LinearLayout implements RemoteModelListener {
	private final static String TAG = "RemotePlayerView";
	private View view;
	
	private Handler handler;
	private RemotePlayerState state;
	
	private boolean isPlaying = true;
	
	private RemotePlayerViewThread thread;
	private SeekBar		seekBar;
	private ImageButton skipPrevious;
	private ImageButton skipNext;
	private ImageButton play;
	private ImageButton pause;
	private ImageButton stop;
	
	
	public RemotePlayerView(Context context) {
		super(context);
		Log.v(TAG, "Remote Player Action Bar Initializing...");
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.remote_player_action_bar_layout, this, false);
		view.setVisibility(View.INVISIBLE);
		this.addView(view);
		
		seekBar = (SeekBar)view.findViewById(R.id.remote_player_action_bar_seek);
		skipPrevious = (ImageButton)view.findViewById(R.id.remote_player_action_bar_skip_previous);
		skipNext = (ImageButton)view.findViewById(R.id.remote_player_action_bar_skip_next);
		play = (ImageButton)view.findViewById(R.id.remote_player_action_bar_play);
		pause = (ImageButton)view.findViewById(R.id.remote_player_action_bar_pause);
		stop = (ImageButton)view.findViewById(R.id.remote_player_action_bar_stop);
		
		
    	handler = new Handler() { 
            @Override
            public void handleMessage(Message msg) {
            	switch (msg.what) {
            		case 0:
            			handleUpdate();
            			break;
            		case 1:
            			handleProgressUpdate();
            			break;
            	}
            }
        };		
	}
	
	public void pause() {
		isPlaying = false;
	}
	
	public void resume() {
	}
	
	private void handleUpdate() {
		boolean visible = false;
		if (state != null) {
			if (state.isPlaying()) {
				Log.v(TAG, "isPlaying");
				if (state.isPaused()) {
					Log.v(TAG, "isPaused");
					play.setVisibility(View.VISIBLE);
					pause.setVisibility(View.INVISIBLE);
				} else {
					play.setVisibility(View.INVISIBLE);
					pause.setVisibility(View.VISIBLE);
				}
				play.invalidate();
				pause.invalidate();
				visible = true;
				isPlaying = true;
				startTimerThread();
			}
		}		
		if (visible) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.INVISIBLE);
			isPlaying = false;			
		}
	}
	
	private void handleProgressUpdate() {
		int progress = 0;
		if (state != null) {
			if (state.isPaused()) {
				float f = (float)state.getTime() / (float)state.getDuration();
				progress = (int)(f * 100F);					
			} else {
				float f = (float)(System.currentTimeMillis() - state.getStateTime() + state.getTime()) / (float)state.getDuration();
				progress = (int)(f * 100F);
			}
		}
		seekBar.setProgress(progress);
		seekBar.invalidate();
	}
	
	private void startTimerThread() {
		if (thread == null || (thread != null && !thread.isAlive())) {
			thread = new RemotePlayerViewThread();
			thread.start();
		}
	}

	public void update(RemoteModel model) {
		
		RemoteDevice device = model.getSelectedRemoteDevice();
		if (device != null) {
			state = model.getRemotePlayerState(device.getIdentifier());
		}
		handler.sendEmptyMessage(0);
	}

	
	private class RemotePlayerViewThread extends Thread {
		
		public void run() {
			try {
				while (isPlaying) {
					Thread.sleep(1000);
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				Log.e(TAG, "Thread was interrupted");
			}
		}
	}
}
