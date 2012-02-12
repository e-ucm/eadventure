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

package ead.engine.core.platform.assets;

import static playn.core.PlayN.assetManager;

import playn.core.Canvas;
import playn.core.Image;

import com.google.inject.Inject;

import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.assets.RuntimeImage;
import ead.engine.core.platform.rendering.GenericCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayNEngineImage extends RuntimeImage<Canvas> {

	/**
	 * The buffered image
	 */
	private Image image;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("PlayNEngineImage");

	@Inject
	public PlayNEngineImage(AssetHandler assetHandler) {
		super(assetHandler);
		logger.info("New instance");
	}

	public Image getImage() {
		return image;
	}

	public int getWidth() {
		if (image != null)
			return image.width();
		return 1;
	}

	public int getHeight() {
		if (image != null)
			return image.height();
		return 1;
	}

	@Override
	public boolean loadAsset() {
		// Some DesktopEngineImage can be created without an assetHandler
		logger.info("Loading image " + descriptor.getUri());
		if (image == null && assetHandler != null) {
			try {
                String path = assetHandler.getAbsolutePath(
                        descriptor.getUri().getPath());
            	image = assetManager().getImage(path);
				if (image != null) {
					logger.info("Image loaded OK: {} from {} width {}",
                            new Object[]{descriptor.getUri(), path, image.width()});
					return image.isReady();
				} else {
					logger.error("Image NOT loaded: {}", descriptor.getUri());
					return true;
				}
			} catch (Exception e) {
				logger.error("Error loading image: {}", descriptor.getUri(), e);
				return false;
			}
		}
		return assetHandler != null && image.isReady();
	}

	@Override
	public void freeMemory() {
		if (image != null) {
			//TODO flush image
			//image.flush();
			logger.info("Image flushed: {}",
                    (descriptor != null ? descriptor.getUri() : "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return (image != null && image.width() > 0 && image.isReady());
	}

	@Override
	public void render(GenericCanvas<Canvas> c) {
		c.getNativeGraphicContext().drawImage(image, 0, 0);
	}
}
