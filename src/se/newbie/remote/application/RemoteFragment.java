package se.newbie.remote.application;

import se.newbie.remote.R;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.gui.RemoteImageButton;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.gui.RemoteSpinner;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
        RemoteButton backButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Back", "Boxee-boxeebox", "back");
        RemoteButton selectButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Select", "Boxee-boxeebox", "select");
        RemoteImageButton muteButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_mute, "Boxee-boxeebox", "muteToggle");
        RemoteImageButton volumeUpButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_up, "Boxee-boxeebox", "volumeUp");
        RemoteImageButton volumeDownButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_down, "Boxee-boxeebox", "volumeDown");
        RemoteSeekBar seekBar = remoteGUIFactory.createSeekBar(getActivity().getApplicationContext(), "Boxee-boxeebox", "seek");
        RemoteSpinner spinner = remoteGUIFactory.createSpinner(getActivity().getApplicationContext(), "Predefined", "selectRemoteDevice");
        
        RemoteImageButton previousButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_up, "Boxee-boxeebox", "skipPrevious");
        RemoteImageButton nextButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_up, "Boxee-boxeebox", "skipNext");
        RemoteImageButton playToggleButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_up, "Boxee-boxeebox", "play");
        RemoteImageButton stopButton = remoteGUIFactory.createImageButton(getActivity().getApplicationContext(), R.drawable.ic_vol_up, "Boxee-boxeebox", "stop");
        
        //RemoteButton playButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Select", "Boxee-boxeebox", "select");
        
        
        leftButton.setId(1);
        leftButton.addListener(RemoteApplication.getInstance().getRemoteView());
        rightButton.setId(2);
        rightButton.addListener(RemoteApplication.getInstance().getRemoteView());
        upButton.setId(3);
        upButton.addListener(RemoteApplication.getInstance().getRemoteView());
        downButton.setId(4);
        downButton.addListener(RemoteApplication.getInstance().getRemoteView());
        backButton.setId(5);
        backButton.addListener(RemoteApplication.getInstance().getRemoteView());
        selectButton.setId(6);
        selectButton.addListener(RemoteApplication.getInstance().getRemoteView());
        seekBar.setId(7);
        seekBar.addListener(RemoteApplication.getInstance().getRemoteView());
        spinner.setId(8);
        spinner.addListener(RemoteApplication.getInstance().getRemoteView());        
        muteButton.setId(9);
        muteButton.addListener(RemoteApplication.getInstance().getRemoteView());
        volumeUpButton.setId(9);
        volumeUpButton.addListener(RemoteApplication.getInstance().getRemoteView());
        volumeDownButton.setId(9);
        volumeDownButton.addListener(RemoteApplication.getInstance().getRemoteView());
        previousButton.setId(10);
        previousButton.addListener(RemoteApplication.getInstance().getRemoteView());
        nextButton.setId(11);
        nextButton.addListener(RemoteApplication.getInstance().getRemoteView());
        playToggleButton.setId(12);
        playToggleButton.addListener(RemoteApplication.getInstance().getRemoteView());
        stopButton.setId(13);
        stopButton.addListener(RemoteApplication.getInstance().getRemoteView());        
        
        Resources res = getResources();
        int selectorWidth = (int)res.getDimension(R.dimen.standard_device_selector_width);
        int selectorHeight = (int)res.getDimension(R.dimen.standard_device_selector_height);        
        int buttonWidth = (int)res.getDimension(R.dimen.standard_button_width);
        int buttonHeight = (int)res.getDimension(R.dimen.standard_button_height);
        int buttonSmallWidth = (int)res.getDimension(R.dimen.standard_small_button_width);
        int buttonSmallHeight = (int)res.getDimension(R.dimen.standard_small_button_height);        
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
        selectButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, selectButton.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        downButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.LEFT_OF, selectButton.getId());
        params.addRule(RelativeLayout.BELOW, upButton.getId());
        leftButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.RIGHT_OF, selectButton.getId());
        params.addRule(RelativeLayout.BELOW, upButton.getId());
        rightButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, leftButton.getId());
        params.addRule(RelativeLayout.LEFT_OF, downButton.getId());
        backButton.setLayoutParams(params);
        

        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.ABOVE, leftButton.getId());
        params.addRule(RelativeLayout.LEFT_OF, upButton.getId());
        muteButton.setLayoutParams(params);        
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.ABOVE, rightButton.getId());
        params.addRule(RelativeLayout.RIGHT_OF, upButton.getId());
        volumeUpButton.setLayoutParams(params);    
        
        params = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        params.addRule(RelativeLayout.BELOW, leftButton.getId());
        params.addRule(RelativeLayout.RIGHT_OF, downButton.getId());
        volumeDownButton.setLayoutParams(params);
        
        /* START PLAYER LAYOUT */
        
        

        RelativeLayout playerRelativelayout = new RelativeLayout(this.getActivity().getApplicationContext());
        RelativeLayout.LayoutParams playerRelativelayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        playerRelativelayout.setLayoutParams(playerRelativelayoutParams);       
        
        LinearLayout playerLayout = new LinearLayout(this.getActivity().getApplicationContext());
        //FrameLayout.LayoutParams playerLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams playerLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        playerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        playerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        playerLayout.setLayoutParams(playerLayoutParams);
        playerLayout.setOrientation(LinearLayout.VERTICAL);
        playerLayout.setBackgroundResource(R.drawable.standard_player_background);
        
        
        
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, seekHeight);
        seekBar.setLayoutParams(params);

        playerLayout.addView(seekBar);
        
        LinearLayout playerControllLayout = new LinearLayout(this.getActivity().getApplicationContext());
        LinearLayout.LayoutParams playerControllLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        playerControllLayout.setLayoutParams(playerControllLayoutParams);

        playerLayout.addView(playerControllLayout);
        
        params = new RelativeLayout.LayoutParams(buttonSmallWidth, buttonSmallHeight);
        previousButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonSmallWidth, buttonSmallHeight);
        stopButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonSmallWidth, buttonSmallHeight);
        playToggleButton.setLayoutParams(params);
        
        params = new RelativeLayout.LayoutParams(buttonSmallWidth, buttonSmallHeight);
        nextButton.setLayoutParams(params);   
        
        playerControllLayout.addView(previousButton);
        playerControllLayout.addView(stopButton);
        playerControllLayout.addView(playToggleButton);
        playerControllLayout.addView(nextButton);
        
        playerRelativelayout.addView(playerLayout);
        
        /*playerLayout.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) v.getLayoutParams();
				switch(event.getAction()) {
					case MotionEvent.ACTION_MOVE: {
						par.topMargin = (int)event.getRawY();
						v.setLayoutParams(par);
						break;
					}				
				}
				return true;
			}
		});   */      

        
        /* END PLAYER LAYOUT */
        
        
        
        FrameLayout frameLayout = new FrameLayout(this.getActivity().getApplicationContext()); 
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        frameLayout.setLayoutParams(frameLayoutParams);
        
        RelativeLayout layout = new RelativeLayout(this.getActivity().getApplicationContext());
        RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layout.setLayoutParams(labelLayoutParams);        
        
        layout.addView(spinner); 
        layout.addView(upButton);
        layout.addView(selectButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);
        layout.addView(backButton);
        layout.addView(muteButton);
        layout.addView(volumeUpButton);
        layout.addView(volumeDownButton);
        layout.setBackgroundResource(R.drawable.standard_remote);
        
        frameLayout.addView(layout);
        //frameLayout.addView(playerLayout);
        frameLayout.addView(playerRelativelayout);
        
    	return frameLayout;
    }

}
