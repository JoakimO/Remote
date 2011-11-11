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
	public Fragment getFragment();
}
