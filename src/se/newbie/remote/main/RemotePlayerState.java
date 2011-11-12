package se.newbie.remote.main;

public interface RemotePlayerState {
	/**
 	 * Returns true if the current media is playing.
 	 */
	public boolean isPlaying();
	
	/**
	 * Returns true if the media is paused. 
	 */		
	public boolean isPaused();
	
	/**
	 * Returns true if the next button should be enabled. 
	 */		
	public boolean isNextAvailable();
	
	/**
	 * Returns true if the previous button should be enabled. 
	 */
	public boolean isPreviousAvailable();
	
	/**
	 * Returns if the media is seek able, if this variable is set
	 * the duration, time remaining and time variable must be set. 
	 */
	public boolean isSeekable();
	
	/**
	 * Get the network address to the playing file. 
	 */
	public String getFile();
	
	
	/**
	 * Returns the duration in milliseconds. 
	 */
	public long getDuration();
	
	/**
	 * Returns the duration in milliseconds. 
	 */
	public long getTime();	
	
	/**
	 * Returns the local time when the state was updated.
	 * 
	 * Current playing time must be calculated :
	 * (System.currentTimeMillis() - getStateTime()) + getTime();
	 */
	public long getStateTime();
}
