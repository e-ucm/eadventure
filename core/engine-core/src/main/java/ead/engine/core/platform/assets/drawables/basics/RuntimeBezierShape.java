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

import java.util.List;

import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.util.EAdPosition;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;

public abstract class RuntimeBezierShape<GraphicContext> extends
		AbstractRuntimeAsset<BezierShape> implements
		RuntimeDrawable<BezierShape, GraphicContext> {

	protected boolean loaded = false;

	protected int width = 0;

	protected int height = 0;

	@Override
	public boolean loadAsset() {
		int point = 0;

		EAdPosition p = null;
		int xMax = Integer.MIN_VALUE;
		int xMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;

		for (Integer i : descriptor.getSegmentsLength()) {
			p = descriptor.getPoints().get(point);
			xMax = xMax < p.getX() ? p.getX() : xMax;
			xMin = xMin > p.getX() ? p.getX() : xMin;
			yMax = yMax < p.getY() ? p.getY() : yMax;
			yMin = yMin > p.getY() ? p.getY() : yMin;
			point += i;
		}

		p = descriptor.getPoints().get(point);
		xMax = xMax < p.getX() ? p.getX() : xMax;
		xMin = xMin > p.getX() ? p.getX() : xMin;
		yMax = yMax < p.getY() ? p.getY() : yMax;
		yMin = yMin > p.getY() ? p.getY() : yMin;

		width = xMax - xMin;
		height = yMax - yMin;
		loaded = true;
		return true;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	public void render(GenericCanvas<GraphicContext> c) {
		c.setPaint(descriptor.getPaint());
		c.drawShape(this);
	}
	
	@Override
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states, int level) {
		return this;
	}

}
