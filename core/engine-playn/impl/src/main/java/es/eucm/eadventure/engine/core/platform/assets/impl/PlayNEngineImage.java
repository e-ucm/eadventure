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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import static playn.core.PlayN.assetManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import playn.core.Canvas;
import playn.core.Image;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.rendering.EAdCanvas;

public class PlayNEngineImage extends RuntimeImage<Canvas> {

	/**
	 * The buffered image
	 */
	private Image image;
	
	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("PlayNEngineImage");

	@Inject
	public PlayNEngineImage(AssetHandler assetHandler) {
		super(assetHandler);
		logger.info("New instance");
	}

	/**
	 * Creates an empty {@link DesktopEngineImage} with the given dimensions
	 * 
	 * @param width
	 * @param height
	 */
	public PlayNEngineImage(int width, int height) {
		super(null);
		//TODO		
		logger.info("New instance, width:" + width + "; height:" + height);
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
		logger.info("We are going to load " + descriptor.getUri() );
		// Some DesktopEngineImage can be created without an assetHandler
		logger.info("Loading image " + descriptor.getUri());
		if (image == null && assetHandler != null) {
			logger.info("image == null && assetHandler != null");
			try {
				image = assetManager().getImage(assetHandler.getAbsolutePath(descriptor.getUri().getPath()));
				logger.info("image: " + image);
				if (image != null) {
					logger.log(Level.INFO, "Image loaded: " + descriptor.getUri() + " from path " + assetHandler.getAbsolutePath(descriptor.getUri().getPath()));
					return image.isReady();
				} else {
					logger.log(Level.SEVERE, "Image NOT loaded: " + descriptor.getUri());
					return true;
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, 
						"Could not load image! " + descriptor.getUri(), e);
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
			logger.log(Level.INFO, "Image flushed: " + (descriptor != null ? descriptor.getUri() : "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return (image != null && image.isReady());
	}

	@Override
	public void render(EAdCanvas<Canvas> c) {
		c.getNativeGraphicContext().drawImage(image, 0, 0);
	}

}
