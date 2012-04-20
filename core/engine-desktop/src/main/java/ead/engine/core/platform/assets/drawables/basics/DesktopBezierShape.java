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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import ead.common.model.elements.extra.EAdList;
import ead.common.params.paint.EAdPaint;
import ead.engine.core.platform.rendering.DesktopCanvas;
import ead.engine.core.platform.rendering.GenericCanvas;

public class DesktopBezierShape extends RuntimeBezierShape<Graphics2D> {

	private GeneralPath path;

	private BufferedImage pathImage;

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		path = new GeneralPath();
		EAdList<Integer> points = descriptor.getPoints();
		if (points.size() < 0) {
			logger.warn(
					"Bezier shape descriptor hasn't got enough points. ({})",
					descriptor.toString());
			return false;
		}

		path.moveTo(points.get(0), points.get(1));

		int pointIndex = 2;
		float x1, y1, x2, y2, x3, y3;

		while (pointIndex < points.size()) {
			int length = points.get(pointIndex++);
			switch (length) {
			case 1:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				path.lineTo(x1, y1);
				break;
			case 2:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				x2 = points.get(pointIndex++);
				y2 = descriptor.getPoints().get(pointIndex++);
				path.quadTo(x1, y1, x2, y2);
				break;
			case 3:
				x1 = points.get(pointIndex++);
				y1 = points.get(pointIndex++);
				x2 = points.get(pointIndex++);
				y2 = points.get(pointIndex++);
				x3 = points.get(pointIndex++);
				y3 = points.get(pointIndex++);
				path.curveTo(x1, y1, x2, y2, x3, y3);
				break;
			default:

			}
		}

		if (descriptor.isClosed())
			path.closePath();

		if (!descriptor.isPaintAsVector()) {
			generatePathImage();
		}

		return true;
	}

	public GeneralPath getShape() {
		return path;
	}

	@Override
	public boolean contains(int x, int y) {
		return path.contains(x, y);
	}

	@Override
	public void freeMemory() {
		if (path != null) {
			path.reset();
			path = null;
		}
		if (pathImage != null) {
			pathImage.flush();
		}
		this.loaded = false;
	}

	@Override
	public void render(GenericCanvas<Graphics2D> c) {
		if (descriptor.isPaintAsVector()) {
			c.setPaint(descriptor.getPaint());
			c.drawShape(this);
		} else {
			c.getNativeGraphicContext().drawImage(pathImage, 0, 0, null);
		}
	}

	private void generatePathImage() {
		EAdPaint paint = descriptor.getPaint();

		Rectangle2D bounds = path.getBounds();
		int width = Math.max(
				(int) (bounds.getWidth() + bounds.getX())
						+ paint.getBorderWidth() * 2, 1);
		int height = Math.max((int) (bounds.getHeight() + bounds.getY())
				+ paint.getBorderWidth() * 2, 1);

		pathImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) pathImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Fill
		if (paint.getFill() != null) {
			g.setPaint(DesktopCanvas.createPaint(paint.getFill()));
			g.fill(path);
		}
		// Border
		if (paint.getBorder() != null && paint.getBorderWidth() > 0) {
			g.setPaint(DesktopCanvas.createPaint(paint.getBorder()));
			g.setStroke(new BasicStroke(paint.getBorderWidth()));
			g.draw(path);
		}
		g.dispose();
	}

}
