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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.DesktopAssetHandler;

public class DesktopEngineImage extends RuntimeImage {

	/**
	 * The buffered image
	 */
	private BufferedImage image;

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("DesktopEngineImage");

	@Inject
	public DesktopEngineImage(AssetHandler assetHandler) {
		super(assetHandler);
		logger.info("New instance");
	}

	/**
	 * Creates an empty {@link DesktopEngineImage} with the given dimensions
	 * 
	 * @param width
	 * @param height
	 */
	public DesktopEngineImage(int width, int height) {
		super(null);
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		// Test code to see if it has any influence in performance
		if (width >= 800)
			image.setAccelerationPriority(1.0f);
		
		logger.info("New instance, width:" + width + "; height:" + height);
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		if (image != null)
			return image.getWidth();
		return 1;
	}

	public int getHeight() {
		if (image != null)
			return image.getHeight();
		return 1;
	}

	@Override
	public boolean loadAsset() {
		// Some DesktopEngineImage can be created without an assetHandler
		if (image == null && assetHandler != null) {
			try {
				image = ImageIO.read(((DesktopAssetHandler) assetHandler
						).getResourceAsStream(descriptor.getUri().getPath()));
				logger.log(Level.INFO, "Image loaded: " + descriptor.getUri());
				return true;
			} catch (IOException e) {
				logger.log(Level.SEVERE, 
						"Could not load image! " + descriptor.getUri(), e);
				return false;
			} catch (Exception e) {
				logger.log(Level.SEVERE, 
						"Could not load image! " + descriptor.getUri(), e);
				return false;
			}
		}
		return assetHandler != null;
	}

	@Override
	public void freeMemory() {
		if (image != null) {
			image.flush();
			logger.log(Level.INFO, "Image flushed: " + (descriptor != null ? descriptor.getUri() : "no descriptor"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return image != null;
	}

}
