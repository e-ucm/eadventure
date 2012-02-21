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

package ead.engine.core.platform.assets.drawables.basics;

import com.google.inject.Inject;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DesktopAssetHandler;
import ead.engine.core.platform.assets.drawables.basics.RuntimeImage;
import ead.engine.core.platform.rendering.GenericCanvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesktopImage extends RuntimeImage<Graphics2D> {

	/**
	 * The buffered image
	 */
	private BufferedImage image;

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger("DesktopEngineImage");

	@Inject
	public DesktopImage(AssetHandler assetHandler) {
		super(assetHandler);
		logger.info("New instance");
	}

	/**
	 * Creates an empty {@link DesktopImage} with the given dimensions
	 *
	 * @param width
	 * @param height
	 */
	public DesktopImage(int width, int height) {
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
				logger.debug("Image loaded: '{}'", descriptor.getUri());
				return true;
			} catch (Exception e) {
				logger.error("Error loading image: '{}'", descriptor.getUri(), e);
				return false;
			}
		}
		return assetHandler != null;
	}

	@Override
	public void freeMemory() {
		if (image != null) {
			image.flush();
			logger.info("Image flushed: '{}'",
                    (descriptor != null ? descriptor.getUri() : "<no descriptor>"));
		}
		image = null;
	}

	@Override
	public boolean isLoaded() {
		return image != null;
	}

	public boolean contains( int x, int y ){
		if ( super.contains(x, y)){
			int alpha = image.getRGB( x, y ) >>> 24;
			return alpha > 128;
		}
		return false;
	}

	@Override
	public void render( GenericCanvas<Graphics2D> c ){
		c.getNativeGraphicContext().drawImage(this.getImage(), 0, 0, null);
	}

}
