package se.newbie.remote.boxee;

import se.newbie.remote.R;
import se.newbie.remote.boxee.BoxeeBrowserRemoteDisplay.BoxeeBrowserFileAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BoxeeBrowserFragment extends Fragment {
	private final static String TAG = "BoxeeBrowserFragment";
	private View view;
	
	private Handler	handler = new Handler() { 
			@Override
			public void handleMessage(Message msg) {
				Log.v(TAG, "handleMessage");
				//fileAdapter.clear();
				//fileAdapter.addFiles(files);     
				ListView listView = (ListView)view.findViewById(R.id.standard_browser_list_view);	
				if (listView != null) {
					((BoxeeBrowserFileAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
			}
		};	  	

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		
		//if (savedInstanceState != null) {
		//	device = (BoxeeRemoteDevice)RemoteApplication.getInstance()
		//		.getRemoteDeviceFactory().getRemoteDevice(savedInstanceState.getString("BoxeeBrowserFragment.remoteDevice"));
		//}         	
		view = inflater.inflate(R.layout.standard_browser_layout, container, false);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  //super.onSaveInstanceState(outState);
	  //outState.putString("BoxeeBrowserFragment.remoteDevice", device.getIdentifier());
	}      

	public void update() {
		handler.sendEmptyMessage(0); 
	}
}