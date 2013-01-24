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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jna.NativeLibrary;

import ead.common.model.assets.multimedia.EAdVideo;
import ead.engine.core.platform.assets.SpecialAssetRenderer;

/**
 * <p>
 * Video renderer for desktop (and applets) using vlcj library {@link http
 * ://code.google.com/p/vlcj/}
 * </p>
 *
 *
 */
@Singleton
public class VLCDesktopVideoRenderer implements
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
	private static MediaPlayerFactory mediaPlayerFactory;

	/**
	 * Used to configure vlc when necessary
	 */
	private static String vlcOptions = "";

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
	public VLCDesktopVideoRenderer(GdxDesktopAssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		initializeVariables();
	}

	@Override
	public Component getComponent(EAdVideo asset) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JLabel label = new JLabel("   SKIP >>   ");
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setOpaque(true);
		label.requestFocus();
		label.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		JPanel p = new JPanel(new FlowLayout());
		p.setBackground(Color.BLACK);
		p.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		p.add(label);
		panel.add(p, BorderLayout.AFTER_LAST_LINE);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (vlcLoaded) {
					mediaPlayer.stop();
				} else {
					setFinished(true);
					setStarted(false);
				}
			}

		});

		Component c = null;
		if (vlcLoaded) {
			c = getVLCComponent(asset);
		} else {
			c = getErrorComponent();
		}
		panel.add(c, BorderLayout.CENTER);
		return panel;
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
		videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
		mediaPlayer.setVideoSurface(videoSurface);

		mediaPlayer
				.addMediaPlayerEventListener(new VLCMediaPlayerEventListener(
						this, asset.isStream() ? -1 : 1));

		path = asset.getUri().getPath();
		if (assetHandler != null && !asset.isStream()) {
			path = assetHandler.getTempFilePath(asset.getUri().getPath());
		}

		if (asset.isStream()) {
			mediaPlayer.setPlaySubItems(true);
		}

		finished = false;

		return canvas;
	}

	protected Component getErrorComponent() {
		return new VLCErrorPanel();
	}

	private class VLCErrorPanel extends JPanel {
		private static final long serialVersionUID = -1227824919051690700L;
		private BufferedImage img;

		public VLCErrorPanel() {
			try {
				img = ImageIO
						.read(ClassLoader
								.getSystemResourceAsStream("ead/engine/desktop/drawable/vlc.png"));
			} catch (IOException e) {

			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (img != null) {
				super.paintComponent(g);
				int width = this.getWidth();
				int height = this.getHeight();
				g.setColor(Color.BLACK);
				g.clearRect(0, 0, width, height);
				g.drawImage(img, 0, 0, null);
			}
		}
	}

	/**
	 * Initialize system and system dependent variables for vlcj.
	 */
	private void initializeVariables() {
		vlcLoaded = false;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			String temp = null;
			try {
				temp = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE,
						"Software\\VideoLAN\\VLC", "InstallDir");
				if (temp == null) {
					temp = WinRegistry.readString(
							WinRegistry.HKEY_LOCAL_MACHINE,
							"Software\\Wow6432Node\\VideoLAN\\VLC",
							"InstallDir");
				}
				logger.info("VLC folder: '{}'", temp);
			} catch (Exception e) {
				logger.debug("VLC folder not found in Windows Registry");
			}

			if (temp == null) {
				logger.warn("VLC not installed");
				// not exists, extract
				return;

			}
			String pathLibvlc = temp;
			String pathPlugins = temp + "\\plugins";
			NativeLibrary.addSearchPath("vlc", pathLibvlc);
			System.setProperty("jna.library.path", pathLibvlc);
			System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
			vlcOptions = "--no-video-title-show";
			vlcLoaded = true;
		} else if (os.contains("mac")) {
			String temp = "/Applications/VLC.app";
			if (!new File("/Applications/VLC.app/").exists()) {
				logger.warn("VLC not installed");
				// not exists, extract
				// temp = ....;
			} else {
				logger.info("VLC installed");
			}
			String pathLibvlc = temp + "/Contents/MacOS/lib/";
			String pathPlugins = temp + "/Contents/MacOS/plugins/";
			NativeLibrary.addSearchPath("vlc", pathLibvlc);
			System.setProperty("jna.library.path", pathLibvlc);
			System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
			vlcOptions = "--vout=macosx";
			vlcLoaded = true;
		} else if (os.contains("linux")) {
			File[] libDirs = new File[] { new File("/usr/lib/vlc"),
					new File("/usr/local/lib/vlc") };
			File libDir = null;
			for (File d : libDirs) {
				if (d.exists()) {
					libDir = d;
					break;
				}
			}
			if (libDir != null) {
				logger.info("VLC installation at {}", libDir);
				String pathPlugins = new File(libDir, "plugins")
						.getAbsolutePath();
				String pathLibvlc = libDir.getAbsolutePath();
				NativeLibrary.addSearchPath("vlc", pathLibvlc);
				System.setProperty("jna.library.path", pathLibvlc);
				System.setProperty("VLC_PLUGIN_PATH", pathPlugins);
				vlcLoaded = true;
			} else {
				logger.error("No VLC installations found (linux OS)");
			}
		} else {
			logger.error("OS '{}' not supported by VLC video plugin", os);
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
