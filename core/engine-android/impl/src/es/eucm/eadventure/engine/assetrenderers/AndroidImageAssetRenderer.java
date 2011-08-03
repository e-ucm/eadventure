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

package es.eucm.eadventure.engine.assetrenderers;

import java.util.logging.Logger;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;

@Singleton
public class AndroidImageAssetRenderer implements AssetRenderer<Canvas, AndroidEngineImage> {

	private static final Logger logger = Logger.getLogger("AndroidImageAssetRenderer");

	@Override
	public void render(Canvas graphicContext, AndroidEngineImage asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		if (asset != null && asset.getImage() != null) {
			int x = position.getJavaX(asset.getWidth() * scale) + offsetX;
			int y = position.getJavaY(asset.getHeight() * scale) + offsetY;

			if (scale == 1.0f){
				graphicContext.drawBitmap(asset.getImage(), x, y, null);
			}
			else {					
				Matrix matrix = new Matrix();					
				matrix.postScale(scale, scale);
				matrix.postTranslate(x, y);
				graphicContext.drawBitmap(asset.getImage(), matrix, null);
			}
		} else {
			if (asset == null)
				logger.info("Null asset..." );
			else {
				logger.info("Null image: " + asset.getAssetDescriptor().getURI());
			}
		}
	}	

	public void sizeChanged(int width, int height) {	        
	}

	@Override
	public boolean contains(int x, int y, AndroidEngineImage asset) {
		if (asset != null && asset.getImage() != null) {
			if (x < asset.getWidth() && y < asset.getHeight())
				return true;
		}
		return false;
	}


}


