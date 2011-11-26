package se.newbie.remote.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class RemotePlayerViewCreator extends LinearLayout implements
		RemoteModelEventListener, RemoteActivityListener {
	private final static String TAG = "RemotePlayerViewCreator";

	private Map<String, RemotePlayerView> remotePlayerViews = new HashMap<String, RemotePlayerView>();
	private Thread thread;

	public RemotePlayerViewCreator(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!this.isInEditMode()) {
			Log.v(TAG, "New RemotePlayerViewCreator created.");
			RemoteApplication application = RemoteApplication.getInstance();
			application.getActivity().addListener(this);

			RemoteModel model = application.getRemoteModel();
			update(model);

			model.addListener(this);
		}
	}
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.v(TAG, "onDetachedFromwindow");
		RemoteApplication.getInstance().getRemoteModel().removeListener(this);
		RemoteApplication.getInstance().getActivity().removeListener(this);
	}		

	public void onRemoteModelEvent(RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.PlayerStateChanged) {
			Log.v(TAG, "onRemoteModelEvent");
			RemoteModel model = event.getRemoteModel();
			update(model);
		}
	}

	private void update(RemoteModel model) {
		Set<String> states = model.getRemotePlayerStates();
		for (String identification : states) {
			final RemotePlayerState remotePlayerState = model
					.getRemotePlayerState(identification);
			RemotePlayerView remotePlayerView = getRemotePlayerView(remotePlayerState);
			if (remotePlayerView != null) {
				Log.v(TAG, "Updating player: " + remotePlayerState.toString());
				remotePlayerView.update(remotePlayerState);
			} else {
				Log.v(TAG,
						"Creating new player view: "
								+ remotePlayerState.toString());
				this.post(new Runnable() {
					public void run() {
						final RemotePlayerView newRemotePlayerView = new RemotePlayerView(
								RemoteApplication.getInstance().getContext());
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.FILL_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						newRemotePlayerView.setLayoutParams(params);
						remotePlayerViews.put(
								remotePlayerState.getIdentification(),
								newRemotePlayerView);

						addView(newRemotePlayerView);
						newRemotePlayerView.update(remotePlayerState);
						startTickThread();
					}
				});
			}
		}
		for (String identification : remotePlayerViews.keySet()) {
			RemotePlayerView remotePlayerView = remotePlayerViews
					.get(identification);
			if (!states.contains(identification)) {
				Log.v(TAG,
						"Removing player view since the player have been removed");
				removeRemotePlayerView(remotePlayerView);
			}
		}
	}

	private void removeRemotePlayerView(final RemotePlayerView view) {
		this.post(new Runnable() {
			public void run() {
				remotePlayerViews.remove(view.getRemotePlayerState()
						.getIdentification());
				removeView(view);
			}
		});
	}

	private RemotePlayerView getRemotePlayerView(final RemotePlayerState state) {
		RemotePlayerView remotePlayerView = null;
		if (remotePlayerViews.containsKey(state.getIdentification())) {
			remotePlayerView = remotePlayerViews.get(state.getIdentification());
		}
		return remotePlayerView;
	}

	public void resume() {
		Log.v(TAG, "Resume");
		startTickThread();
	}

	public void pause() {
		Log.v(TAG, "Pause");
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
	}

	protected void tick() {
		for (int i = 0; i < getChildCount(); i++) {
			RemotePlayerView view = (RemotePlayerView) this.getChildAt(i);
			view.tick();
		}
	}

	protected boolean isActive() {
		return this.remotePlayerViews.size() > 0;
	}

	private void startTickThread() {
		if (thread == null || (thread != null && !thread.isAlive())) {
			thread = new RemotePlayerViewCreatorThread();
			thread.start();
		}
	}

	private class RemotePlayerViewCreatorThread extends Thread {

		public void run() {
			try {
				while (isActive()) {
					Thread.sleep(1000);
					tick();
				}
			} catch (Exception e) {
				Log.e(TAG,
						"Thread error or thread was interrupted: "
								+ e.getMessage());
			}
		}
	}

}
