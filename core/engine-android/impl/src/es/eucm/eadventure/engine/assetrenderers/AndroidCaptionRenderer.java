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
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.assets.AndroidEngineCaption;
import es.eucm.eadventure.engine.assets.AndroidEngineColor;
import es.eucm.eadventure.engine.assets.AndroidEngineFont;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;

public class AndroidCaptionRenderer implements
		AssetRenderer<Canvas, AndroidEngineCaption> {

	private static final Logger logger = Logger
			.getLogger("DesktopCaptionRenderer");

	public AndroidCaptionRenderer() {
		logger.info("New instance");
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
				& asset.getCaption().getBubbleColor() != null)
			drawBubble(g, width, height, asset.getCaption().getBubbleColor());

		
		g.translate(asset.getCaption().getPadding(), asset.getCaption().getPadding() );
		int yOffset = 0;
		for (String s : asset.getText()) {
			yOffset += asset.getFont().lineHeight();
			drawString(g, asset, s, yOffset );
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
		float alpha = text.getAlpha();
		EAdBorderedColor textColor = text.getCaption().getTextColor();
		AndroidEngineFont deFont = (AndroidEngineFont) text.getFont();
		
		//TODO "add" alpha to colors?

		int borderColor = new AndroidEngineColor(textColor.getBorderColor())
				.getColor();
		int color = new AndroidEngineColor(textColor.getCenterColor())
				.getColor();

		Paint p = new Paint();
		p.setColor(borderColor);
		p.setTypeface(((AndroidEngineFont) text.getFont()).getFont());
		
		g.drawText(string, -1, yOffset - 1, p);
		g.drawText(string, 1, yOffset - 1, p);
		g.drawText(string, -1, yOffset + 1, p);
		g.drawText(string, 1, yOffset + 1, p);

		p.setColor(color);

		g.drawText(string, 0, yOffset, p);
	}

	private void drawBubble(Canvas g, int width, int height,
			EAdBorderedColor bubbleColor) {

		AndroidEngineColor color = new AndroidEngineColor(
				bubbleColor.getCenterColor());

		if (bubbleColor != null) {
			Paint p = new Paint();
			p.setColor(color.getColor());
			p.setStyle(Style.FILL);
			g.drawRoundRect(new RectF(0, 0, width, height), 15, 15, p);
		}

		AndroidEngineColor border = new AndroidEngineColor(
				bubbleColor.getBorderColor());
		if (border != null) {
			Paint p = new Paint();
			p.setColor(border.getColor());
			p.setStyle(Style.STROKE);
			g.drawRoundRect(new RectF(0, 0, width, height), 15, 15, p);
		}

	}

}
