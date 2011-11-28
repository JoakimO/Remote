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
	 * returns the title of the playing media. 
	 */
	public String getLabel();
	
	
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
	
	/**
	 * Returns the owner device identification id.
	 */
	public String getDevice();

	/**
	 * This is the key the player will be registered with.
	 */
	public String getIdentification();

	/**
	 * This command will execute if the media is seekable and the seekbar 
	 * has changed
	 */
	public String getSeekCommand();

	/**
	 * This command will execute if the media is playing and the pause button 
	 * is pressed
	 */
	public String getPauseCommand();

	/**
	 * This command will execute if the media is paused and the play button
	 * is pressed
	 */
	public String getPlayCommand();
	
	public String getStopCommand();

	/**
	 * This command will execute if the media is skippable and the next button
	 * is pressed.
	 */
	public String getNextCommand();


	/**
	 * This command will execute if the media is skippable and the previous button
	 * is pressed.
	 */
	public String getPreviousCommand();	
	
}
