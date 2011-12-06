package se.newbie.remote.gui;

import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteModelEventListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class RemoteDisplayPanelCreator extends LinearLayout implements RemoteModelEventListener {
	private final static String TAG = "RemoteDisplayPanelCreator";
	
	private int target;
	private String display;
	
	//TODO look at creating unique id's
	private static int displayPanelId = 0x5f000000;

	public RemoteDisplayPanelCreator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
		if (!this.isInEditMode()) {
			RemoteModel model = RemoteApplication.getInstance().getRemoteModel();
			
			if (model != null ) {
				model.addListener(this);
			}
		}
    }		
	
	protected void onFinishInflate () {
		Log.v(TAG, "onFinishInflate");
		RemoteModel model = RemoteApplication.getInstance().getRemoteModel();
		initDisplayPanels(model);
	}	
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.v(TAG, "onDetachedFromwindow");
		RemoteApplication.getInstance().getRemoteModel().removeListener(this);
	}		
	
    private final void initAttributes( AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.remote);
		target = a.getResourceId(R.styleable.remote_target, -1);
		display = a.getString(R.styleable.remote_display);
    }	

	public void onRemoteModelEvent(RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.RemoteDevicesChanged) {
			initDisplayPanels(event.getRemoteModel());
		}
	}	
	
	private void initDisplayPanels(RemoteModel model) {
		Log.v(TAG, "Init display panels on " + target);
		final List<RemoteDevice> devices = model.getRemoteDevices();
		for (final RemoteDevice device : devices) {
			if (!containsDisplayPanel(target, device.getIdentifier(), display)) {
				this.post(new Runnable(){
					public void run() {
						ViewGroup viewGroup = (ViewGroup)findViewById(target);
						Log.v(TAG, "Add display panel: " + device.getIdentifier() + ";" + display + " to " + target);
						RemoteDisplayPanel remoteDisplayPanel = new RemoteDisplayPanel(getNextDisplayId(), getContext(), device.getIdentifier(), display);
						//remoteDisplayPanel.setId(displayPanelId++);
						viewGroup.addView(remoteDisplayPanel);
					}
				});
			}
		}
	}
	
	private synchronized int getNextDisplayId() {
		return displayPanelId++;
	}
	
	/**
	 * Iterates all remote display panel child views of target view and
	 * compares the given device and display for match.  
	 */
	private boolean containsDisplayPanel(int resource, String device, String display) {
		ViewGroup viewGroup = (ViewGroup)this.findViewById(resource);
		for (int i = 0; i < viewGroup.getChildCount(); i ++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof RemoteDisplayPanel) {
				RemoteDisplayPanel panel = (RemoteDisplayPanel)view;
				if (display.equals(panel.getDisplay()) && device.equals(panel.getDevice())) {
					return true;
				}
			}
		}		
		return false;
	}
}