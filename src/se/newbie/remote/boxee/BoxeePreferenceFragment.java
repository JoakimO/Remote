package se.newbie.remote.boxee;

import java.util.List;

import se.newbie.remote.R;
import se.newbie.remote.application.RemoteActivity;
import se.newbie.remote.device.RemoteDevice;
import se.newbie.remote.device.RemoteDevice.RemoteDeviceType;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class BoxeePreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.boxee_preferences);
		
		PreferenceScreen root = this.getPreferenceScreen();
		
		//TODO add some way to determine type of remoteDevices.
		List<RemoteDevice> remoteDevices = RemoteActivity.getRemoteApplication().getRemoteModel().getRemoteDevices();
		if (remoteDevices != null) {
			Context context = this.getActivity();
			for (RemoteDevice remoteDevice : remoteDevices) {
				if (remoteDevice.getRemoteDeviceType() == RemoteDeviceType.Boxee) {
					PreferenceCategory category = new PreferenceCategory(context);
					category.setTitle(remoteDevice.getRemoteDeviceName());
					
					EditTextPreference userPreference = new EditTextPreference(context);
					userPreference.setDialogTitle(R.string.dialog_title_user);
					userPreference.setKey(remoteDevice.getIdentifier() + ".user");
					userPreference.setTitle(R.string.title_user);	
					userPreference.setDefaultValue("boxee");
					userPreference.setSummary(R.string.summary_title_user);	
	
					EditTextPreference passwordPreference = new EditTextPreference(context);
					passwordPreference.setDialogTitle(R.string.dialog_title_password);
					passwordPreference.setKey(remoteDevice.getIdentifier() + ".password");
					passwordPreference.setTitle(R.string.title_password);	
					passwordPreference.setSummary(R.string.summary_title_password);				
					
					root.addPreference(category);
					category.addPreference(userPreference);
					category.addPreference(passwordPreference);				
				}
			}
		}
	}
}