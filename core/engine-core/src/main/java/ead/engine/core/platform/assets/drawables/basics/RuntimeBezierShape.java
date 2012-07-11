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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.engine.core.platform.rendering.GenericCanvas;

public abstract class RuntimeBezierShape<GraphicContext> extends
		AbstractRuntimeAsset<BezierShape> implements
		RuntimeDrawable<BezierShape, GraphicContext> {

	protected final static Logger logger = LoggerFactory
			.getLogger("RuntimeBezierShape");

	protected boolean loaded = false;

	protected int width = 0;

	protected int height = 0;

	@Override
	public boolean loadAsset() {

		if (descriptor.getPoints().size() < 0) {
			logger.warn(
					"Bezier shape descriptor hasn't got enough points. ({})",
					descriptor.toString());
			return false;
		}

		Integer points = 1;
		int xMax = Integer.MIN_VALUE;
		int xMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;

		int index = 0;
		int index2 = 0;

		while (index < descriptor.getPoints().size()) {
			index2 = 0;
			while (index2 < points) {
				int x = descriptor.getPoints().get(index);
				int y = descriptor.getPoints().get(index + 1);
				xMax = xMax < x ? x : xMax;
				xMin = xMin > x ? x : xMin;
				yMax = yMax < y ? y : yMax;
				yMin = yMin > y ? y : yMin;

				index += 2;
				index2++;
			}
			if (index < descriptor.getPoints().size()) {
				points = descriptor.getPoints().get(index);
			}
			index++;
		}

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
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states,
			int level) {
		return this;
	}

}
