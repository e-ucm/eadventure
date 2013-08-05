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

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.shapes.BezierShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.assets.drawable.compounds.ComposedDrawable;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.effects.AddActorReferenceEf;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.enums.PhType;
import es.eucm.ead.model.elements.effects.physics.PhApplyImpulseEf;
import es.eucm.ead.model.elements.effects.physics.PhysicsEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.EAdSceneElementDef;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;
import ead.demos.elementfactories.EAdElementsFactory;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		this.setId("PhysicsScene");
		init();
	}

	protected void init() {
		setBackgroundFill(new LinearGradientFill(ColorFill.CYAN,
				ColorFill.BLUE, 800, 600));

		// Add sky

		addSky();

		PhysicsEf effect = new PhysicsEf();

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.ADDED, effect);

		// getEvents().add(event);
		getBackground().addBehavior(MouseGEv.MOUSE_ENTERED, effect);

		addCanyon(effect);
		addWalls(effect);
		// addPendulum(effect);
		addSea();
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing the use of jbox2d";
	}

	protected void addWalls(PhysicsEf effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setPaint(new LinearGradientFill(ColorFill.BROWN,
				ColorFill.DARK_BROWN, 750, 50));
		SceneElement ground = new SceneElement(groundS);
		ground.setPosition(new Position(Corner.CENTER, 400, 545));

		SceneElement wall = new SceneElement(groundS);
		wall.setPosition(new Position(Corner.CENTER, 775, 300));
		wall.setVarInitialValue(SceneElement.VAR_ROTATION, 90.0f);

		SceneElement wall2 = new SceneElement(groundS);
		wall2.setPosition(new Position(Corner.CENTER, 25, 300));
		wall2.setVarInitialValue(SceneElement.VAR_ROTATION, 90.0f);

		effect.addSceneElement(ground);
		getSceneElements().add(ground);
		effect.addSceneElement(wall);
		getSceneElements().add(wall);
		effect.addSceneElement(wall2);
		getSceneElements().add(wall2);

	}

	public String getDemoName() {
		return "Physics Scene";
	}

	private void addCanyon(PhysicsEf effect) {

		int height = 100;

		SceneElement canyon = new SceneElement(
				new Image("@drawable/canyon.png"));
		SceneElement canyonSupport = new SceneElement(new Image(
				"@drawable/canyonbottom.png"));

		canyon.setPosition(new Position(Corner.CENTER, 130, height));
		canyonSupport.setPosition(new Position(100, height));

		ComposedDrawable composed = new ComposedDrawable();
		composed.addDrawable(new RectangleShape(80, 600,
				new LinearGradientFill(ColorFill.BROWN, ColorFill.LIGHT_BROWN,
						80, 0)));
		composed.addDrawable(new Image("@drawable/grass.png"), 0, -15);

		SceneElement grass = new SceneElement(composed);
		grass.setPosition(90, height + 60);
		effect.getElements().add(grass);

		this.getSceneElements().add(grass);

		EAdField<Float> rotationField = new BasicField<Float>(canyon,
				SceneElement.VAR_ROTATION);

		ChangeFieldEf followMouse = new ChangeFieldEf();

		EAdField<Float> mouseX = SystemFields.MOUSE_X;
		EAdField<Float> mouseY = SystemFields.MOUSE_Y;
		EAdField<Float> canyonX = new BasicField<Float>(canyon,
				SceneElement.VAR_X);

		EAdField<Float> canyonY = new BasicField<Float>(canyon,
				SceneElement.VAR_Y);

		String expression = "deg( - acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) ) )";
		MathOp op = new MathOp(expression, canyonX, canyonY, mouseX, mouseY);
		followMouse.setOperation(op);
		followMouse.addField(rotationField);
		OperationCond c1 = new OperationCond(mouseX, 0,
				Comparator.GREATER_EQUAL);
		OperationCond c2 = new OperationCond(mouseY, new BasicField<Float>(
				canyon, SceneElement.VAR_Y), Comparator.LESS_EQUAL);
		OperationCond c3 = new OperationCond(mouseX, new BasicField<Float>(
				canyon, SceneElement.VAR_X), Comparator.GREATER_EQUAL);
		followMouse.setCondition(new ANDCond(c1, c2, c3));

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.ALWAYS, followMouse);
		canyon.getEvents().add(event);

		getSceneElements().add(canyon);
		getSceneElements().add(canyonSupport);

		// Bullet generation

		EAdShape circle = new CircleShape(10);
		circle.setPaint(new LinearGradientFill(ColorFill.LIGHT_GRAY,
				ColorFill.DARK_GRAY, 20, 20));
		EAdSceneElementDef bullet = new SceneElementDef(circle);

		PhApplyImpulseEf applyForce = new PhApplyImpulseEf();
		applyForce.setForce(new MathOp("([0] - [1]) * 500", mouseX, canyonX),
				new MathOp("([0] - [1]) * 500", mouseY, canyonY));
		AddActorReferenceEf addEffect = new AddActorReferenceEf(bullet,
				new Position(Corner.CENTER, 140, height - 5), applyForce);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, addEffect);

		// Add text
		Caption c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("radians: #0 ");
		c.getOperations().add(rotationField);
		c.setBubblePaint(ColorFill.TRANSPARENT);
		SceneElement e = new SceneElement(c);
		e.setPosition(120, 375);
		getSceneElements().add(e);

	}

	protected void addPendulum(PhysicsEf effect) {

		SceneElement holder = new SceneElement(new RectangleShape(100, 10,
				new LinearGradientFill(ColorFill.DARK_BROWN,
						ColorFill.LIGHT_BROWN, 100, 10)));
		holder.setPosition(new Position(Corner.CENTER, 400, 50));

		SceneElement rope = new SceneElement(new RectangleShape(150, 10,
				new LinearGradientFill(ColorFill.YELLOW, ColorFill.LIGHT_BROWN,
						150, 10)));
		rope.setPosition(new Position(Corner.CENTER, 450, 50));
		rope.setVarInitialValue(PhysicsEf.VAR_PH_TYPE, PhType.DYNAMIC);
		rope.setVarInitialValue(PhysicsEf.VAR_PH_FRICTION, 0.7f);
		getSceneElements().add(rope);
		getSceneElements().add(holder);

		effect.addJoint(holder, rope);

	}

	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.addAsset(SceneElementDef.appearance, new Image(
				"@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(new BasicField<Float>(
				getBackground(), SceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEvType.INIT, effect);

		this.getBackground().getEvents().add(event);

	}

	private void addSea() {
		SceneElement wave1 = new SceneElement(new Image("@drawable/wave1.png"));
		SceneElement wave2 = new SceneElement(new Image("@drawable/wave2.png"));
		GroupElement waves = new GroupElement();
		waves.getSceneElements().add(wave1);
		addGoal(waves);
		waves.getSceneElements().add(wave2);
		waves.setPosition(Position.Corner.BOTTOM_LEFT, -50, 600);

		this.getSceneElements().add(waves);

	}

	private void addGoal(GroupElement waves) {
		BezierShape shape = new BezierShape();
		shape.moveTo(0, 0);
		shape.lineTo(40, 0);
		shape.lineTo(35, 40);
		shape.lineTo(5, 40);
		shape.setClosed(true);
		shape.setPaint(ColorFill.RED);
		SceneElement goal = new SceneElement(shape);
		goal.setPosition(540, 0);
		waves.getSceneElements().add(goal);
	}

}
