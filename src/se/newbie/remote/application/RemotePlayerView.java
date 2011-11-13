package se.newbie.remote.application;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * This is the remote player for devices with API 11+
 * 
 * It is used to display the current playing progress with stop, play, pause and skip
 * buttons.
 */
public class RemotePlayerView extends LinearLayout {
	private final static String TAG = "RemotePlayerView";

	public RemotePlayerView(Context context) {
		super(context);
		Log.v(TAG, "Remote Player Action Bar Initializing...");
	}
}
