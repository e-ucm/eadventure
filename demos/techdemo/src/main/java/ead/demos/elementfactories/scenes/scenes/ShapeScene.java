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

package ead.demos.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.hud.ModifyHUDEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.assets.ShapeFactory;

/**
 * Scene with shapes. Test containst for different types of shapes
 * 
 * 
 */
public class ShapeScene extends EmptyScene {
	public ShapeScene() {
		this.setId("ShapeScene");
		int margin = 10;
		int size = 140;
		int x = margin;

		ShapeFactory shapeFactory = EAdElementsFactory.getInstance()
				.getShapeFactory();

		// Rectangle
		EAdDrawable rectangleAsset1 = shapeFactory.getElement(
				ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size,
				Paint.WHITE_ON_BLACK);
		EAdDrawable rectangleAsset2 = shapeFactory.getElement(
				ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size,
				Paint.BLACK_ON_WHITE);
		EAdSceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(rectangleAsset1,
						rectangleAsset2, x + 20, margin);
		e.setVarInitialValue(SceneElement.VAR_SCALE, 0.5f);
		e.setVarInitialValue(SceneElement.VAR_ROTATION, 10.0f);
		getSceneElements().add(e);
		x += margin + size;

		// Circle
		EAdDrawable asset1 = shapeFactory.getElement(
				ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new Paint(
						ColorFill.YELLOW, ColorFill.BROWN, 4));
		EAdDrawable asset2 = shapeFactory.getElement(
				ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new Paint(
						ColorFill.ORANGE, ColorFill.BROWN, 2));
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(asset1, asset2, x, margin));
		x += margin + size;

		// Triangle
		EAdDrawable asset31 = shapeFactory.getElement(
				ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new Paint(
						ColorFill.BLUE, ColorFill.BROWN));
		EAdDrawable asset32 = shapeFactory.getElement(
				ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new Paint(
						ColorFill.CYAN, ColorFill.BLACK));

		SceneElement triangle = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(asset31, asset32,
						x, margin);
		triangle.setId("ead.demos.triangle");
		getSceneElements().add(triangle);
		x += margin + size;

		// Irregular shape 1
		EAdDrawable asset51 = shapeFactory.getElement(
				ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size,
				new Paint(ColorFill.MAGENTA, ColorFill.YELLOW));
		EAdDrawable asset52 = shapeFactory.getElement(
				ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size,
				new Paint(ColorFill.RED, ColorFill.ORANGE));
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(asset51, asset52, x, margin));
		x += margin + size;

		// Random shape
		EAdDrawable asset41 = shapeFactory.getElement(
				ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size,
				new Paint(ColorFill.GRAY, ColorFill.BROWN));
		EAdDrawable asset42 = shapeFactory.getElement(
				ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size,
				new Paint(ColorFill.DARK_GRAY, ColorFill.BLACK));
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(asset41, asset42, x, margin));
		x = margin;

		// Drop shape
		EAdDrawable asset61 = shapeFactory.getElement(
				ShapeFactory.ShapeType.DROP_SHAPE, size, size, new Paint(
						ColorFill.GRAY, ColorFill.BROWN));
		EAdDrawable asset62 = shapeFactory.getElement(
				ShapeFactory.ShapeType.DROP_SHAPE, size, size, new Paint(
						ColorFill.DARK_GRAY, ColorFill.BLACK));
		getSceneElements().add(
				EAdElementsFactory.getInstance().getSceneElementFactory()
						.createSceneElement(asset61, asset62, x,
								margin * 2 + size));
		x += margin + size;

		// Rectangle rotating
		EAdDrawable asset21 = shapeFactory.getElement(
				ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3,
				new Paint(ColorFill.RED, ColorFill.BROWN, 2));
		EAdDrawable asset22 = shapeFactory.getElement(
				ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3,
				new Paint(ColorFill.LIGHT_BROWN, ColorFill.DARK_BROWN, 10));
		SceneElement rotatingRectangle = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(asset21, asset22,
						330, 200);
		rotatingRectangle.setPosition(Corner.CENTER, 400, 300);
		rotatingRectangle.setId("ead.demos.rotatingrectangle");
		getSceneElements().add(rotatingRectangle);
		InterpolationEf interpolation = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getInterpolationEffect(
						new BasicField<Float>(rotatingRectangle,
								SceneElement.VAR_ROTATION), 0, 360.0f, 2000,
						InterpolationLoopType.RESTART, InterpolationType.LINEAR);
		SceneElementEv event = EAdElementsFactory.getInstance()
				.getEventsFactory().getEvent(SceneElementEvType.INIT,
						interpolation);
		rotatingRectangle.getEvents().add(event);
		getSceneElements().add(rotatingRectangle);

		SceneElement rotatingRectangle2 = new SceneElement(rotatingRectangle
				.getDefinition());
		rotatingRectangle2.setPosition(Corner.CENTER, 400, 300);
		ModifyHUDEf effect = new ModifyHUDEf(rotatingRectangle2, true);
		rotatingRectangle.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);
		InterpolationEf interpolation2 = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getInterpolationEffect(
						new BasicField<Float>(rotatingRectangle2,
								SceneElement.VAR_ROTATION), 0,
						(float) (Math.PI * 2.0), 2000,
						InterpolationLoopType.RESTART, InterpolationType.LINEAR);
		rotatingRectangle2.addBehavior(MouseGEv.MOUSE_ENTERED, interpolation2);

	}

}
