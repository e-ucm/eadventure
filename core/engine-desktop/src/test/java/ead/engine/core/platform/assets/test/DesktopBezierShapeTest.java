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

package ead.engine.core.platform.assets.test;

import junit.framework.TestCase;

import org.junit.Test;

import ead.common.resources.assets.drawable.basics.shapes.BallonShape;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import ead.engine.core.TestUtil;
import ead.engine.desktop.core.platform.assets.drawables.basics.DesktopBezierShape;

public class DesktopBezierShapeTest extends TestCase {

	@Test
	public void testShapeLoadUnload() {
		DesktopBezierShape shape = TestUtil.getInjector().getInstance(DesktopBezierShape.class);
		BezierShape shapeDescriptor = new BallonShape(0, 0, 100, 100, BalloonType.CLOUD);
		shape.setDescriptor(shapeDescriptor);
		shape.loadAsset();

		assertTrue(shape.isLoaded());

		shape.freeMemory();

		assertFalse(shape.isLoaded());
	}
}
