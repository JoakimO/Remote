package se.newbie.remote.application;

import java.util.ArrayList;
import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.main.RemoteView;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * This is the main activity for the application.
 * 
 * @author joakim
 *
 */
public class RemoteActivity extends Activity {
	private static final String TAG = "RemoteActivity";
	RemoteView remoteView;
	RemotePlayerView player;
	
	private List<RemoteActivityListener> listeners = new ArrayList<RemoteActivityListener>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Activity start");
        this.setTheme(R.style.DefaultTheme);
        
        RemoteApplication.getInstance().init(this);
        RemoteApplication.getInstance().getRemoteDeviceFactory().create();
        addListener(RemoteApplication.getInstance());
        
        remoteView = RemoteApplication.getInstance().getRemoteView();
        
        setContentView(remoteView.createLayout(this));
    }
    
    
    public void addListener(RemoteActivityListener listener) {
    	listeners.add(listener);
    }
    
    @Override
    public void onPause() {
    	Log.v(TAG, "onPause");
    	for (RemoteActivityListener listener : listeners) {
    		listener.pause();
    	}
    	//RemoteApplication.getInstance().pause();
        super.onPause();
    }
    
    
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	super.onResume();

    	for (RemoteActivityListener listener : listeners) {
    		listener.resume();
    	}    	
    	
    	//RemoteApplication.getInstance().resume();
    	
    	/*Log.v(TAG, "Check if there was a fragment dialog in progress");
    	Fragment inProgressFragmentDialog = getFragmentManager().findFragmentByTag("remote_dialog");
    	if (inProgressFragmentDialog != null) {
    		Log.v(TAG, "Fragment dialog found and will be shown again");
    		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    		fragmentTransaction.show(inProgressFragmentDialog);
    	}*/    	
    }
    
    public void showDialog(DialogFragment dialog) {
    	FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
    	Fragment prev = getFragmentManager().findFragmentByTag("remote_dialog");
    	
    	if (prev != null) {
    		fragmentTransaction.remove(prev);
    	}
    	fragmentTransaction.addToBackStack(null);

   		dialog.show(fragmentTransaction, "remote_dialog");
    }    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remote_menu, menu); 

        /*player = new RemotePlayerView(this);
        ActionBar actionBar = getActionBar();
        
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setCustomView(player, params);

        RemoteApplication.getInstance().getRemoteModel().addListener(player);*/    
        return super.onCreateOptionsMenu(menu);
    }    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) 
    	{
    	    case R.id.settings:
    	        Intent i = new Intent(this, RemotePreferenceActivity.class);
    	        startActivity(i);
    	        break;
    	    case android.R.id.home:
    	    	//Clicked home icon
    	    	break;
    	    default:

    	    break;
    	}
    	return false;
    }     
}