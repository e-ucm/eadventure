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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jna.NativeLibrary;

import ead.common.model.assets.multimedia.EAdVideo;
import ead.engine.core.assets.SpecialAssetRenderer;

/**
 * <p>
 * Video renderer for desktop (and applets) using vlcj library {@link http
 * ://code.google.com/p/vlcj/}
 * </p>
 * 
 * 
 */
@Singleton
public class VLCVideoRenderer implements
		SpecialAssetRenderer<EAdVideo, Component> {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("VLCDesktopVideoRenderer");

	/**
	 * The vlcj media player (controls, etc.)
	 */
	private EmbeddedMediaPlayer mediaPlayer;

	/**
	 * The vlcj surface for the video
	 */
	private CanvasVideoSurface videoSurface;

	/**
	 * True if finished
	 */
	protected boolean finished;

	/**
	 * True if started
	 */
	protected boolean started;

	/**
	 * The vlcj media player factory
	 */
	private MediaPlayerFactory mediaPlayerFactory;

	/**
	 * Used to configure vlc when necessary
	 */
	private String vlcOptions = "";

	/**
	 * The path of the video file
	 */
	private String path;

	/**
	 * The eAd asset handler
	 */
	private GdxDesktopAssetHandler assetHandler;

	/**
	 * Sets if VLC has been successfully loaded
	 */
	private boolean vlcLoaded;

	@Inject
	public VLCVideoRenderer(GdxDesktopAssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		initializeVariables();
	}

	@Override
	public Component getComponent(EAdVideo asset) {
		if (vlcLoaded) {
			return getVLCComponent(asset);
		} else {
			logger.warn("VLC not supported in this OS. Videos won't load");
			this.setFinished(true);
			return null;
		}
	}

	protected Component getVLCComponent(EAdVideo asset) {
		if (mediaPlayerFactory == null) {
			String[] options = { vlcOptions };

			mediaPlayerFactory = new MediaPlayerFactory(options);
		}

		Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		started = false;

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setAdjustVideo(false);
		mediaPlayer.setCropGeometry("4:3");
		videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
		mediaPlayer.setVideoSurface(videoSurface);

		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				finished = true;
			}

		});

		mediaPlayer
				.addMediaPlayerEventListener(new VLCMediaPlayerEventListener(
						this, asset.isStream() ? -1 : 1));

		path = asset.getUri();
		if (assetHandler != null && !asset.isStream()) {
			path = assetHandler.getTempFilePath(asset.getUri());
		}

		if (asset.isStream()) {
			mediaPlayer.setPlaySubItems(true);
		}

		finished = false;

		return canvas;
	}

	/**
	 * Initialize system and system dependent variables for vlcj.
	 */
	private void initializeVariables() {
		vlcLoaded = false;
		String pathLibvlc = null;
		String pathPlugins = null;
		vlcOptions = "--no-video-title-show";
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			pathLibvlc = "vlc/vlc-windows";
			pathPlugins = "vlc/vlc-windows/plugins";
		} else if (os.contains("mac")) {
			pathLibvlc = "vlc/vlc-mac/Contents/MacOS/lib/";
			pathPlugins = "vlc/vlc-mac/Contents/MacOS/plugins/";
			vlcOptions += " --vout=macosx";
		} else if (os.contains("linux")) {
			pathLibvlc = "vlc/vlc-linux/";
			pathPlugins = "vlc/vlc-linux/plugins";
		}

		if (pathLibvlc == null) {
			logger.warn("VLC not supported by this OS. Videos won't load.");
		} else {
			NativeLibrary.addSearchPath("vlc", pathLibvlc);
			System.setProperty("jna.library.path", pathLibvlc);
			System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
			vlcLoaded = true;
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean start() {
		if (!started && mediaPlayer != null) {
			String[] mediaOptions = {};
			mediaPlayer.prepareMedia(path, mediaOptions);
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

}
