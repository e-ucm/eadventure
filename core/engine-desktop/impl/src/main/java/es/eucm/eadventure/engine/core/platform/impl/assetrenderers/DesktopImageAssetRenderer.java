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

package es.eucm.eadventure.engine.core.platform.impl.assetrenderers;

import java.awt.Graphics2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineImage;

@Singleton
public class DesktopImageAssetRenderer implements AssetRenderer<Graphics2D, DesktopEngineImage> {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DesktopImageAssetRenderer.class);

	public DesktopImageAssetRenderer() {
		logger.info("New instance");
	}
	
	@Override
	public void render(Graphics2D graphicContext, DesktopEngineImage asset, EAdPosition position, float scale, int offsetX, int offsetY) {
		if (asset != null) {
			if (!asset.isLoaded())
				asset.loadAsset();
			
			int x = position.getJavaX(asset.getWidth() * scale) + offsetX;
			int y = position.getJavaY(asset.getHeight() * scale) + offsetY;
			if (scale == 1.0f)
				graphicContext.drawImage(asset.getImage(), x, y, null);
			else {
				graphicContext.drawImage(asset.getImage(), x, y, (int) (asset.getWidth() * scale), (int) (asset.getHeight() * scale), null);
			}
		}
	}

	@Override
	public boolean contains(int x, int y, DesktopEngineImage asset) {
		if (asset != null && x < asset.getWidth() && y < asset.getHeight()){
            int alpha = asset.getImage().getRGB( x, y ) >>> 24;
        	return alpha > 128;
		}
		return false;
	}		

}
