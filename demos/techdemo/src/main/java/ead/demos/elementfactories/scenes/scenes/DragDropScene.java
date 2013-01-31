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

import ead.common.model.assets.drawable.basics.shapes.BalloonShape;
import ead.common.model.assets.drawable.basics.shapes.BezierShape;
import ead.common.model.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.fills.LinearGradientFill;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.DragGEv;
import ead.common.model.params.guievents.enums.DragGEvType;
import ead.common.model.params.util.Position;
import ead.common.model.params.util.Position.Corner;

public class DragDropScene extends EmptyScene {

	public DragDropScene() {
		this.setId("DragDropScene");
		setBackgroundFill(new LinearGradientFill(ColorFill.LIGHT_GRAY,
				new ColorFill(245, 255, 245), 800, 600));
		BezierShape shape = new BalloonShape(0, 0, 100, 100,
				BalloonType.ROUNDED_RECTANGLE);
		shape.setPaint(new LinearGradientFill(ColorFill.RED, new ColorFill(200,
				0, 0), 100, 100));

		SceneElementDef def = new SceneElementDef(shape);

		SceneElement e1 = new SceneElement(def);
		e1.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e1.setPosition(new Position(Corner.CENTER, 600, 300));
		e1.setVarInitialValue(SceneElement.VAR_Z, 0);

		SceneElement e4 = new SceneElement(def);
		e4.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e4.setPosition(new Position(Corner.TOP_LEFT, 20, 20));
		e4.setInitialScale(0.5f);
		e4.setVarInitialValue(SceneElement.VAR_Z, 1);

		SceneElement e5 = new SceneElement(def);
		e5.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e5.setPosition(new Position(Corner.TOP_RIGHT, 500, 10));
		e5.setVarInitialValue(SceneElement.VAR_ROTATION, 0.5f);
		e5.setInitialScale(1.5f);
		e5.setVarInitialValue(SceneElement.VAR_Z, 2);

		//		addComplexElement( );

		BezierShape shape2 = new BalloonShape(0, 0, 110, 110,
				BalloonType.ROUNDED_RECTANGLE);
		shape2.setPaint(Paint.BLACK_ON_WHITE);
		SceneElement e2 = new SceneElement(shape2);
		e2.setPosition(new Position(Corner.CENTER, 100, 300));

		SceneElement e3 = new SceneElement(shape2);
		e3.setPosition(new Position(Corner.CENTER, 300, 300));

		addBehaviors(e2, e1);
		addBehaviors(e3, e1);

		getSceneElements().add(e2);
		getSceneElements().add(e3);
		getSceneElements().add(e1);
		getSceneElements().add(e4);
		getSceneElements().add(e5);

	}

	private void addBehaviors(SceneElement e2, SceneElement e1) {
		EAdField<Float> scale = new BasicField<Float>(e2,
				SceneElement.VAR_SCALE);
		ChangeFieldEf changeScale1 = new ChangeFieldEf(scale, new ValueOp(1.2f));
		ChangeFieldEf changeScale2 = new ChangeFieldEf(scale, new ValueOp(1.0f));
		e2.addBehavior(new DragGEv(e1.getDefinition().getId(),
				DragGEvType.ENTERED), changeScale1);
		e2.addBehavior(new DragGEv(e1.getDefinition().getId(),
				DragGEvType.EXITED), changeScale2);

		//		BasicField<Integer> fieldX = new BasicField<Integer>(e1,
		//				SceneElement.VAR_X);
		//		BasicField<Integer> fieldY = new BasicField<Integer>(e1,
		//				SceneElement.VAR_Y);

		//		ChangeFieldEf changeX = new ChangeFieldEf(
		//				fieldX,
		//				new MathOp("[0]", new BasicField<Integer>(e2,
		//						SceneElement.VAR_X)));
		//
		//		ChangeFieldEf changeY = new ChangeFieldEf(
		//				fieldY,
		//				new MathOp("[0]", new BasicField<Integer>(e2,
		//						SceneElement.VAR_Y)));

		//		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.DROP), changeX);
		//		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.DROP), changeY);
	}

}
