package se.newbie.remote.application;

import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.gui.RemoteButton;
import se.newbie.remote.gui.RemoteGUIFactory;
import se.newbie.remote.gui.RemoteSeekBar;
import se.newbie.remote.universal.RemoteDeviceBaseAdapter;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class RemoteFragment extends Fragment {
	private final static String TAG = "RemoteFragment";
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	
    	RemoteApplication remoteApplication = RemoteApplication.getInstance();
    	RemoteGUIFactory remoteGUIFactory = remoteApplication.getRemoteModel().getRemoteGUIFactory();
    	
    	RemoteButton leftButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Left");
        RemoteButton rightButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Right");
        RemoteButton upButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Up");
        RemoteButton downButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Down");
        RemoteButton stopButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Back");
        RemoteButton playButton = remoteGUIFactory.createButton(getActivity().getApplicationContext(), "Select");
        
        RemoteSeekBar seekBar = remoteGUIFactory.createSeekBar(getActivity().getApplicationContext());
        
        Spinner spinner = new Spinner(getActivity().getApplicationContext());
        spinner.setId(8);
        
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
        
        seekBar.setId(7);
        seekBar.setCommand("seek");
        seekBar.setDevice("Boxee-boxeebox");        
        seekBar.addListener(RemoteApplication.getInstance().getRemoteView());
        
        DisplayMetrics metrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        
        
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
        
        layout.addView(upButton);
        layout.addView(playButton);
        layout.addView(downButton);
        layout.addView(leftButton);
        layout.addView(rightButton);
        layout.addView(stopButton);
        layout.addView(seekBar);
    	
        
        List<RemoteDevice> list = RemoteApplication.getInstance().getRemoteModel().getRemoteDevices();
        RemoteDeviceBaseAdapter adapter = new RemoteDeviceBaseAdapter(this.getActivity(), list, R.layout.standard_device_spinner_item);

        
        spinner.setAdapter(adapter);
        spinner.setBackgroundResource(R.drawable.standard_spinner);
        //spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        	{
        		RemoteDevice remoteDevice = (RemoteDevice)parent.getItemAtPosition(position);
        		Log.v(TAG, "Selected Device:" + remoteDevice.getIdentifier());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				Log.v(TAG, "onNothingSelected");
			}
        });
        
        
        
        
        layout.addView(spinner);        
        
        layout.setBackgroundResource(R.drawable.standard_remote);
        
    	return layout;
    }

}
