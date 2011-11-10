package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.gui.RemoteSpinner;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class RemoteFragment extends Fragment {
	private final static String TAG = "RemoteFragment";
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	
    	RemoteApplication remoteApplication = RemoteApplication.getInstance();
    	RemoteGUIFactory remoteGUIFactory = remoteApplication.getRemoteModel().getRemoteGUIFactory();
    	
    	RemoteButton leftButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Left", "Boxee-boxeebox", "left");
        RemoteButton rightButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Right", "Boxee-boxeebox", "right");
        RemoteButton upButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Up", "Boxee-boxeebox", "up");
        RemoteButton downButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Down", "Boxee-boxeebox", "down");
        RemoteButton stopButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Back", "Boxee-boxeebox", "back");
        RemoteButton playButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Select", "Boxee-boxeebox", "select");
        RemoteSeekBar seekBar = remoteGUIFactory.createSeekBar(getActivity().getApplicationContext(), "Boxee-boxeebox", "seek");
        RemoteSpinner spinner = remoteGUIFactory.createSpinner(getActivity().getApplicationContext(), "Predefined", "selectRemoteDevice");
        
        leftButton.setId(1);
        leftButton.addListener(RemoteApplication.getInstance().getRemoteView());
        rightButton.setId(2);
        rightButton.addListener(RemoteApplication.getInstance().getRemoteView());
        upButton.setId(3);
        upButton.addListener(RemoteApplication.getInstance().getRemoteView());
        downButton.setId(4);
        downButton.addListener(RemoteApplication.getInstance().getRemoteView());
        stopButton.setId(5);
        stopButton.addListener(RemoteApplication.getInstance().getRemoteView());
        playButton.setId(6);
        playButton.addListener(RemoteApplication.getInstance().getRemoteView());
        seekBar.setId(7);
        seekBar.addListener(RemoteApplication.getInstance().getRemoteView());
        spinner.setId(8);
        spinner.addListener(RemoteApplication.getInstance().getRemoteView());        
        
        
        Resources res = getResources();
        
        int selectorWidth = (int)res.getDimension(R.dimen.standard_device_selector_width);
        int selectorHeight = (int)res.getDimension(R.dimen.standard_device_selector_height);        
        int buttonWidth = (int)res.getDimension(R.dimen.standard_button_width);
        int buttonHeight = (int)res.getDimension(R.dimen.standard_button_height);
        int seekHeight = (int)res.getDimension(R.dimen.standard_seek_height);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(selectorWidth, selectorHeight);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        spinner.setLayoutParams(params);        
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, spinner.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        upButton.setLayoutParams(params);        
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.BELOW, upButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        playButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, playButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        downButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.LEFT_OF, playButton.getId());
        params.addRule(RelativeLayout.BELOW, upButton.getId());
        leftButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.RIGHT_OF, playButton.getId());
        params.addRule(RelativeLayout.BELOW, upButton.getId());
        rightButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, leftButton.getId());
        params.addRule(RelativeLayout.LEFT_OF, downButton.getId());
        stopButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, seekHeight);
        params.addRule(RelativeLayout.BELOW, downButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        seekBar.setLayoutParams(params);
        
        
        
        RelativeLayout layout = new RelativeLayout(this.getActivity().getApplicationContext());
        RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layout.setLayoutParams(labelLayoutParams);
        
        layout.addView(spinner); 
        layout.addView(upButton);
        layout.addView(playButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);
        layout.addView(stopButton);
        layout.addView(seekBar);
        
        layout.setBackgroundResource(R.drawable.standard_remote);
        
    	return layout;
    }

}
