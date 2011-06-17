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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineColor;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineFont;

public class DesktopCaptionRenderer implements
		AssetRenderer<Graphics2D, DesktopEngineCaption> {

	private static final Logger logger = Logger
			.getLogger("DesktopCaptionRenderer");

	public DesktopCaptionRenderer() {
		logger.info("New instance");
	}

	@Override
	public void render(Graphics2D g, DesktopEngineCaption asset,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// TODO use offsets

		if (!asset.isLoaded())
			asset.loadAsset();
		
		Graphics2D g2 = (Graphics2D) g.create();

		int xLeft = position.getJavaX(asset.getWidth() * scale);
		int yTop = position.getJavaY(asset.getHeight() * scale);
		int width = (int) (asset.getWidth() * scale);
		int height = (int) (asset.getHeight() * scale);

		g2.translate(offsetX + xLeft, offsetY + yTop);

		if (asset.getCaption().hasBubble()
				& asset.getCaption().getBubbleColor() != null)
			drawBubble(g2, width, height, asset.getCaption().getBubbleColor());

		
		g2.translate(asset.getCaption().getPadding(), asset.getCaption().getPadding() + asset.getFont().lineHeight() );
		int yOffset = 0;
		for (String s : asset.getText()) {
			drawString(g2, asset, s, yOffset );
			yOffset += asset.getLineHeight();
		}

	}

	@Override
	public boolean contains(int x, int y, DesktopEngineCaption asset) {
		if (asset == null || asset.getBounds() == null)
			return false;
		boolean tempValue = x < asset.getBounds().width
				&& y < asset.getBounds().height;
		return tempValue;
	}

	protected void drawString(Graphics2D g, DesktopEngineCaption text,
			String string, int yOffset) {
		float alpha = text.getAlpha();
		EAdBorderedColor textColor = text.getCaption().getTextColor();
		DesktopEngineFont deFont = (DesktopEngineFont) text.getFont();
		Composite c = g.getComposite();
		if (alpha != 1.0f)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					alpha));

		g.setFont(deFont.getFont());

		Color borderColor = new DesktopEngineColor(textColor.getBorderColor())
				.getColor();
		Color color = new DesktopEngineColor(textColor.getCenterColor())
				.getColor();

		g.setColor(borderColor);

		g.drawString(string, -1, yOffset - 1);
		g.drawString(string, 1, yOffset + 1);
		g.drawString(string, -1, yOffset + 1);
		g.drawString(string, 1, yOffset - 1);

		g.setColor(color);

		g.drawString(string, 0, yOffset);
		g.setComposite(c);
	}

	private void drawBubble(Graphics2D g, int width, int height,
			EAdBorderedColor bubbleColor) {

		DesktopEngineColor color = new DesktopEngineColor(
				bubbleColor.getCenterColor());

		if (bubbleColor != null) {
			g.setPaint(new GradientPaint(0, 0, color.getColor(), 0, height,
					color.getColor().darker()));
			g.fillRoundRect(0, 0, width, height, 15, 15);
		}

		DesktopEngineColor border = new DesktopEngineColor(
				bubbleColor.getBorderColor());
		if (border != null) {
			g.setColor(border.getColor());
			g.drawRoundRect(0, 0, width, height, 15, 15);
		}

	}

}
