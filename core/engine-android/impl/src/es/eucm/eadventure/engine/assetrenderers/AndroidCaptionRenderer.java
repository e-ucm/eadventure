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
import android.graphics.Path;
import es.eucm.eadventure.common.params.EAdFill;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.assets.AndroidEngineCaption;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.FillFactory;

public class AndroidCaptionRenderer implements
		AssetRenderer<Canvas, AndroidEngineCaption> {

	private FillFactory<Canvas, Path> fillFactory;

	public AndroidCaptionRenderer(FillFactory<Canvas, Path> fillFactory) {
		this.fillFactory = fillFactory;
	}

	@Override
	public void render(Canvas g, AndroidEngineCaption asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// TODO use offsets

		if (!asset.isLoaded())
			asset.loadAsset();

		int xLeft = position.getJavaX(asset.getWidth() * scale);
		int yTop = position.getJavaY(asset.getHeight() * scale);
		int width = (int) (asset.getWidth() * scale);
		int height = (int) (asset.getHeight() * scale);

		g.save();
		g.translate(offsetX + xLeft, offsetY + yTop);

		if (asset.getCaption().hasBubble()
				& asset.getCaption().getBubbleFill() != null)
			drawBubble(g, width, height, asset.getCaption().getBubbleFill());

		g.translate(asset.getCaption().getPadding(), asset.getCaption()
				.getPadding());
		int yOffset = 0;
		for (String s : asset.getText()) {
			yOffset += asset.getFont().lineHeight();
			drawString(g, asset, s, yOffset);
		}
		g.restore();

	}

	@Override
	public boolean contains(int x, int y, AndroidEngineCaption asset) {
		if (asset == null || asset.getBounds() == null)
			return false;
		boolean tempValue = x < asset.getBounds().width
				&& y < asset.getBounds().height;
		return tempValue;
	}

	protected void drawString(Canvas g, AndroidEngineCaption text,
			String string, int yOffset) {

		g.save();
		g.translate(0, yOffset);
		fillFactory.fill(text.getAssetDescriptor().getTextFill(), g, string);
		g.restore();
	}

	private void drawBubble(Canvas g, int width, int height, EAdFill bubbleColor) {

		Path p = new Path();
		p.addRect(0, 0, width, height, Path.Direction.CW);
		fillFactory.fill(bubbleColor, g, p);

	}

}
