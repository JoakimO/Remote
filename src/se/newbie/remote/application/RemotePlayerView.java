package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
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
public class RemotePlayerView extends LinearLayout {
	private final static String TAG = "RemotePlayerView";
	private View view;
	
	private RemotePlayerState state;
	private boolean isPlaying = true;
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
		view = inflater.inflate(R.layout.default_remote_player_layout, this, false);
		view.setVisibility(View.INVISIBLE);
		this.addView(view);
		
		seekBar = (SeekBar)view.findViewById(R.id.remote_player_action_bar_seek);
		skipPrevious = (ImageButton)view.findViewById(R.id.remote_player_action_bar_skip_previous);
		skipNext = (ImageButton)view.findViewById(R.id.remote_player_action_bar_skip_next);
		play = (ImageButton)view.findViewById(R.id.remote_player_action_bar_play);
		pause = (ImageButton)view.findViewById(R.id.remote_player_action_bar_pause);
		stop = (ImageButton)view.findViewById(R.id.remote_player_action_bar_stop);
	}
	

	protected void tick() {
		this.post(new Runnable() {
			public void run() {
				handleProgressUpdate();
			}
		});
	}
	
	protected void update(RemotePlayerState state) {
		this.state = state;
		this.post(new Runnable() {
			public void run() {
				handleUpdate();
			}
		});
	}
	
	private void handleUpdate() {
		boolean visible = false;
		if (state != null) {
			if (state.isPlaying()) {
				if (state.isPaused()) {
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
}
