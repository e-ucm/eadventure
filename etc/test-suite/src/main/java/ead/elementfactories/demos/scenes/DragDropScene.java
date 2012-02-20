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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.guievents.DragGEv;
import ead.common.model.elements.guievents.enums.DragGEvType;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.PaintFill;
import ead.common.resources.assets.drawable.basics.shapes.BallonShape;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;

public class DragDropScene extends EmptyScene {

	public DragDropScene() {
		setBackgroundFill(new LinearGradientFill(ColorFill.LIGHT_GRAY,
				new ColorFill(245, 255, 245), 800, 600));
		BezierShape shape = new BallonShape(0, 0, 100, 100,
				BalloonType.ROUNDED_RECTANGLE);
		shape.setPaint(new LinearGradientFill(ColorFill.RED, new ColorFill(200, 0,
				0), 100, 100));
		
		SceneElementDef def = new SceneElementDef( shape );
		
		
		
		SceneElement e1 = new SceneElement(def);
		e1.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e1.setPosition(new EAdPosition(Corner.CENTER, 600, 300));
		
		SceneElement e4 = new SceneElement(def);
		e4.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e4.setPosition(new EAdPosition(Corner.TOP_LEFT, 20, 20));
		e4.setInitialScale(0.5f);
		
		SceneElement e5 = new SceneElement(def);
		e5.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e5.setPosition(new EAdPosition(Corner.TOP_RIGHT, 500, 10));
		e5.setVarInitialValue(SceneElement.VAR_ROTATION, 0.5f);
		e5.setInitialScale(1.5f);
		
		
//		addComplexElement( );

		BezierShape shape2 = new BallonShape(0, 0, 110, 110,
				BalloonType.ROUNDED_RECTANGLE);
		shape2.setPaint(PaintFill.BLACK_ON_WHITE);
		SceneElement e2 = new SceneElement(shape2);
		e2.setPosition(new EAdPosition(Corner.CENTER, 100, 300));

		SceneElement e3 = new SceneElement(shape2);
		e3.setPosition(new EAdPosition(Corner.CENTER, 300, 300));

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
		changeScale1.setId("changeScale");
		ChangeFieldEf changeScale2 = new ChangeFieldEf( scale, new ValueOp(1.0f));
		changeScale2.setId("changeScale");
		e2.addBehavior(new DragGEv(e1.getDefinition(), DragGEvType.ENTERED),
				changeScale1);
		e2.addBehavior(new DragGEv(e1.getDefinition(), DragGEvType.EXITED),
				changeScale2);
		

		BasicField<Integer> fieldX = new BasicField<Integer>(e1, SceneElement.VAR_X);
		BasicField<Integer> fieldY = new BasicField<Integer>(e1, SceneElement.VAR_Y);

		ChangeFieldEf changeX = new ChangeFieldEf(
				fieldX,
				new MathOp("[0]", new BasicField<Integer>(e2,
						SceneElement.VAR_X)));
		changeX.setId("x");

		ChangeFieldEf changeY = new ChangeFieldEf(
				fieldY,
				new MathOp("[0]", new BasicField<Integer>(e2,
						SceneElement.VAR_Y)));
		changeY.setId("y");

//		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.DROP), changeX);
//		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.DROP), changeY);
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing drag and drop";
	}

	public String getDemoName() {
		return "Drag & Drop Scene";
	}

}
