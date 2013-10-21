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

package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.effects.enums.PhShape;
import es.eucm.ead.model.elements.effects.enums.PhType;
import es.eucm.ead.model.elements.effects.physics.PhApplyImpulseEf;
import es.eucm.ead.model.elements.effects.physics.PhysicsEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;

public class PhysicsScene2 extends PhysicsScene {

	public PhysicsScene2() {
		this.setId("PhysicsScene2");
		setBackgroundFill(new LinearGradientFill(ColorFill.YELLOW,
				ColorFill.ORANGE, 800, 600));
	}

	protected void init() {

		RectangleShape rShape = new RectangleShape(10, 100, ColorFill.BROWN);

		SceneElement e2 = new SceneElement(rShape);
		getSceneElements().add(e2);
		e2.setPosition(new Position(Corner.CENTER, 500, 200));
		e2.setVar(SceneElement.VAR_ROTATION, 45.0f);

		PhysicsEf effect = new PhysicsEf();
		effect.addSceneElement(e2);

		SceneElement e3 = new SceneElement(rShape);
		getSceneElements().add(e3);
		e3.setPosition(new Position(Corner.CENTER, 200, 100));
		e3.setVar(SceneElement.VAR_ROTATION, 90.0f);

		effect.addSceneElement(e3);

		EAdShape circle = new CircleShape(20);
		circle.setPaint(new LinearGradientFill(ColorFill.GREEN, new ColorFill(
				0, 100, 0), 40, 40));

		SceneElement b = new SceneElement(circle);
		b.setPosition(new Position(Corner.CENTER, 500, 0));
		getSceneElements().add(0, b);
		effect.addSceneElement(b);
		b.setVar(PhysicsEf.VAR_PH_TYPE, PhType.DYNAMIC);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				new PhApplyImpulseEf(b, new MathOp("0"), new MathOp("-1")));
		b.setVar(PhysicsEf.VAR_PH_RESTITUTION, 0.3f);
		b.setVar(PhysicsEf.VAR_PH_SHAPE, PhShape.CIRCULAR);

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				SceneElement e = new SceneElement(circle);
				e.setPosition(new Position(Corner.CENTER, i * 60 + 200,
						j * 60 + 200));
				getSceneElements().add(e);
				effect.addSceneElement(e);
				e.setVar(PhysicsEf.VAR_PH_TYPE, PhType.DYNAMIC);
				getBackground().addBehavior(
						MouseGEv.MOUSE_LEFT_PRESSED,
						new PhApplyImpulseEf(e, new MathOp("0"), new MathOp(
								"-100")));
				e.setVar(PhysicsEf.VAR_PH_RESTITUTION, 0.3f);
				e.setVar(PhysicsEf.VAR_PH_SHAPE, PhShape.CIRCULAR);
			}

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.ADDED, effect);

		addEvent(event);

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
