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
}
