package se.newbie.remote.main;

import se.newbie.remote.action.RemoteActionListener;
import android.app.Fragment;


public interface RemoteView extends RemoteModelListener, RemoteActionListener {
	public void addListener(RemoteViewListener listener);
	
	public Fragment getFragment();
}