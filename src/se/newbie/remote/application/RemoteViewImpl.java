package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.action.RemoteAction;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteModelEvent;
import se.newbie.remote.main.RemoteModelEvent.RemoteModelEventType;
import se.newbie.remote.main.RemoteView;
import se.newbie.remote.main.RemoteViewListener;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ViewFlipper;

public class RemoteViewImpl implements RemoteView {
	private static final String TAG = "RemoteViewImpl";
	
	int GESTURE_THRESHOLD_DP = 0;
	int GESTURE_THRESHOLD_VELOCITY = 0;

	private List<RemoteViewListener> listeners = new ArrayList<RemoteViewListener>();;
	private ViewFlipper viewFlipper;
	RemoteViewGestureDetector gestureDetector;
	private GestureDetector detector;

	private List<String> registeredDevices = new ArrayList<String>();

	public RemoteViewImpl() {
    	GESTURE_THRESHOLD_DP = ViewConfiguration.get(RemoteApplication.getInstance().getContext()).getScaledTouchSlop();
    	GESTURE_THRESHOLD_VELOCITY = ViewConfiguration.get(RemoteApplication.getInstance().getContext()).getScaledMinimumFlingVelocity();		
	}
	
	public void addListener(RemoteViewListener listener) {
		this.listeners.add(listener);		
	}
	
	public void actionPerformed(RemoteAction action) {
		Log.v(TAG, "actionPerformed: " + action.getCommand() + ", " + action.getDevice() + " to " + listeners.size() + " listeners");
		for (RemoteViewListener listener : listeners) {
			switch (action.getRemoteActionType()) {
				case Click:
					listener.executeCommand(action.getCommand(), action.getDevice());
					break;
				case Seek:
					listener.executeCommand(action.getCommand(), action.getDevice());
					break;
				default :
					break;
			}
		}
	}

	/**
	 * Check if any new remote devices has been added.
	 */
	public void onRemoteModelEvent(RemoteModelEvent event) {
		if (event.getEventType() == RemoteModelEventType.RemoteDevicesChanged) {
			Log.v(TAG, "onRemoteModelEvent");
			RemoteModel model = event.getRemoteModel();
			List<RemoteDevice> devices = model.getRemoteDevices();
			for (RemoteDevice device : devices) {
				if (!registeredDevices.contains(device.getIdentifier())) {
					registeredDevices.add(device.getIdentifier());
				}
			}
		}
	}	
	
	public View createLayout(Activity activity) {
    	Log.v(TAG, "Create remote view layout");
    	LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.default_layout, null, false);
		
		viewFlipper = (ViewFlipper)view.findViewById(R.id.standard_view_flipper);
		if (viewFlipper != null) {
			gestureDetector = new RemoteViewGestureDetector(RemoteApplication.getInstance().getContext());
			detector = new GestureDetector(gestureDetector);
			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View rootView, MotionEvent event) {
					detector.onTouchEvent(event);
					return true;
				}
			});  
		}	
		return view;
	}
	
    class RemoteViewGestureDetector extends SimpleOnGestureListener {
		Context context;
		
		protected RemoteViewGestureDetector(Context context) {
			this.context = context;
		}

		@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			if (viewFlipper != null) {
	    		if(e1.getX() - e2.getX() > GESTURE_THRESHOLD_DP && Math.abs(velocityX) > GESTURE_THRESHOLD_VELOCITY) {
	    			viewFlipper.setInAnimation(context, R.anim.slide_in_right);
	    			viewFlipper.setOutAnimation(context, R.anim.slide_out_left);
	    			viewFlipper.showNext();
	    		} else if (e2.getX() - e1.getX() > GESTURE_THRESHOLD_DP && Math.abs(velocityX) > GESTURE_THRESHOLD_VELOCITY) {
	    			viewFlipper.setInAnimation(context, R.anim.slide_in_left);
	    			viewFlipper.setOutAnimation(context, R.anim.slide_out_right);	    			
	    			viewFlipper.showPrevious();
	    		}
			}
    		return true;
    	}
    }    
}