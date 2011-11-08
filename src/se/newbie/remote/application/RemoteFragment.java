package se.newbie.remote.application;

import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class RemoteFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	RemoteApplication remoteApplication = RemoteApplication.getInstance();
    	RemoteGUIFactory remoteGUIFactory = remoteApplication.getRemoteModel().getRemoteGUIFactory();
    	
    	RemoteButton leftButton = remoteGUIFactory.createButton("Left");
        RemoteButton rightButton = remoteGUIFactory.createButton("Right");
        RemoteButton upButton = remoteGUIFactory.createButton("Up");
        RemoteButton downButton = remoteGUIFactory.createButton("Down");
        RemoteButton stopButton = remoteGUIFactory.createButton("Back");
        RemoteButton playButton = remoteGUIFactory.createButton("Select");
        
        leftButton.setId(1);
        leftButton.setCommand("left");
        leftButton.setDevice("Boxee-boxeebox");
        leftButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
        rightButton.setId(2);
        rightButton.setCommand("right");
        rightButton.setDevice("Boxee-boxeebox");
        rightButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
        upButton.setId(3);
        upButton.setCommand("up");
        upButton.setDevice("Boxee-boxeebox");
        upButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
        downButton.setId(4);
        downButton.setCommand("down");
        downButton.setDevice("Boxee-boxeebox");         
        downButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
        stopButton.setId(5);
        stopButton.setCommand("back");
        stopButton.setDevice("Boxee-boxeebox");        
        stopButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
        playButton.setId(6);
        playButton.setCommand("select");
        playButton.setDevice("Boxee-boxeebox");        
        playButton.addListener(RemoteApplication.getInstance().getRemoteView());
        
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
        
        RelativeLayout layout = new RelativeLayout(this.getActivity().getApplicationContext());
        RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(labelLayoutParams);
        
        layout.addView(upButton);
        layout.addView(playButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);
        layout.addView(stopButton);
    	
    	return layout;
    }

}
