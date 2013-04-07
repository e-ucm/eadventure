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

package ead.engine.core.gdx.desktop.platform.assets;

import java.awt.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.multimedia.EAdVideo;
import ead.engine.core.assets.SpecialAssetRenderer;
import ead.engine.core.game.interfaces.SoundManager;

@Singleton
public class VLC2VideoRenderer implements
		SpecialAssetRenderer<EAdVideo, Component>, MediaPlayerEventListener {

	private static final Logger logger = LoggerFactory
			.getLogger("VLCDesktopVideoRenderer");
	private static EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private static MediaPlayer mediaPlayer;

	private static void init() {
		try {
			if (mediaPlayerComponent != null) {
				mediaPlayerComponent.release();
			}
			mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
			mediaPlayer = mediaPlayerComponent.getMediaPlayer();
		} catch (Exception e) {
			logger.warn("VLC installation not found");
		}
	}

	static {
		init();
	}

	private GdxDesktopAssetHandler assetHandler;

	private SoundManager soundManager;

	private boolean finished;

	private int count;
	private boolean started;
	private String path;
	private boolean wasSilence;

	@Inject
	public VLC2VideoRenderer(GdxDesktopAssetHandler assetHandler,
			SoundManager soundManager) {
		this.assetHandler = assetHandler;
		this.soundManager = soundManager;
	}

	public Component getComponent(EAdVideo asset) {
		if (mediaPlayer != null) {
			try {
				return getVLCComponent(asset);
			} catch (Exception e) {
				logger.warn("VLC not supported in this OS. Videos won't load");
				this.setFinished(true);
				return null;
			}
		} else {
			logger.warn("VLC not supported in this OS. Videos won't load");
			this.setFinished(true);
			return null;
		}
	}

	protected Component getVLCComponent(EAdVideo asset) {
		init();
		mediaPlayer.addMediaPlayerEventListener(this);
		count = asset.isStream() ? -1 : 1;
		started = false;
		path = asset.getUri();
		if (assetHandler != null && !asset.isStream()) {
			path = assetHandler.getTempFilePath(asset.getUri());
		}
		if (asset.isStream()) {
			mediaPlayer.setPlaySubItems(true);
		}
		String[] mediaOptions = {};
		mediaPlayer.prepareMedia(path, mediaOptions);
		finished = false;
		return mediaPlayerComponent;
	}

	@Override
	public boolean start() {
		if (!started && mediaPlayer != null) {
			wasSilence = soundManager.isSilence();
			soundManager.setSilence(true);
			mediaPlayer.play();
			started = true;
			return true;
		}
		return false;
	}

	/**
	 * Set the finished flag
	 * 
	 * @param b
	 *            The new value for finished
	 */
	public void setFinished(boolean b) {
		if (b)
			soundManager.setSilence(wasSilence);
		this.finished = b;
	}

	public void stop() {
		mediaPlayer.setPlaySubItems(false);
	}

	/**
	 * Set the started flag
	 * 
	 * @param b
	 *            The new value for started
	 */
	public void setStarted(boolean b) {
		this.started = b;
	}

	@Override
	public void reset() {
		finished = false;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void backward(MediaPlayer arg0) {

	}

	@Override
	public void buffering(MediaPlayer arg0, float arg1) {

	}

	@Override
	public void endOfSubItems(MediaPlayer arg0) {

	}

	@Override
	public void error(MediaPlayer arg0) {

	}

	@Override
	public void finished(MediaPlayer arg0) {
		count--;
		if (count == 0) {
			setFinished(true);
			setStarted(false);
		}

	}

	@Override
	public void forward(MediaPlayer arg0) {

	}

	@Override
	public void lengthChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {

	}

	@Override
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void mediaFreed(MediaPlayer arg0) {

	}

	@Override
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {

	}

	@Override
	public void newMedia(MediaPlayer arg0) {

	}

	@Override
	public void opening(MediaPlayer arg0) {

	}

	@Override
	public void pausableChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void paused(MediaPlayer arg0) {

	}

	@Override
	public void playing(MediaPlayer arg0) {

	}

	@Override
	public void positionChanged(MediaPlayer arg0, float arg1) {

	}

	@Override
	public void seekableChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void snapshotTaken(MediaPlayer arg0, String arg1) {

	}

	@Override
	public void stopped(MediaPlayer arg0) {

	}

	@Override
	public void subItemFinished(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void subItemPlayed(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void timeChanged(MediaPlayer arg0, long arg1) {

	}

	@Override
	public void titleChanged(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void videoOutput(MediaPlayer arg0, int arg1) {

	}

}
