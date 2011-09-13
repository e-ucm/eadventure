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

import playn.core.Canvas;
import playn.core.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.FillFactory;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNBezierShape;

@Singleton
public class PlayNBezierShapeRenderer implements
		AssetRenderer<Canvas, PlayNBezierShape> {
	
	private FillFactory<Canvas, Path> fillFactory;
	
	@Inject
	public PlayNBezierShapeRenderer( FillFactory<Canvas, Path> fillFactory){
		this.fillFactory = fillFactory;
	}

	@Override
	public void render(Canvas graphicContext, PlayNBezierShape asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {

		if (!asset.isLoaded())
			asset.loadAsset();

		int x = position.getJavaX(asset.getWidth() * scale) + offsetX;
		int y = position.getJavaY(asset.getHeight() * scale) + offsetY;

//		Color temp = graphicContext.getColor();
		
		graphicContext.translate(x, y);
		graphicContext.scale(scale, scale);

//		Stroke s = graphicContext.getStroke();
		fillFactory.fill(asset.getAssetDescriptor().getFill(), graphicContext, asset.getShape());
//		graphicContext.setStroke(s);

//		graphicContext.setColor(temp);
		graphicContext.scale(1/scale, 1/scale);
		graphicContext.translate(-x, -y);

	}

	@Override
	public boolean contains(int x, int y, PlayNBezierShape asset) {
		if (asset != null && x < asset.getWidth() && y < asset.getHeight()) {
			return true;
			//return asset.getShape().contains(x, y);
		}
		return false;
	}

}
