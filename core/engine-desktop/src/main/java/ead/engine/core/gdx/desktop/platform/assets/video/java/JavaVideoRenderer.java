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

package ead.engine.core.gdx.desktop.platform.assets.video.java;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.CannotRealizeException;
import javax.media.Codec;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.assets.multimedia.EAdVideo;
import ead.engine.core.assets.SpecialAssetRenderer;
import ead.engine.core.game.interfaces.SoundManager;
import ead.engine.core.gdx.desktop.platform.assets.GdxDesktopAssetHandler;

public class JavaVideoRenderer implements
		SpecialAssetRenderer<EAdVideo, Component> {
	private static Logger logger = LoggerFactory
			.getLogger("DesktopVideoRenderer");

	private static boolean loaded = false;

	private boolean finished = false;

	private boolean started = false;

	private Player mediaPlayer;

	private static String CODEC_CLASS_NAME = "net.sourceforge.jffmpeg.VideoDecoder";

	private GdxDesktopAssetHandler assetHandler;

	private Component video;

	private SoundManager soundManager;

	private boolean wasSilence;

	@Inject
	public JavaVideoRenderer(GdxDesktopAssetHandler assetHandler,
			SoundManager soundManager) {
		this.assetHandler = assetHandler;
		this.soundManager = soundManager;
	}

	public Component getComponent(EAdVideo asset) {
		if (video != null) {
			return video;
		}

		video = null;
		finished = false;
		if (!loaded) {
			try {
				Codec video = (Codec) Class.forName(CODEC_CLASS_NAME)
						.newInstance();
				PlugInManager.addPlugIn(CODEC_CLASS_NAME, video
						.getSupportedInputFormats(), video
						.getSupportedOutputFormats(null), PlugInManager.CODEC);
				PlugInManager.commit();
				loaded = true;
			} catch (Exception e) {
				logger.error("Could not load video codec!");
				finished = true;
			}
		}

		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		try {
			String path = asset.getUri();
			if (assetHandler != null)
				path = assetHandler.getTempFilePath(asset.getUri());
			MediaLocator mediaLocator = new MediaLocator("file://" + path);
			mediaPlayer = Manager.createRealizedPlayer(mediaLocator);
			mediaPlayer.addControllerListener(new ControllerListener() {

				@Override
				public void controllerUpdate(ControllerEvent event) {
					if (event instanceof RealizeCompleteEvent) {
						logger.info("RealizeCompleteEvent");
						// realized = true;
						// notify( );
					} else if (event instanceof EndOfMediaEvent) {
						logger.info("EndOfMediaEvent");
						mediaPlayer.close();
						mediaPlayer.deallocate();
						mediaPlayer = null;
						finished = true;
						soundManager.setSilence(wasSilence);
					} else if (event instanceof StopEvent) {
						logger.info("StopEvent");
						mediaPlayer.close();
						mediaPlayer.deallocate();
						mediaPlayer = null;
						finished = true;
						soundManager.setSilence(wasSilence);
						// notify( );
					} else if (event instanceof PrefetchCompleteEvent) {
						logger.info("PrefetchCompleteEvent");
						// notify( );
					}
				}

			});
			video = mediaPlayer.getVisualComponent();

		} catch (NoPlayerException e) {
			logger.error("No player", e);
			finished = true;
		} catch (MalformedURLException e) {
			logger.error("Malformed URL", e);
			finished = true;
		} catch (IOException e) {
			logger.error("IO Exception", e);
			finished = true;
		} catch (CannotRealizeException e) {
			logger.error("Cannot realized player", e);
			finished = true;
		}
		if (video != null) {
			video.setSize(800, 600);
			video.setPreferredSize(new Dimension(800, 600));
		}
		return video;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean start() {
		if (!started && video != null) {
			wasSilence = soundManager.isSilence();
			soundManager.setSilence(true);
			mediaPlayer.start();
			started = true;
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		started = false;
		finished = false;
	}
}
