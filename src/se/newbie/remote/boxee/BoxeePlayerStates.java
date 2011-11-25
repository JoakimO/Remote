package se.newbie.remote.boxee;

import org.json.JSONException;
import org.json.JSONObject;

import se.newbie.remote.application.RemoteApplication;
import se.newbie.remote.application.RemotePlayerStateImpl;
import se.newbie.remote.main.RemotePlayerState;
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
public class BoxeePlayerStates implements JSONRPC2NotificationListener {
	private final static String TAG = "BoxeePlayerState";

	public enum BoxeePlayer {
		VideoPlayer, AudioPlayer
	}

	private RemotePlayerStateImpl audioPlayerState;
	private RemotePlayerStateImpl videoPlayerState;
	private BoxeeRemoteDevice device;

	public BoxeePlayerStates(BoxeeRemoteDevice device) {
		this.device = device;
		device.getConnection().addNotificationListener(this);
		updateState();
	}

	public RemotePlayerState getAudioPlayerState() {
		return audioPlayerState;
	}

	public void setAudioPlayerState(JSONRPC2Response response) {
		audioPlayerState = createState(response, BoxeePlayer.AudioPlayer);
	}

	public RemotePlayerState getVideoPlayerState() {
		return videoPlayerState;
	}

	public void setVideoPlayerState(JSONRPC2Response response) {
		videoPlayerState = createState(response, BoxeePlayer.VideoPlayer);
	}

