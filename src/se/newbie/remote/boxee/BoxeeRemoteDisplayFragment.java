package se.newbie.remote.boxee;

import se.newbie.remote.R;
import se.newbie.remote.boxee.BoxeeRemoteDisplay.InfoLabel;
import se.newbie.remote.boxee.BoxeeRemoteDisplay.MediaItem;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoxeeRemoteDisplayFragment extends Fragment {
	private final static String TAG = "BoxeeRemoteDisplayFragment";
	private MediaItem mediaItem;
	private LinearLayout contentLayout;
	
	public BoxeeRemoteDisplayFragment() {
	}
	
	private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v(TAG, "Handler update view..");
				updateFragment(mediaItem);
			}
		};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreateView(inflater, container, savedInstanceState);
		
		//if (savedInstanceState != null) {
		//	boxeeRemoteDevice = (BoxeeRemoteDevice)RemoteApplication.getInstance()
		//		.getRemoteDeviceFactory().getRemoteDevice(savedInstanceState.getString("BoxeeRemoteDisplayFragment.remoteDevice"));
		//}    	
   
		View view = inflater.inflate(R.layout.standard_display_layout, container, false);
		contentLayout = (LinearLayout)view.findViewById(R.id.standard_display_content);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  //outState.putString("BoxeeRemoteDisplayFragment.remoteDevice", boxeeRemoteDevice.getIdentifier());
	}    
	
	public void update(MediaItem mediaItem) {
		this.mediaItem = mediaItem;
		handler.sendEmptyMessage(0); 
	}
	
	private void updateFragment(MediaItem mediaItem) {
		contentLayout.removeAllViews();
		Log.v(TAG, "Update fragment: " + mediaItem);
		if (mediaItem != null) {
			if (mediaItem.getMediaType() == BoxeeMediaType.video) {
				LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.boxee_video_view, null);    	
				
				TextView textView;
				
				textView = (TextView)view.findViewById(R.id.boxee_video_title);
				textView.setText(mediaItem.getTitle());
				textView = (TextView)view.findViewById(R.id.boxee_video_year);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoYear.getName(), ""));
				
				textView = (TextView)view.findViewById(R.id.boxee_video_genre);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoGenre.getName(), ""));
				textView = (TextView)view.findViewById(R.id.boxee_video_duration);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoDuration.getName(), ""));
				
				textView = (TextView)view.findViewById(R.id.boxee_video_plotoutline);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoPlotoutline.getName(), ""));		
				
				textView = (TextView)view.findViewById(R.id.boxee_video_director);
				textView.setText(mediaItem.getProperty(InfoLabel.VideoDirector.getName(), ""));		
				
				contentLayout.addView(view);
			} else if (mediaItem.getMediaType() == BoxeeMediaType.music) {
				LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.standard_display_music_layout, contentLayout);    	
				
				TextView textView;
				
				textView = (TextView)view.findViewById(R.id.standard_display_music_title);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicTitle.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_artist);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicArtist.getName(), ""));				
				textView = (TextView)view.findViewById(R.id.standard_display_music_duration); 
				textView.setText(mediaItem.getProperty(InfoLabel.MusicDuration.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_genre);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicGenre.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_year);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicYear.getName(), ""));
				textView = (TextView)view.findViewById(R.id.standard_display_music_album);
				textView.setText(mediaItem.getProperty(InfoLabel.MusicAlbum.getName(), ""));				
			}
		} else {
			
		}
	}
}