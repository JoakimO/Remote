package se.newbie.remote.tellduslive;

import se.newbie.remote.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TelldusLiveMainRemoteDisplayFragment extends Fragment {
	private final static String TAG = "TelldusLiveMainRemoteDisplayFragment";
	private View view;
	
	public TelldusLiveMainRemoteDisplayFragment() {
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		view = inflater.inflate(R.layout.telldus_live_main_display_layout, container, false);
		
		
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  //outState.putString("BoxeeRemoteDisplayFragment.remoteDevice", boxeeRemoteDevice.getIdentifier());
	}   
}
