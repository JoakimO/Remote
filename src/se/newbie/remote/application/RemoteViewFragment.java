package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.display.RemoteDisplay;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RemoteViewFragment extends Fragment {
	public static final String TAG = "RemoteViewFragment";

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	updateFragment();
        }
    };			

	
	public RemoteViewFragment() {
	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	Log.v(TAG, "On create view");
		View view = inflater.inflate(R.layout.standard_layout, container, false);
		return view;
	}
    
    private void initializeSubFragments() {
    	Log.v(TAG, "Initializing sub fragments...");
    	
    	
    	
    	FragmentManager fragmentManager = getFragmentManager();

    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    	RemoteDisplay displayRemoteDisplay = RemoteApplication.getInstance().getRemoteDisplayFactory().getRemoteDisplay("currentlyPlaying", "Boxee-boxeebox");
    	RemoteDisplay browserRemoteDisplay = RemoteApplication.getInstance().getRemoteDisplayFactory().getRemoteDisplay("browser", "Boxee-boxeebox");
    	if (displayRemoteDisplay != null && browserRemoteDisplay != null) {
	    	RemoteFragment remoteFragment = new RemoteFragment();
	    	fragmentTransaction.add(R.id.standard_remote_layout, remoteFragment); 
	    	RemoteApplication.getInstance().getRemoteModel().addListener(remoteFragment);
	    	Fragment displayFragment = displayRemoteDisplay.createFragment();
	    	Fragment browserFragment = browserRemoteDisplay.createFragment();
	    	fragmentTransaction.add(R.id.standard_display_layout, displayFragment);
	    	fragmentTransaction.add(R.id.standard_browser_layout, browserFragment);
	    	fragmentTransaction.commit();   
	    	
	    	fragmentManager.executePendingTransactions();
	    	
	    	displayRemoteDisplay.setFragment(displayFragment);
	    	browserRemoteDisplay.setFragment(browserFragment);
    	}
    }
    
    public void update() {
    	Log.v(TAG, "updateFragment");
    	handler.sendEmptyMessage(0);
    }    
    
    private void updateFragment() {
    	initializeSubFragments();
    }
	
	
}
