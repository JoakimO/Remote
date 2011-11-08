package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.action.RemoteAction;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.main.RemoteModel;
import se.newbie.remote.main.RemoteView;
import se.newbie.remote.main.RemoteViewListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

public class RemoteViewImpl extends Fragment implements RemoteView {
	private static final String TAG = "RemoteViewImpl";
	int GESTURE_THRESHOLD_DP = 0;
	int GESTURE_THRESHOLD_VELOCITY = 0;

	private List<RemoteViewListener> listeners;
	private ViewFlipper viewFlipper;
	RemoteViewGestureDetector gestureDetector;
	private GestureDetector detector;
	private Handler handler;
	View rootView;
	
	private List<String> knownDevices = new ArrayList<String>();
	private List<String> registeredDevices = new ArrayList<String>();

	public RemoteViewImpl() {
		this.listeners = new ArrayList<RemoteViewListener>();
    	handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	updateFragment();
            }
        };			
	}
	
	public void addListener(RemoteViewListener listener) {
		this.listeners.add(listener);		
	}
	
	public void actionPerformed(RemoteAction action) {
		Log.v(TAG, "actionPerformed: " + action.getCommand() + ", " + action.getDevice() + " to " + listeners.size() + " listeners");
		for (RemoteViewListener listener : listeners) {
			listener.executeCommand(action.getCommand(), action.getDevice());
		}
	}

	/**
	 * Check if any new remote devices has been added.
	 */
	public void update(RemoteModel model) {
		Log.v(TAG, "Update");
		List<RemoteDevice> devices = model.getRemoteDevices();
		for (RemoteDevice device : devices) {
			if (!registeredDevices.contains(device.getIdentifier())) {
				registeredDevices.add(device.getIdentifier());
				handler.sendEmptyMessage(0);
			}
		}
	}	
	
	public Fragment getFragment() {
		return this;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		
    	GESTURE_THRESHOLD_DP = ViewConfiguration.get(this.getActivity().getApplicationContext()).getScaledTouchSlop();
    	GESTURE_THRESHOLD_VELOCITY = ViewConfiguration.get(this.getActivity().getApplicationContext()).getScaledMinimumFlingVelocity();
    	
		RemoteModel remoteModel = RemoteApplication.getInstance().getRemoteModel();
		//RemoteGUIFactory remoteGUIFactory = remoteModel.getRemoteGUIFactory();
		
		

		rootView = inflater.inflate(R.layout.standard_layout, container, false);
		viewFlipper = (ViewFlipper)rootView.findViewById(R.id.standard_view_flipper);
		if (viewFlipper != null) {
			gestureDetector = new RemoteViewGestureDetector(this.getActivity().getApplicationContext());
			detector = new GestureDetector(gestureDetector);
			rootView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View rootView, MotionEvent event) {
					detector.onTouchEvent(event);
				//	Log.v(TAG, "MotionEvent: " + event.getRawX() + "," + event.getRawY());
					return true;
				}
			}); 
		}
		return rootView;
	}
    
    private void updateFragment() {
    	Log.v(TAG, "updateFragment");
    	FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    	RemoteFragment remoteFragment = new RemoteFragment();
    	fragmentTransaction.add(R.id.standard_remote_layout, remoteFragment);    	
    	
    	RemoteDisplay remoteDisplay = RemoteApplication.getInstance().getRemoteDisplayFactory().getRemoteDisplay("currentlyPlaying", "Boxee-boxeebox");
    	fragmentTransaction.add(R.id.standard_display_layout, remoteDisplay.getFragment());
    	
    	remoteDisplay = RemoteApplication.getInstance().getRemoteDisplayFactory().getRemoteDisplay("browser", "Boxee-boxeebox");
    	fragmentTransaction.add(R.id.standard_browser_layout, remoteDisplay.getFragment());
    	
    	fragmentTransaction.commit();    	
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