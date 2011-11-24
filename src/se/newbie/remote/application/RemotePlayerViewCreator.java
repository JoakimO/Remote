package se.newbie.remote.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import se.newbie.remote.main.RemotePlayerState;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class RemotePlayerViewCreator extends LinearLayout implements RemoteModelListener, RemoteActivityListener {
	private final static String TAG = "RemotePlayerViewCreator";
	
	private Map<String, RemotePlayerView> remotePlayerViews = new HashMap<String, RemotePlayerView>();
	private Thread thread;
	
	public RemotePlayerViewCreator(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!this.isInEditMode()) {
			RemoteApplication application = RemoteApplication.getInstance();
			application.getActivity().addListener(this);
			
			RemoteModel model = application.getRemoteModel();
			update(model);
	
			model.addListener(this);
		}
	}

	public void update(RemoteModel model) {
		Log.v(TAG, "Model update");
		
		Set<String> states = model.getRemotePlayerStates();
		for (String identification : states) {
			final RemotePlayerState remotePlayerState = model.getRemotePlayerState(identification);
			RemotePlayerView remotePlayerView = getRemotePlayerView(remotePlayerState);
			if (remotePlayerView != null) {
				Log.v(TAG, "Updating player");
				remotePlayerView.update(remotePlayerState);
			} else {
				Log.v(TAG, "Creating new player view");
				this.post(new Runnable() {
					public void run() {			
						final RemotePlayerView newRemotePlayerView = new RemotePlayerView(RemoteApplication.getInstance().getContext());
						remotePlayerViews.put(remotePlayerState.getIdentification(), newRemotePlayerView);
						addView(newRemotePlayerView);
						newRemotePlayerView.update(remotePlayerState);
						startTickThread();
					}
				});				
			}
		}
		for (String identification : remotePlayerViews.keySet()) {
			if (!states.contains(identification)) {
				RemotePlayerView remotePlayerView = remotePlayerViews.get(identification);
				this.removeView(remotePlayerView);
			}
		}
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
	}

	public void pause() {
		Log.v(TAG, "Pause");
		
	}
	
	protected void tick() {
		for (int i = 0; i < getChildCount(); i++) {
			RemotePlayerView view = (RemotePlayerView)this.getChildAt(i);
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
					Thread.sleep(2000);
					tick();
				}
			} catch (Exception e) {
				Log.e(TAG, "Thread was interrupted");
			}
		}
	}	

}
