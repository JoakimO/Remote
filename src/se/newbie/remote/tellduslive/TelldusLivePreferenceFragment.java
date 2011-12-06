package se.newbie.remote.tellduslive;

import se.newbie.remote.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class TelldusLivePreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.telldus_live_preferences);
	}
}