package se.newbie.remote.gui;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.display.RemoteDisplayFactory;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class RemoteDisplayPanel extends LinearLayout implements RemoteModelEventListener {
	private final static String TAG = "RemoteDisplayPanel";
	
	private String display;
	private String device;

	//private RemoteDisplay remoteDisplay;
	protected RemoteDisplayPanel(int id, Context context, String device, String display) {
		super(context);
		this.setId(id);
		this.device = device;
		this.display = display;
		init();
	}
	
	public RemoteDisplayPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        init();
    }		
	
	protected void init() {
		if (!this.isInEditMode()) {
			initDisplay();
			RemoteModel model = RemoteApplication.getInstance().getRemoteModel();
			if (model != null ) {
				model.addListener(this);
			}
		}
	}
	
	
    private final void initAttributes( AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		display = a.getString(R.styleable.remote_display);
		device = a.getString(R.styleable.remote_device);
    }		
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.v(TAG, "onDetachedFromwindow");
		RemoteApplication.getInstance().getRemoteModel().removeListener(this);
	}		
	
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

	/**
	 * This will add the given display fragment to this panel, if it exists.
	 */
	private void initDisplay() {
		if (getDisplay() != null && getDevice() != null) {
			final RemoteDisplay display = getRemoteDisplay(this.getDisplay(), this.getDevice());
			if (display != null) {
				final int id = this.getId();
				Log.v(TAG, "Display found :" + display + ";" + getDevice());
				Log.v(TAG, "Removing display panel from model listening");
				RemoteApplication.getInstance().getRemoteModel().removeListener(this);				
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
	public void onRemoteModelEvent(RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.RemoteDevicesChanged) {
			if (getRemoteDisplay(this.getDisplay(), this.getDevice()) == null) {
				Log.v(TAG, "Try to create display for:" + getDisplay() + ";" + getDevice());
				initDisplay();
			}
		}
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}	
}