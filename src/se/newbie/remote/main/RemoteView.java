package se.newbie.remote.main;

import se.newbie.remote.action.RemoteActionListener;
import android.app.Activity;
import android.view.View;


public interface RemoteView extends RemoteModelListener, RemoteActionListener {
	public void addListener(RemoteViewListener listener);
	
	public View createLayout(Activity activity);
}