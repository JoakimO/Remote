package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.main.RemoteView;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Activity start");

        //setContentView(R.layout.main);        

        Context context = this.getApplicationContext();
        
        RemoteApplication.getInstance().init(context);
        RemoteApplication.getInstance().getRemoteDeviceFactory().create();
        
        remoteView = RemoteApplication.getInstance().getRemoteView();
        
        setContentView(remoteView.createLayout(this));
        
        
        
    	/*FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	Fragment fragment = remoteView.createFragment(); 
    	fragmentTransaction.add(R.id.main_panel, fragment);    	
    	fragmentTransaction.commit();
    	
    	fragmentManager.executePendingTransactions();
    	
    	remoteView.setFragment(fragment);*/    	
    }
    
    @Override
    public void onPause() {
    	Log.v(TAG, "onPause");
    	RemoteApplication.getInstance().pause();
        super.onPause();
    }
    
    
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	
	
    	
    	super.onResume();
    	remoteView.initializeFragments(this);
    	RemoteApplication.getInstance().resume();
    }	    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remote_menu, menu); 

        player = new RemotePlayerView(this);
        ActionBar actionBar = getActionBar();
        
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(player, params);
 
        RemoteApplication.getInstance().setRemotePlayerView(player);
        RemoteApplication.getInstance().getRemoteModel().addListener(player);    
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