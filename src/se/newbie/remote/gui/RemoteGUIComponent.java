package se.newbie.remote.gui;

import se.newbie.remote.action.RemoteActionListener;

/**
 * Each GUI component must implement this interface and extend a Android View object. This
 * could be "android.view.Button" or the base class "android.view.View". For 3D graphics the
 * "android.view.SurfaceView" is extended.
 *
 * The RemoteButton simply extends the "android.view.Button" class for easily creating a button,
 * factories which want to create 3D buttons simple wrap a "android.view.SurfaceView".
 *
 * Android views have a number of different observable methods, the remote only observes with
 * ActionListeners and the the others can be used internally to featch Click events and then 
 * create a RemoteActionEvent for the remote.
 */
public interface RemoteGUIComponent {
	public void addListener(RemoteActionListener listener);
}