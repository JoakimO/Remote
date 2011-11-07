package se.newbie.remote.application;

import java.util.List;

import se.newbie.remote.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;


public class RemotePreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
	
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.general_preferences);
            
            
        }
    }
    
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	RemoteModel remoteModel = RemoteActivity.getRemoteApplication().getRemoteModel();
    	remoteModel.setBroadcast(outState.getBoolean("is_broadcast", true));    	
    }*/
}