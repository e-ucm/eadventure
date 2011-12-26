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

import es.eucm.eadventure.common.model.elements.conditions.OperationCond;
import es.eucm.eadventure.common.model.elements.effects.enums.PhShape;
import es.eucm.eadventure.common.model.elements.effects.enums.PhType;
import es.eucm.eadventure.common.model.elements.effects.physics.PhApplyImpluseEf;
import es.eucm.eadventure.common.model.elements.effects.physics.PhysicsEffect;
import es.eucm.eadventure.common.model.elements.events.ConditionedEv;
import es.eucm.eadventure.common.model.elements.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.MathOp;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.eadventure.common.util.EAdPosition;
import es.eucm.eadventure.common.util.EAdPosition.Corner;

public class PhysicsScene2 extends PhysicsScene {

	public PhysicsScene2() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.YELLOW, EAdColor.ORANGE, 800, 600));
	}

	protected void init() {

		RectangleShape rShape = new RectangleShape(10, 100, EAdColor.BROWN);

		SceneElementImpl e2 = new SceneElementImpl(rShape);
		e2.setId("e2");
		getComponents().add(e2);
		e2.setPosition(new EAdPosition(Corner.CENTER, 500, 200));
		e2.setVarInitialValue(SceneElementImpl.VAR_ROTATION,
				(float) Math.PI / 4.0f);

		PhysicsEffect effect = new PhysicsEffect();
		effect.addSceneElement(e2);

		
		SceneElementImpl e3 = new SceneElementImpl( rShape);
		e3.setId("e3");
		getComponents().add(e3);
		e3.setPosition(new EAdPosition(Corner.CENTER, 200, 100));
		e3.setVarInitialValue(SceneElementImpl.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		effect.addSceneElement(e3);

		BezierShape circle = new CircleShape(20, 20, 20, 60);
		circle.setPaint(new EAdLinearGradient(EAdColor.GREEN,
				new EAdColor(0, 100, 0), 40, 40));

		SceneElementImpl b = new SceneElementImpl( circle);
		b.setId("ball");
		b.setPosition(new EAdPosition(Corner.CENTER, 500, 0));
		getComponents().add(b, 0);
		effect.addSceneElement(b);
		b.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		getBackground().addBehavior(
				EAdMouseEvent.MOUSE_LEFT_CLICK,
				new PhApplyImpluseEf(b, new MathOp(
						 "0"), new MathOp(
						 "-100")));
		b.setVarInitialValue(PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
		b.setVarInitialValue(PhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				SceneElementImpl e = new SceneElementImpl( circle);
				e.setId("ball" + i + "_" + j);
				e.setPosition(new EAdPosition(Corner.CENTER, i * 60 + 200,
						j * 60 + 200));
				getComponents().add(e);
				effect.addSceneElement(e);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE,
						PhType.DYNAMIC);
				getBackground().addBehavior(
						EAdMouseEvent.MOUSE_LEFT_CLICK,
						new PhApplyImpluseEf(e, new MathOp(
								 "0"),
								new MathOp(
										"-100")));
				e.setVarInitialValue(PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_SHAPE,
						PhShape.CIRCULAR);
			}

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(new FieldImpl<Boolean>(
				this, SceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEventType.CONDITIONS_MET, effect);

		getEvents().add(event);
		
		//getBackground().addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, effect);

		addWalls(effect);
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing the use of jbox2d";
	}

	public String getDemoName() {
		return "Physics Scene 2";
	}

}
