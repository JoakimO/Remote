package se.newbie.remote.display;

import android.app.Fragment;

/**
 * This class represent a display on the remote, it could show information about
 * the current playing item or be the TV tablet for the given channel on TV.
 * 
 * Each remote device can have as many different display as needed and they will
 * register them with the display factory which keeps all the displays.
 * 
 * All displays is build upon Android 3.0 fragments to be able to use easily for
 * phone or tablets. 
 *  
 * @author joakim
 */
public interface RemoteDisplay {
	/**
	 * Return the identifier of this display, it will be used to map
	 * the display against a view.
	 */
	public String getIdentifier();
	
	/**
	 * Return the fragment associated with the display. It will
	 * then be added to a view on the remote with different proportions.
	 */
	public Fragment createFragment();
	
	/**
	 * After the activity have initialized the fragment (e.g after onCreate()), it will make a callback with 
	 * the fragment.
	 *
	 * Fragment newFragment = remoteDisplay.createFragment();
	 * FragmentTransaction transaction = getFragmentManager().beginTransaction();
	 * transaction.replace(R.id.fragment_container, newFragment);
	 * transaction.commit();	 
	 * newFragment = getFragmentManager().findFragmentById(newFragment.getId());
	 * fragmentManager.executePendingTransactions();
	 * remoteDisplay.setFragment(newFragment);
	 *
	 * When getting this instance it will be safe to connect all the external listeners to the 
	 * views, The root view is retrived with fragment.getView().
	 *
	 * Design principle for display fragments should follow KISS (keep it simple, Stupid!). 
	 * Implement the functionality in the display and tell the Fragment when to 
	 * update it's views.
	 */
	public void setFragment(Fragment fragment);
	
	public Fragment getFragment();
}
