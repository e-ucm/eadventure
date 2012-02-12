/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.assets.specialassetrenderers;

import java.io.IOException;

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
import ead.editor.R;
import ead.engine.EAdventureEngineActivity;
import ead.engine.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidVideoActivity extends Activity implements OnCompletionListener,
OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	/**
	 * The class logger
	 */
	private static final Logger logger = LoggerFactory.getLogger("AndroidVideoActivity");
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

		} catch (Exception e) {
			logger.error("preparing video from {}", path,  e);
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
