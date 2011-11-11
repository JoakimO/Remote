package se.newbie.remote.boxee;

import org.json.JSONException;
import org.json.JSONObject;

import se.newbie.remote.util.jsonrpc2.JSONRPC2Notification;
import se.newbie.remote.util.jsonrpc2.JSONRPC2NotificationListener;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Request;
import se.newbie.remote.util.jsonrpc2.JSONRPC2Response;
import se.newbie.remote.util.jsonrpc2.JSONRPC2ResponseHandler;
import android.util.Log;

/**
 * This class holds all the different states for the player
 * 
 * @author joakim
 */
public class BoxeePlayerState implements JSONRPC2NotificationListener {
	private final static String TAG = "BoxeePlayerState";
	
	public enum BoxeePlayer {
		VideoPlayer, AudioPlayer
	}
	
	private PlayerState audioPlayerState;
	private PlayerState videoPlayerState;
	private BoxeeRemoteDevice device;
		
	public BoxeePlayerState(BoxeeRemoteDevice device) {
		this.device = device;
		updateState();
		device.getConnection().addNotificationListener(this);		
	}
	
	public BoxeePlayer getActivePlayer() {
		if (videoPlayerState != null && videoPlayerState.isPlaying()) {
			return BoxeePlayer.VideoPlayer; 
		} else if (audioPlayerState != null && audioPlayerState.isPlaying()) {
			return BoxeePlayer.AudioPlayer;
		}
		return null;
	}	

	public PlayerState getAudioPlayerState() {
		return audioPlayerState;
	}

	public void setAudioPlayerState(JSONRPC2Response response) {
		audioPlayerState = createState(response);
	}
	
	public PlayerState getVideoPlayerState() {
		return videoPlayerState;
	}


	public void setVideoPlayerState(JSONRPC2Response response) {
		videoPlayerState = createState(response);
	}
	
	public PlayerState createState(JSONRPC2Response response) {
		PlayerState state = new PlayerState();
		
		state.setPlaying(response.getBooleanResult("playing"));
		if (state.isPlaying()) {
			state.setPaused(response.getBooleanResult("paused"));
			JSONObject responseState = response.getJSONObject("state");
			if (responseState != null) {
				try {
					state.setNextAvailable(responseState.getBoolean("can-play-next"));
					state.setPreviousAvailable(responseState.getBoolean("can-play-previous"));
					state.setSeekable(responseState.getBoolean("seekable"));
					state.setFile(responseState.getString("stream"));
				} catch (JSONException e) {
					Log.w(TAG, "Could not read player state");
				}
			}
		}
		
		return state;
	}


	private void updateState() {
		BoxeeRemoteDeviceConnection connection = device.getConnection();
		//{"jsonrpc": "2.0", "method": "VideoPlayer.State", "id": 1}
    	JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				try {
					setVideoPlayerState(response);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
    		
    	};
		JSONRPC2Request request = connection.createJSONRPC2Request("VideoPlayer.State");
		if (request != null) {
			connection.sendRequest(request, responseHandler);
		}    				
		
		//{"jsonrpc": "2.0", "method": "AudioPlayer.State", "id": 1}
    	responseHandler = new JSONRPC2ResponseHandler(){
			public void onResponse(JSONRPC2Response response) {
				try {
					setAudioPlayerState(response);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
    		
    	};
		request = connection.createJSONRPC2Request("AudioPlayer.State");
		if (request != null) {
			connection.sendRequest(request, responseHandler);
		}    						
	}

	/**
	 * Playback 	PlaybackStarted 	Playback of media has started
	 * Playback 	PlaybackEnded 	Playback of media has ended
	 * Playback 	PlaybackPaused 	Playback of media has paused
	 * Playback 	PlaybackReumsed 	Playback of media has resumed after a pause
	 * Playback 	PlaybackSeek 	Playback of media has seeked to another time
	 * Playback 	PlaybackStopped 	Playback of media has stopped
	 * Playback 	QueueNextItem 	Playback of the next media item in a playlist 
	 */
	public void onNotification(JSONRPC2Notification notification) {
		
		if (notification.getStringParam("message") != null &&
				notification.getStringParam("message").equals("PlaybackEnded")) {
			updateState();
		} else if (notification.getStringParam("message") != null &&
				notification.getStringParam("message").equals("PlaybackSeek")) {
			updateState();
		} else if (notification.getStringParam("message") != null &&
				notification.getStringParam("message").equals("PlaybackStopped")) {
			updateState();
		} else if (notification.getStringParam("message") != null &&
				notification.getStringParam("message").equals("PlaybackStarted")) {
			updateState();
		}
	}	
	
	/**
	 * Holds the current state of a player, players can be video or audio players.
	 * 
	 * There is a possibility that both players are active at the same time;
	 */
	public class PlayerState {
		private boolean isPlaying;
		private boolean isPaused;
		private boolean isNextAvailable;
		private boolean isPreviousAvailable;
		private boolean isSeekable;
		private String  file;
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
	}
}
