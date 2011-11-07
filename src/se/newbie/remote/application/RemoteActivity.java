package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.display.RemoteDisplay;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RemoteActivity extends Activity {
	
	private static final String TAG = "RemoteActivity";
	
	static private RemoteApplication remoteApplication; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.v(TAG, "Activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Context context = this.getApplicationContext();
                
        remoteApplication = new RemoteApplication(context);  
        
        remoteApplication.getRemoteDeviceFactory().create();
        
        RemoteGUIFactory remoteGUIFactory = remoteApplication.getRemoteModel().getRemoteGUIFactory();

        Log.v(TAG, "Create Test GUI");
        RemoteButton leftButton = remoteGUIFactory.createButton("Left");
        RemoteButton rightButton = remoteGUIFactory.createButton("Right");
        RemoteButton upButton = remoteGUIFactory.createButton("Up");
        RemoteButton downButton = remoteGUIFactory.createButton("Down");
        RemoteButton stopButton = remoteGUIFactory.createButton("Back");
        RemoteButton playButton = remoteGUIFactory.createButton("Select");
        
        leftButton.setId(1);
        leftButton.setCommand("left");
        leftButton.setDevice("Boxee-boxeebox");
        leftButton.addListener(remoteApplication.getRemoteView());
        
        rightButton.setId(2);
        rightButton.setCommand("right");
        rightButton.setDevice("Boxee-boxeebox");
        rightButton.addListener(remoteApplication.getRemoteView());
        
        upButton.setId(3);
        upButton.setCommand("up");
        upButton.setDevice("Boxee-boxeebox");
        upButton.addListener(remoteApplication.getRemoteView());
        
        downButton.setId(4);
        downButton.setCommand("down");
        downButton.setDevice("Boxee-boxeebox");         
        downButton.addListener(remoteApplication.getRemoteView());
        
        stopButton.setId(5);
        stopButton.setCommand("back");
        stopButton.setDevice("Boxee-boxeebox");        
        stopButton.addListener(remoteApplication.getRemoteView());
        
        playButton.setId(6);
        playButton.setCommand("select");
        playButton.setDevice("Boxee-boxeebox");        
        playButton.addListener(remoteApplication.getRemoteView());
        
        LinearLayout container = (LinearLayout)findViewById(R.id.panel);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, playButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        upButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        playButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, playButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        downButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, playButton.getId());
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, playButton.getId());
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(150, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, downButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        
        stopButton.setLayoutParams(params);
        
        
        
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layout.setLayoutParams(labelLayoutParams);
        
        container.addView(layout);
        layout.addView(upButton);
        layout.addView(playButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);
        layout.addView(stopButton);

        
        //RemoteDisplay remoteDisplay = remoteApplication.getRemoteDisplayFactory().getRemoteDisplay("currentlyPlaying", "Boxee-boxee-VirtualBox");
        
     
        
        
//        container.addView(upButton);
//        container.addView(leftButton);
//        container.addView(stopButton);
//        container.addView(playButton);
//        container.addView(rightButton); 
//        container.addView(downButton);
    }
    
    @Override
    public void onPause() {
    	Log.v(TAG, "onPause");
    	remoteApplication.getRemoteDeviceFactory().pause();
        super.onPause();
    }
    
    @Override
    public void onResume() {
    	Log.v(TAG, "onResume");
    	remoteApplication.getRemoteDeviceFactory().resume();
    	//TODO fragment is added again which causes a crash on tablet when switching from settings
        RemoteDisplay remoteDisplay = remoteApplication.getRemoteDisplayFactory().getRemoteDisplay("currentlyPlaying", "Boxee-boxeebox");
        if (remoteDisplay != null && remoteDisplay.getFragment() != null) {
        	FragmentManager fragmentManager = getFragmentManager();
        	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	fragmentTransaction.add(R.id.fragment_panel, remoteDisplay.getFragment());
        	fragmentTransaction.commit();
        }
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
    
    public static RemoteApplication getRemoteApplication() {
    	return remoteApplication;
    }
}