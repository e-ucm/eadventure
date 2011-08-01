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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.engine.assets.AndroidBezierShape;
import es.eucm.eadventure.engine.assets.AndroidEngineColor;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;

public class AndroidBezierShapeRenderer implements AssetRenderer<Canvas, AndroidBezierShape> {

	@Override
	public void render(Canvas graphicContext, AndroidBezierShape asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		
		if (!asset.isLoaded())
			asset.loadAsset();
		
		BezierShape assetDescriptor = asset.getAssetDescriptor();
		int x = position.getJavaX(asset.getWidth() * scale) + offsetX;
		int y = position.getJavaY(asset.getHeight() * scale) + offsetY;
		if (assetDescriptor.getColor().getBorderColor().getAlpha() != 0 || assetDescriptor.getColor().getCenterColor().getAlpha() != 0) {
			Paint paint = new Paint();
			graphicContext.save();

			graphicContext.translate(x, y);
			graphicContext.scale(scale, scale);

			paint.setStyle(Style.FILL);
			paint.setColor(new AndroidEngineColor(assetDescriptor.getColor().getCenterColor()).getColor());
			graphicContext.drawPath(asset.getShape(), paint);

			paint.setStyle(Style.STROKE);
			paint.setColor(new AndroidEngineColor(assetDescriptor.getColor().getBorderColor()).getColor());
			paint.setStrokeWidth(assetDescriptor.getBorderWidth());
			graphicContext.drawPath(asset.getShape(), paint);

			graphicContext.restore();

		}
	}

	@Override
	public boolean contains(int x, int y, AndroidBezierShape asset) {
		if (asset != null && x < asset.getWidth() && y < asset.getHeight()){
			//TODO no contains?
			//return asset.getShape().contains(x, y);
			return true;
		}
		return false;
	}

}
