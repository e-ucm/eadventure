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

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.enums.PhShape;
import ead.common.model.elements.effects.enums.PhType;
import ead.common.model.elements.effects.physics.PhApplyImpluseEf;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;

public class PhysicsScene2 extends PhysicsScene {

	public PhysicsScene2() {
		setBackgroundFill(new LinearGradientFill(ColorFill.YELLOW, ColorFill.ORANGE, 800, 600));
	}

	protected void init() {

		RectangleShape rShape = new RectangleShape(10, 100, ColorFill.BROWN);

		SceneElement e2 = new SceneElement(rShape);
		e2.setId("e2");
		getSceneElements().add(e2);
		e2.setPosition(new EAdPosition(Corner.CENTER, 500, 200));
		e2.setVarInitialValue(SceneElement.VAR_ROTATION,
				(float) Math.PI / 4.0f);

		PhysicsEffect effect = new PhysicsEffect();
		effect.addSceneElement(e2);

		
		SceneElement e3 = new SceneElement( rShape);
		e3.setId("e3");
		getSceneElements().add(e3);
		e3.setPosition(new EAdPosition(Corner.CENTER, 200, 100));
		e3.setVarInitialValue(SceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		effect.addSceneElement(e3);

		BezierShape circle = new CircleShape(20, 20, 20, 60);
		circle.setPaint(new LinearGradientFill(ColorFill.GREEN,
				new ColorFill(0, 100, 0), 40, 40));

		SceneElement b = new SceneElement( circle);
		b.setId("ball");
		b.setPosition(new EAdPosition(Corner.CENTER, 500, 0));
		getSceneElements().add(b, 0);
		effect.addSceneElement(b);
		b.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		getBackground().addBehavior(
				MouseGEv.MOUSE_LEFT_CLICK,
				new PhApplyImpluseEf(b, new MathOp(
						 "0"), new MathOp(
						 "-100")));
		b.setVarInitialValue(PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
		b.setVarInitialValue(PhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				SceneElement e = new SceneElement( circle);
				e.setId("ball" + i + "_" + j);
				e.setPosition(new EAdPosition(Corner.CENTER, i * 60 + 200,
						j * 60 + 200));
				getSceneElements().add(e);
				effect.addSceneElement(e);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE,
						PhType.DYNAMIC);
				getBackground().addBehavior(
						MouseGEv.MOUSE_LEFT_CLICK,
						new PhApplyImpluseEf(e, new MathOp(
								 "0"),
								new MathOp(
										"-100")));
				e.setVarInitialValue(PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_SHAPE,
						PhShape.CIRCULAR);
			}

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(new BasicField<Boolean>(
				this, BasicScene.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, effect);

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
