package es.eucm.eadventure.engine.assets.specialassetrenderers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import es.eucm.eadventure.engine.EAdventureEngineActivity;
import es.eucm.eadventure.engine.R;


public class AndroidVideoActivity extends Activity implements OnCompletionListener,
OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	/**
	 * The class logger
	 */
	private static final Logger logger = Logger.getLogger("AndroidVideoActivity");
	private int videoWidth;
	private int videoHeight;
	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Bundle extras;
	private boolean videoSizeKnown = false;
	private boolean videoReady = false;

	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		extras = getIntent().getExtras();
		this.setResult(0);
	}

	private void prepareVideo(String path) {

		clean();

		try {			
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(path);
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnVideoSizeChangedListener(this);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void onCompletion(MediaPlayer arg0) {
		
		returnToAdventure();
	}

	private void returnToAdventure() {
		
		Intent i = new Intent(this, EAdventureEngineActivity.class);
		this.startActivity(i);
		this.finish();
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		if (width == 0 || height == 0) {
			return;
		}
		videoSizeKnown = true;
		videoWidth = width;
		videoHeight = height;
		if (videoReady && videoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		videoReady = true;
		if (videoReady && videoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
	}


	public void surfaceCreated(SurfaceHolder holder) {
		prepareVideo(extras.getString("media_path"));
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		clean();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		clean();
	}

	private void releaseMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void clean() {
		videoWidth = 0;
		videoHeight = 0;
		videoReady = false;
		videoSizeKnown = false;
	}

	private void startVideoPlayback() {
		
		surfaceHolder.setFixedSize(videoWidth, videoHeight);
		mediaPlayer.start();
	}
}
