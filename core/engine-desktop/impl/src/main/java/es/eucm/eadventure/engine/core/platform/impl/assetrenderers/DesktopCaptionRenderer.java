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

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.FillFactory;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineFont;

@Singleton
public class DesktopCaptionRenderer implements
		AssetRenderer<Graphics2D, DesktopEngineCaption> {

	private FillFactory<Graphics2D, Shape> fillFactory;

	@Inject
	public DesktopCaptionRenderer(FillFactory<Graphics2D, Shape> fillFactory) {
		this.fillFactory = fillFactory;
	}

	@Override
	public void render(Graphics2D g, DesktopEngineCaption asset) {
		if (!asset.isLoaded())
			asset.loadAsset();

		Graphics2D g2 = (Graphics2D) g.create();

		int width = asset.getWidth();
		int height = asset.getHeight() ;

		if (asset.getCaption().hasBubble()
				& asset.getCaption().getBubbleFill() != null)
			drawBubble(g2, width, height, asset.getCaption().getBubbleFill());

		g2.translate(asset.getCaption().getPadding(), asset.getCaption()
				.getPadding());
		int yOffset = - asset.getFont().lineHeight() / 4;
		for (String s : asset.getText()) {
			yOffset += asset.getFont().stringBounds(s).getHeight();
			drawString(g2, asset, s, yOffset);
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

		DesktopEngineFont deFont = (DesktopEngineFont) text.getFont();
		Composite c = g.getComposite();
		AffineTransform a = g.getTransform();

		g.setFont(deFont.getFont());
		g.translate(0, yOffset);

		fillFactory.fill(text.getAssetDescriptor().getTextFill(), g, string);

		g.setComposite(c);
		g.setTransform(a);
	}

	private void drawBubble(Graphics2D g, int width, int height,
			EAdFill bubbleFill) {

		Shape shape = new Rectangle(0, 0, width, height);

		fillFactory.fill(bubbleFill, g, shape);

	}

}