	public RemotePlayerStateImpl createState(JSONRPC2Response response,
			BoxeePlayer player) {
		RemotePlayerStateImpl state = new RemotePlayerStateImpl();
		state.setIdentification(device.getIdentifier() + "-" + player.ordinal());
		state.setPlaying(response.getBooleanResult("playing"));
		state.setDevice(device.getIdentifier());

		state.setNextCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerSkipNext
				.name() : BoxeeRemoteCommand.Command.audioPlayerSkipNext.name());
		state.setPreviousCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerSkipPrevious
				.name() : BoxeeRemoteCommand.Command.audioPlayerSkipPrevious
				.name());
		state.setPauseCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerPlay
				.name() : BoxeeRemoteCommand.Command.audioPlayerPlay.name());
		state.setPlayCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerPlay
				.name() : BoxeeRemoteCommand.Command.audioPlayerPlay.name());
		state.setSeekCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerSeek
				.name() : BoxeeRemoteCommand.Command.audioPlayerSeek.name());
		state.setStopCommand((player == BoxeePlayer.VideoPlayer) ? BoxeeRemoteCommand.Command.videoPlayerStop
				.name() : BoxeeRemoteCommand.Command.audioPlayerStop.name());

		if (state.isPlaying()) {
			state.setPaused(response.getBooleanResult("paused"));
			JSONObject responseState = response.getJSONObject("state");
			if (responseState != null) {
				try {
					state.setNextAvailable(responseState
							.getBoolean("can-play-next"));
					state.setPreviousAvailable(responseState
							.getBoolean("can-play-previous"));
					state.setSeekable(responseState.getBoolean("seekable"));
					state.setTitle(responseState.getString("label"));
				} catch (JSONException e) {
					Log.w(TAG, "Could not read player state");
				}
			}
		}
		return state;
	}

	private void updateState() {
		BoxeeRemoteDeviceConnection connection = device.getConnection();
		// {"jsonrpc": "2.0", "method": "VideoPlayer.State", "id": 1}
		JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler() {
			public void onResponse(JSONRPC2Response response) {
				try {
					setVideoPlayerState(response);
					updateTimeState(BoxeePlayer.VideoPlayer, videoPlayerState);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}

		};
		JSONRPC2Request request = connection
				.createJSONRPC2Request("VideoPlayer.State");
		if (request != null) {
			connection.sendRequest(request, responseHandler);
		}

		// {"jsonrpc": "2.0", "method": "AudioPlayer.State", "id": 1}
		responseHandler = new JSONRPC2ResponseHandler() {
			public void onResponse(JSONRPC2Response response) {
				try {
					setAudioPlayerState(response);
					updateTimeState(BoxeePlayer.AudioPlayer, audioPlayerState);
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

	private void updateTimeState(final BoxeePlayer player,
			final RemotePlayerStateImpl state) {
		if (state != null && state.isPlaying()) {
			BoxeeRemoteDeviceConnection connection = device.getConnection();
			JSONRPC2Request request = null;
			// { "jsonrpc": "2.0","id": 1,"method": "AudioPlayer.GetTime" }
			JSONRPC2ResponseHandler responseHandler = new JSONRPC2ResponseHandler() {
				public void onResponse(JSONRPC2Response response) {
					try {
						((RemotePlayerStateImpl) state).setStateTime(System
								.currentTimeMillis());
						JSONObject timeObject = response.getJSONObject("time");
						if (state != null && timeObject != null) {
							((RemotePlayerStateImpl) state)
									.setTime(getTimeMillis(timeObject));
							((RemotePlayerStateImpl) state)
									.setDuration(getTimeMillis(response
											.getJSONObject("total")));
							state.setPaused(response.getBooleanResult("paused"));
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
					RemoteApplication.getInstance().getRemoteModel()
							.updateRemotePlayerState(state);
				}

			};
			if (player == BoxeePlayer.AudioPlayer) {
				request = connection
						.createJSONRPC2Request("AudioPlayer.GetTime");
			} else if (player == BoxeePlayer.VideoPlayer) {
				request = connection
						.createJSONRPC2Request("VideoPlayer.GetTime");
			}

			if (request != null) {
				connection.sendRequest(request, responseHandler);
			}
		} else {
			state.setStateTime(-1);
			state.setDuration(-1);
			state.setTime(-1);
			RemoteApplication.getInstance().getRemoteModel()
					.updateRemotePlayerState(state);
		}
	}

	/**
	 * Method to parse the input from JSON object and return the time in
	 * milliseconds.
	 */
	private long getTimeMillis(JSONObject object) {
		long time = 0;

		try {
			time += (object.getInt("hours") * 60 * 60 * 1000);
			time += (object.getInt("minutes") * 60 * 1000);
			time += (object.getInt("seconds") * 1000);
			time += object.getInt("milliseconds");
		} catch (JSONException e) {
			Log.v(TAG, "Not able to parse time");
		}
		return time;
	}

	/**
	 * Playback PlaybackStarted Playback of media has started Playback
	 * PlaybackEnded Playback of media has ended Playback PlaybackPaused
	 * Playback of media has paused Playback PlaybackReumsed Playback of media
	 * has resumed after a pause Playback PlaybackSeek Playback of media has
	 * seeked to another time Playback PlaybackStopped Playback of media has
	 * stopped Playback QueueNextItem Playback of the next media item in a
	 * playlist
	 */
	public void onNotification(JSONRPC2Notification notification) {

		if (notification.getStringParam("message") != null
				&& notification.getStringParam("message").equals(
						"PlaybackEnded")) {
			updateState();
		} else if (notification.getStringParam("message") != null
				&& notification.getStringParam("message")
						.equals("PlaybackSeek")) {
			updateState();
		} else if (notification.getStringParam("message") != null
				&& notification.getStringParam("message").equals(
						"PlaybackStopped")) {
			updateState();
		} else if (notification.getStringParam("message") != null
				&& notification.getStringParam("message").equals(
						"PlaybackStarted")) {
			updateState();
		} else if (notification.getStringParam("message") != null
				&& notification.getStringParam("message").equals(
						"PlaybackPaused")) {
			updateState();
		} else if (notification.getStringParam("message") != null
				&& notification.getStringParam("message").equals(
						"PlaybackResumed")) {
			updateState();
		}
	}
}
