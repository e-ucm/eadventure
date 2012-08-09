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

import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.AddActorReferenceEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.enums.PhType;
import ead.common.model.elements.effects.physics.PhApplyImpluseEf;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.EAdElementsFactory;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		init();
	}

	protected void init() {
		setId("PhysicsScene");
		setBackgroundFill(new LinearGradientFill(ColorFill.CYAN, ColorFill.BLUE,
				800, 600));

		// Add sky

		addSky();

		PhysicsEffect effect = new PhysicsEffect();

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(
				new BasicField<Boolean>(this, BasicScene.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, effect);

		// getEvents().add(event);
		getBackground()
				.addBehavior(MouseGEv.MOUSE_ENTERED, effect);

		addCanyon(effect);
    	addWalls(effect);
//		addPendulum(effect);
		addSea();
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing the use of jbox2d";
	}

	protected void addWalls(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setPaint(new LinearGradientFill(ColorFill.BROWN,
				ColorFill.DARK_BROWN, 750, 50));
		SceneElement ground = new SceneElement(
				groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 545));

		SceneElement wall = new SceneElement( groundS);
		wall.setId("wall");
		wall.setPosition(new EAdPosition(Corner.CENTER, 775, 300));
		wall.setVarInitialValue(SceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		SceneElement wall2 = new SceneElement( groundS);
		wall2.setId("wall2");
		wall2.setPosition(new EAdPosition(Corner.CENTER, 25, 300));
		wall2.setVarInitialValue(SceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

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

	private void addCanyon(PhysicsEffect effect) {
		
		int height = 100;

		SceneElement canyon = new SceneElement(
				new Image("@drawable/canyon.png"));
		canyon.setId("canyon");
		SceneElement canyonSupport = new SceneElement(
				 new Image("@drawable/canyonbottom.png"));
		canyonSupport.setId("canyonSupport");
		
		canyon.setPosition(new EAdPosition(Corner.CENTER, 130, height));
		canyonSupport.setPosition(new EAdPosition(100, height));
		
		ComposedDrawable composed = new ComposedDrawable();
		composed.addDrawable(new RectangleShape(80, 600, new LinearGradientFill(ColorFill.BROWN, ColorFill.LIGHT_BROWN, 80, 0)));
		composed.addDrawable(new Image("@drawable/grass.png"), 0, -15);
		
		SceneElement grass = new SceneElement( composed);
		grass.setId("grass");
		grass.setPosition(90, height + 60);
		effect.getElements().add(grass);
		
		this.getSceneElements().add(grass);

		EAdField<Float> rotationField = new BasicField<Float>(canyon,
				SceneElement.VAR_ROTATION);

		ChangeFieldEf followMouse = new ChangeFieldEf();
		followMouse.setId("followMouse");

		EAdField<Integer> mouseX = SystemFields.MOUSE_X;
		EAdField<Integer> mouseY = SystemFields.MOUSE_Y;
		EAdField<Integer> canyonX = new BasicField<Integer>(canyon,
				SceneElement.VAR_X);

		EAdField<Integer> canyonY = new BasicField<Integer>(canyon,
				SceneElement.VAR_Y);

		String expression = "- acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )";
		MathOp op = new MathOp(expression, canyonX, canyonY,
				mouseX, mouseY);
		followMouse.setOperation(op);
		followMouse.addField(rotationField);
		OperationCond c1 = new OperationCond(mouseX, 0,
				Comparator.GREATER_EQUAL);
		OperationCond c2 = new OperationCond(mouseY,
				new BasicField<Integer>(canyon, SceneElement.VAR_Y),
				Comparator.LESS_EQUAL);
		OperationCond c3 = new OperationCond(mouseX,
				new BasicField<Integer>(canyon, SceneElement.VAR_X),
				Comparator.GREATER_EQUAL);
		followMouse.setCondition(new ANDCond(c1, c2, c3));

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.ALWAYS, followMouse);
		canyon.getEvents().add(event);

		getSceneElements().add(canyon);
		getSceneElements().add(canyonSupport);

		// Bullet generation

		BezierShape circle = new CircleShape(10, 10, 10, 25);
		circle.setPaint(new LinearGradientFill(ColorFill.LIGHT_GRAY,
				ColorFill.DARK_GRAY, 20, 20));
		EAdSceneElementDef bullet = new SceneElementDef(circle);
		bullet.setId("bullet");

		PhApplyImpluseEf applyForce = new PhApplyImpluseEf();
		applyForce.setForce(new MathOp("([0] - [1]) * 500", mouseX, canyonX),
				new MathOp("([0] - [1]) * 500", mouseY, canyonY));
		AddActorReferenceEf addEffect = new AddActorReferenceEf(
				bullet, new EAdPosition(Corner.CENTER, 140, height - 5),
				applyForce);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				addEffect);

		// Add text
		Caption c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("radians: #0 ");
		c.getFields().add(rotationField);
		c.setBubblePaint(ColorFill.TRANSPARENT);
		SceneElement e = new SceneElement( c);
		e.setPosition(120, 375);
		getSceneElements().add(e);

	}

	protected void addPendulum(PhysicsEffect effect) {

		SceneElement holder = new SceneElement(
				new RectangleShape(100, 10, new LinearGradientFill(
						ColorFill.DARK_BROWN, ColorFill.LIGHT_BROWN, 100, 10)));
		holder.setId("holder");
		holder.setPosition(new EAdPosition(Corner.CENTER, 400, 50));

		SceneElement rope = new SceneElement(
				new RectangleShape(150, 10, new LinearGradientFill(
						ColorFill.YELLOW, ColorFill.LIGHT_BROWN, 150, 10)));
		rope.setId("rope");
		rope.setPosition(new EAdPosition(Corner.CENTER, 450, 50));
		rope.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		rope.setVarInitialValue(PhysicsEffect.VAR_PH_FRICTION, 0.7f);
		getSceneElements().add(rope);
		getSceneElements().add(holder);

		effect.addJoint(holder, rope);

	}

	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.getResources().addAsset(
				backgroundDef.getInitialBundle(),
				SceneElementDef.appearance,
				new Image("@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(
				new BasicField<Integer>(getBackground(),
						SceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEvType.FIRST_UPDATE,
				effect);

		this.getBackground().getEvents().add(event);

	}
	
	private void addSea(){
		SceneElement wave1 = new SceneElement( new Image("@drawable/wave1.png"));
		wave1.setId("wave1");
		SceneElement wave2 = new SceneElement(new Image("@drawable/wave2.png"));
		wave2.setId("wave2");
		ComplexSceneElement waves = new ComplexSceneElement();
		waves.setId("waves");
		waves.getSceneElements().add(wave1);
		addGoal(waves);
		waves.getSceneElements().add(wave2);
		waves.setPosition(EAdPosition.Corner.BOTTOM_LEFT, -50, 600);
		
		this.getSceneElements().add(waves);
		
	}
	
	private void addGoal(ComplexSceneElement waves){
		BezierShape shape = new BezierShape();
		shape.moveTo(0, 0);
		shape.lineTo(40, 0);
		shape.lineTo(35, 40);
		shape.lineTo(5, 40);
		shape.setClosed(true);
		shape.setPaint(ColorFill.RED);
		SceneElement goal = new SceneElement( shape);
		goal.setId("goal");
		goal.setPosition(540, 0);
		waves.getSceneElements().add(goal);
	}

}
