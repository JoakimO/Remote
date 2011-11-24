package se.newbie.remote.application;

import se.newbie.remote.main.RemotePlayerState;

public class RemotePlayerStateImpl implements RemotePlayerState {
	private boolean isPlaying;
	private boolean isPaused;
	private boolean isNextAvailable;
	private boolean isPreviousAvailable;
	private boolean isSeekable;
	private String  file;
	private long    duration;
	private long	stateTime;
	private long    time;
	private String  device;
	private String  identification;
	private String  seekCommand;
	private String  pauseCommand;
	private String  playCommand;
	private String  nextCommand;
	private String  previousCommand;
	
	
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public boolean isPaused() {
		return isPaused;
	}
	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	public boolean isNextAvailable() {
		return isNextAvailable;
	}
	public void setNextAvailable(boolean isNextAvailable) {
		this.isNextAvailable = isNextAvailable;
	}
	public boolean isPreviousAvailable() {
		return isPreviousAvailable;
	}
	public void setPreviousAvailable(boolean isPreviousAvailable) {
		this.isPreviousAvailable = isPreviousAvailable;
	}
	public boolean isSeekable() {
		return isSeekable;
	}
	public void setSeekable(boolean isSeekable) {
		this.isSeekable = isSeekable;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getStateTime() {
		return stateTime;
	}
	public void setStateTime(long stateTime) {
		this.stateTime = stateTime;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getSeekCommand() {
		return seekCommand;
	}
	public void setSeekCommand(String seekCommand) {
		this.seekCommand = seekCommand;
	}
	public String getPauseCommand() {
		return pauseCommand;
	}
	public void setPauseCommand(String pauseCommand) {
		this.pauseCommand = pauseCommand;
	}
	public String getPlayCommand() {
		return playCommand;
	}
	public void setPlayCommand(String playCommand) {
		this.playCommand = playCommand;
	}
	public String getNextCommand() {
		return nextCommand;
	}
	public void setNextCommand(String nextCommand) {
		this.nextCommand = nextCommand;
	}
	public String getPreviousCommand() {
		return previousCommand;
	}
	public void setPreviousCommand(String previousCommand) {
		this.previousCommand = previousCommand;
	}
}
