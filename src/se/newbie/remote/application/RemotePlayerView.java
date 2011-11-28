package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.gui.RemoteImageButton;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This is the remote player for devices with API 11+
 * 
 * It is used to display the current playing progress with stop, play, pause and
 * skip buttons.
 */
public class RemotePlayerView extends LinearLayout {
	private final static String TAG = "RemotePlayerView";
	private View view;

	private RemotePlayerState state;
	private RemoteSeekBar seekBar;
	private RemoteImageButton skipPrevious;
	private RemoteImageButton skipNext;
	private RemoteImageButton play;
	private RemoteImageButton pause;
	private RemoteImageButton stop;
	private TextView time;
	private TextView duration;
	private TextView device;
	private TextView label;

	public RemotePlayerView(Context context) {
		super(context);

		//Theme theme = context.getTheme();
		//Log.v(TAG, "Theme: " + theme.toString());
		Log.v(TAG, "Remote Player View Initializing...");

		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.default_remote_player_layout, this,
				false);
		this.addView(view);

		seekBar = (RemoteSeekBar) view
				.findViewById(R.id.remote_player_action_bar_seek);
		skipPrevious = (RemoteImageButton) view
				.findViewById(R.id.remote_player_action_bar_skip_previous);
		skipNext = (RemoteImageButton) view
				.findViewById(R.id.remote_player_action_bar_skip_next);
		play = (RemoteImageButton) view
				.findViewById(R.id.remote_player_action_bar_play);
		pause = (RemoteImageButton) view
				.findViewById(R.id.remote_player_action_bar_pause);
		stop = (RemoteImageButton) view
				.findViewById(R.id.remote_player_action_bar_stop);
		time = (TextView) view.findViewById(R.id.remote_player_time_text);
		duration = (TextView) view.findViewById(R.id.remote_player_duration_text);
		label = (TextView) view.findViewById(R.id.remote_player_label_text);
		device = (TextView) view.findViewById(R.id.remote_player_device_text);
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

	protected RemotePlayerState getRemotePlayerState() {
		return state;
	}

	private void handleUpdate() {
		Log.v(TAG, "Handle update: " + state.toString());
		if (state != null) {

			seekBar.setDevice(state.getDevice());
			seekBar.setCommand(state.getSeekCommand());
			seekBar.setVisibility(state.isSeekable() ? View.VISIBLE
					: View.INVISIBLE);
			skipPrevious.setDevice(state.getDevice());
			skipPrevious.setCommand(state.getPreviousCommand());
			skipPrevious.setVisibility(state.isNextAvailable() ? View.VISIBLE
					: View.INVISIBLE);
			skipNext.setDevice(state.getDevice());
			skipNext.setCommand(state.getNextCommand());
			skipNext.setVisibility(state.isNextAvailable() ? View.VISIBLE
					: View.INVISIBLE);
			play.setDevice(state.getDevice());
			play.setCommand(state.getPlayCommand());
			pause.setDevice(state.getDevice());
			pause.setCommand(state.getPauseCommand());
			stop.setDevice(state.getDevice());
			stop.setCommand(state.getStopCommand());
			
			duration.setText(formatTime(state.getDuration()));
			device.setText(state.getDevice());
			label.setText(state.getLabel());

			if (state.isPlaying()) {
				if (state.isPaused()) {
					play.setVisibility(View.VISIBLE);
					pause.setVisibility(View.INVISIBLE);
				} else {
					play.setVisibility(View.INVISIBLE);
					pause.setVisibility(View.VISIBLE);
				}
			}
			this.invalidate();
		}
	}

	private void handleProgressUpdate() {
		int progress = 0;
		if (state != null) {
			if (state.isPaused()) {
				float f = (float) state.getTime() / (float) state.getDuration();
				progress = (int) (f * 100F);
				
				time.setText(formatTime(state.getTime()));		
			} else {
				float f = (float) (System.currentTimeMillis()
						- state.getStateTime() + state.getTime())
						/ (float) state.getDuration();
				progress = (int) (f * 100F);
				
				time.setText(formatTime(System.currentTimeMillis()
						- state.getStateTime() + state.getTime()));		
			}
		}
		
		
		
		seekBar.setProgress(progress);
		seekBar.invalidate();
		time.invalidate();
	}
	
	private String formatTime(long time) {
		
		int hours   = (int)(time / 60 / 60 / 1000);
		int minutes = (int)(time / 60 / 1000) % 60;
		int seconds = (int)(time / 1000) % 60;
		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(hours).append(":").append(formatUnit(minutes)).append(":").append(formatUnit(seconds));
		} else {
			sb.append(formatUnit(minutes)).append(":").append(formatUnit(seconds));
		}
	
		
		return sb.toString();
	}
	
	private String formatUnit(int value) {
		if (value > 9) {
			return ("" + value);
		} else if (value > -1) {
			return "0" + value;
		} else {
			return "00";
		}
	}
}
