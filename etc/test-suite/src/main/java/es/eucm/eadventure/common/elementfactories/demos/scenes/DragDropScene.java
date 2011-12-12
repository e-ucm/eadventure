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

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.enums.DragAction;
import es.eucm.eadventure.common.model.guievents.impl.EAdDragEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra.BalloonType;

public class DragDropScene extends EmptyScene {

	public DragDropScene() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.LIGHT_GRAY,
				new EAdColor(245, 255, 245), 800, 600));
		BezierShape shape = new BallonShape(0, 0, 100, 100,
				BalloonType.ROUNDED_RECTANGLE);
		shape.setPaint(new EAdLinearGradient(EAdColor.RED, new EAdColor(200, 0,
				0), 100, 100));
		
		EAdSceneElementDefImpl def = new EAdSceneElementDefImpl( shape );
		
		
		
		EAdBasicSceneElement e1 = new EAdBasicSceneElement(def);
		e1.setDragCond(EmptyCondition.TRUE_EMPTY_CONDITION);
		e1.setPosition(new EAdPositionImpl(Corner.CENTER, 600, 300));
		
		EAdBasicSceneElement e4 = new EAdBasicSceneElement(def);
		e4.setDragCond(EmptyCondition.TRUE_EMPTY_CONDITION);
		e4.setPosition(new EAdPositionImpl(Corner.TOP_LEFT, 20, 20));
		e4.setScale(0.5f);
		
		EAdBasicSceneElement e5 = new EAdBasicSceneElement(def);
		e5.setDragCond(EmptyCondition.TRUE_EMPTY_CONDITION);
		e5.setPosition(new EAdPositionImpl(Corner.TOP_RIGHT, 500, 10));
		e5.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION, 0.5f);
		e5.setScale(1.5f);
		
		
//		addComplexElement( );

		BezierShape shape2 = new BallonShape(0, 0, 110, 110,
				BalloonType.ROUNDED_RECTANGLE);
		shape2.setPaint(EAdPaintImpl.BLACK_ON_WHITE);
		EAdBasicSceneElement e2 = new EAdBasicSceneElement(shape2);
		e2.setPosition(new EAdPositionImpl(Corner.CENTER, 100, 300));

		EAdBasicSceneElement e3 = new EAdBasicSceneElement(shape2);
		e3.setPosition(new EAdPositionImpl(Corner.CENTER, 300, 300));

		addBehaviors(e2, e1);
		addBehaviors(e3, e1);

		getComponents().add(e2);
		getComponents().add(e3);
		getComponents().add(e1);
		getComponents().add(e4);
		getComponents().add(e5);

	}

	private void addBehaviors(EAdBasicSceneElement e2, EAdBasicSceneElement e1) {
		EAdField<Float> scale = new EAdFieldImpl<Float>(e2,
				EAdBasicSceneElement.VAR_SCALE);
		EAdChangeFieldValueEffect changeScale1 = new EAdChangeFieldValueEffect(scale, new ValueOperation(1.2f));
		changeScale1.setId("changeScale");
		EAdChangeFieldValueEffect changeScale2 = new EAdChangeFieldValueEffect( scale, new ValueOperation(1.0f));
		changeScale2.setId("changeScale");
		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.ENTERED),
				changeScale1);
		e2.addBehavior(new EAdDragEventImpl(e1.getDefinition(), DragAction.EXITED),
				changeScale2);
		

		EAdFieldImpl<Integer> fieldX = new EAdFieldImpl<Integer>(e1, EAdBasicSceneElement.VAR_X);
		EAdFieldImpl<Integer> fieldY = new EAdFieldImpl<Integer>(e1, EAdBasicSceneElement.VAR_Y);

		EAdChangeFieldValueEffect changeX = new EAdChangeFieldValueEffect(
				fieldX,
				new MathOperation("[0]", new EAdFieldImpl<Integer>(e2,
						EAdBasicSceneElement.VAR_X)));
		changeX.setId("x");

		EAdChangeFieldValueEffect changeY = new EAdChangeFieldValueEffect(
				fieldY,
				new MathOperation("[0]", new EAdFieldImpl<Integer>(e2,
						EAdBasicSceneElement.VAR_Y)));
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
