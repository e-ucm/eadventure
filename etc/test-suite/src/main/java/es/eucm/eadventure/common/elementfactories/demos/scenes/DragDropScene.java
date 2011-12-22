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

package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.model.elements.conditions.EmptyCond;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.guievents.DragEventImpl;
import es.eucm.eadventure.common.model.elements.guievents.enums.DragAction;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.MathOp;
import es.eucm.eadventure.common.model.elements.variables.operations.ValueOp;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import es.eucm.eadventure.common.util.EAdPositionImpl;
import es.eucm.eadventure.common.util.EAdPositionImpl.Corner;

public class DragDropScene extends EmptyScene {

	public DragDropScene() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.LIGHT_GRAY,
				new EAdColor(245, 255, 245), 800, 600));
		BezierShape shape = new BallonShape(0, 0, 100, 100,
				BalloonType.ROUNDED_RECTANGLE);
		shape.setPaint(new EAdLinearGradient(EAdColor.RED, new EAdColor(200, 0,
				0), 100, 100));
		
		SceneElementDefImpl def = new SceneElementDefImpl( shape );
		
		
		
		SceneElementImpl e1 = new SceneElementImpl(def);
		e1.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e1.setPosition(new EAdPositionImpl(Corner.CENTER, 600, 300));
		
		SceneElementImpl e4 = new SceneElementImpl(def);
		e4.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e4.setPosition(new EAdPositionImpl(Corner.TOP_LEFT, 20, 20));
		e4.setScale(0.5f);
		
		SceneElementImpl e5 = new SceneElementImpl(def);
		e5.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		e5.setPosition(new EAdPositionImpl(Corner.TOP_RIGHT, 500, 10));
		e5.setVarInitialValue(SceneElementImpl.VAR_ROTATION, 0.5f);
		e5.setScale(1.5f);
		
		
//		addComplexElement( );

		BezierShape shape2 = new BallonShape(0, 0, 110, 110,
				BalloonType.ROUNDED_RECTANGLE);
		shape2.setPaint(EAdPaintImpl.BLACK_ON_WHITE);
		SceneElementImpl e2 = new SceneElementImpl(shape2);
		e2.setPosition(new EAdPositionImpl(Corner.CENTER, 100, 300));

		SceneElementImpl e3 = new SceneElementImpl(shape2);
		e3.setPosition(new EAdPositionImpl(Corner.CENTER, 300, 300));

		addBehaviors(e2, e1);
		addBehaviors(e3, e1);

		getComponents().add(e2);
		getComponents().add(e3);
		getComponents().add(e1);
		getComponents().add(e4);
		getComponents().add(e5);

	}

	private void addBehaviors(SceneElementImpl e2, SceneElementImpl e1) {
		EAdField<Float> scale = new FieldImpl<Float>(e2,
				SceneElementImpl.VAR_SCALE);
		ChangeFieldEf changeScale1 = new ChangeFieldEf(scale, new ValueOp(1.2f));
		changeScale1.setId("changeScale");
		ChangeFieldEf changeScale2 = new ChangeFieldEf( scale, new ValueOp(1.0f));
		changeScale2.setId("changeScale");
		e2.addBehavior(new DragEventImpl(e1.getDefinition(), DragAction.ENTERED),
				changeScale1);
		e2.addBehavior(new DragEventImpl(e1.getDefinition(), DragAction.EXITED),
				changeScale2);
		

		FieldImpl<Integer> fieldX = new FieldImpl<Integer>(e1, SceneElementImpl.VAR_X);
		FieldImpl<Integer> fieldY = new FieldImpl<Integer>(e1, SceneElementImpl.VAR_Y);

		ChangeFieldEf changeX = new ChangeFieldEf(
				fieldX,
				new MathOp("[0]", new FieldImpl<Integer>(e2,
						SceneElementImpl.VAR_X)));
		changeX.setId("x");

		ChangeFieldEf changeY = new ChangeFieldEf(
				fieldY,
				new MathOp("[0]", new FieldImpl<Integer>(e2,
						SceneElementImpl.VAR_Y)));
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
