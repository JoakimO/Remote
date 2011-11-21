package se.newbie.remote.gui;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.display.RemoteDisplayFactory;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class RemoteDisplayPanel extends LinearLayout implements RemoteModelListener {
	private final static String TAG = "RemoteDisplayPanel";
	
	private String display;
	private String device;

	//private RemoteDisplay remoteDisplay;
	
	public RemoteDisplayPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
		initDisplay();
		RemoteModel model = RemoteApplication.getInstance().getRemoteModel();
		if (model != null ) {
			model.addListener(this);
		}
    }		
	
	//protected void onFinishInflate () {
	//	initDisplay();
	//}
	
	private RemoteDisplay getRemoteDisplay(String display, String device) {
		RemoteDisplay remoteDisplay = null;
		RemoteApplication application = RemoteApplication.getInstance();
		if (application != null) {
			RemoteDisplayFactory displayFactory = application.getRemoteDisplayFactory();
			if (displayFactory != null) {
				remoteDisplay = displayFactory.getRemoteDisplay(display, device);
			}
		}
		return remoteDisplay;
	}
	
    private final void initAttributes( AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		CharSequence s = a.getString(R.styleable.remote_display);
		display = s.toString();
		s = a.getString(R.styleable.remote_device);
		device = s.toString();		
    }	

	/**
	 * This will add the given display fragment to this panel, if it exists.
	 */
	private void initDisplay() {
		if (display != null && device != null) {
			final RemoteDisplay display = getRemoteDisplay(this.display, this.device);
			if (display != null) {
				final int id = this.getId();
				Log.v(TAG, "Display found :" + display + ";" + device);
				this.post(new Thread() {
					public void run() {
						Log.v(TAG, "Adding display " + display + " to " + id);
						Activity activity = RemoteApplication.getInstance().getActivity();
						FragmentManager fragmentManager = activity.getFragmentManager();
						Fragment currentFragment = fragmentManager.findFragmentById(id);
						if (currentFragment == null) {
							Log.v(TAG, "No fragment found creating new");
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							currentFragment = display.createFragment();	
							fragmentTransaction.add(id, currentFragment);
							fragmentTransaction.commit();
						}						
						fragmentManager.executePendingTransactions();
						display.setFragment(currentFragment);
					}
				});
			}	
		}
	}
	
	/**
	 * New remote device was found, check if the display have been added otherwise
	 * find out if this is a new device with the display we are looking for.
	 */
	public void update(RemoteModel remoteModel) {
		if (getRemoteDisplay(this.display, this.device) == null) {
			Log.v(TAG, "Try to create display for:" + display + ";" + device);
			initDisplay();
		}
	}	
}