package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.main.RemoteView;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RemoteActivity extends Activity {
	private static final String TAG = "RemoteActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Activity start");
        
        setContentView(R.layout.main);
        Context context = this.getApplicationContext();
        RemoteApplication.getInstance().init(context);
        RemoteApplication.getInstance().getRemoteDeviceFactory().create();
        
        RemoteView remoteView = RemoteApplication.getInstance().getRemoteView();
    	FragmentManager fragmentManager = getFragmentManager();
    	
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	fragmentTransaction.add(R.id.main_panel, remoteView.getFragment());
    	
    	fragmentTransaction.commit();
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
    	RemoteApplication.getInstance().resume();
    	super.onResume();
    }	    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remote_menu, menu);
        return true;
    }    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) 
    	{
    	    case R.id.settings:
    	        Intent i = new Intent(this, RemotePreferenceActivity.class);
    	        startActivity(i);
    	    break;
    	    default:

    	    break;
    	}
    	return false;
    }     
}