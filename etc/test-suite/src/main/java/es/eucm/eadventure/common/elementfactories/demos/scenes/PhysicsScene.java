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

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.conditions.ANDCond;
import es.eucm.eadventure.common.model.elements.conditions.OperationCond;
import es.eucm.eadventure.common.model.elements.conditions.enums.Comparator;
import es.eucm.eadventure.common.model.elements.effects.AddActorReferenceEf;
import es.eucm.eadventure.common.model.elements.effects.InterpolationEf;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.elements.effects.enums.InterpolationType;
import es.eucm.eadventure.common.model.elements.effects.enums.PhType;
import es.eucm.eadventure.common.model.elements.effects.physics.PhApplyImpluseEf;
import es.eucm.eadventure.common.model.elements.effects.physics.PhysicsEffect;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.events.ConditionedEv;
import es.eucm.eadventure.common.model.elements.events.SceneElementEv;
import es.eucm.eadventure.common.model.elements.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.elements.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.scenes.ComplexSceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.SystemFields;
import es.eucm.eadventure.common.model.elements.variables.operations.MathOp;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawableImpl;
import es.eucm.eadventure.common.util.EAdPositionImpl;
import es.eucm.eadventure.common.util.EAdPositionImpl.Corner;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		init();
	}

	protected void init() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.CYAN, EAdColor.BLUE,
				800, 600));

		// Add sky

		addSky();

		PhysicsEffect effect = new PhysicsEffect();

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(
				new FieldImpl<Boolean>(this, SceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEventType.CONDITIONS_MET, effect);

		// getEvents().add(event);
		getBackground()
				.addBehavior(MouseEventImpl.MOUSE_ENTERED, effect);

		addCanyon(effect);
//		addWalls(effect);
//		addPendulum(effect);
		addSea();
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing the use of jbox2d";
	}

	protected void addWalls(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setPaint(new EAdLinearGradient(EAdColor.BROWN,
				EAdColor.DARK_BROWN, 750, 50));
		SceneElementImpl ground = new SceneElementImpl(
				groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 545));

		SceneElementImpl wall = new SceneElementImpl( groundS);
		wall.setId("wall");
		wall.setPosition(new EAdPositionImpl(Corner.CENTER, 775, 300));
		wall.setVarInitialValue(SceneElementImpl.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		SceneElementImpl wall2 = new SceneElementImpl( groundS);
		wall2.setId("wall2");
		wall2.setPosition(new EAdPositionImpl(Corner.CENTER, 25, 300));
		wall2.setVarInitialValue(SceneElementImpl.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		effect.addSceneElement(ground);
		getComponents().add(ground);
		effect.addSceneElement(wall);
		getComponents().add(wall);
		effect.addSceneElement(wall2);
		getComponents().add(wall2);

	}

	public String getDemoName() {
		return "Physics Scene";
	}

	private void addCanyon(PhysicsEffect effect) {
		
		int height = 100;

		SceneElementImpl canyon = new SceneElementImpl(
				new ImageImpl("@drawable/canyon.png"));
		canyon.setId("canyon");
		SceneElementImpl canyonSupport = new SceneElementImpl(
				 new ImageImpl("@drawable/canyonbottom.png"));
		canyonSupport.setId("canyonSupport");
		
		canyon.setPosition(new EAdPositionImpl(Corner.CENTER, 130, height));
		canyonSupport.setPosition(new EAdPositionImpl(100, height));
		
		ComposedDrawableImpl composed = new ComposedDrawableImpl();
		composed.addDrawable(new RectangleShape(80, 600, new EAdLinearGradient(EAdColor.BROWN, EAdColor.LIGHT_BROWN, 80, 0)));
		composed.addDrawable(new ImageImpl("@drawable/grass.png"), 0, -15);
		
		SceneElementImpl grass = new SceneElementImpl( composed);
		grass.setId("grass");
		grass.setPosition(90, height + 60);
		effect.getElements().add(grass);
		
		this.getComponents().add(grass);

		EAdField<Float> rotationField = new FieldImpl<Float>(canyon,
				SceneElementImpl.VAR_ROTATION);

		ChangeFieldEf followMouse = new ChangeFieldEf();
		followMouse.setId("followMouse");

		EAdField<Integer> mouseX = SystemFields.MOUSE_X;
		EAdField<Integer> mouseY = SystemFields.MOUSE_Y;
		EAdField<Integer> canyonX = new FieldImpl<Integer>(canyon,
				SceneElementImpl.VAR_X);

		EAdField<Integer> canyonY = new FieldImpl<Integer>(canyon,
				SceneElementImpl.VAR_Y);

		String expression = "- acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )";
		MathOp op = new MathOp(expression, canyonX, canyonY,
				mouseX, mouseY);
		followMouse.setOperation(op);
		followMouse.addField(rotationField);
		OperationCond c1 = new OperationCond(mouseX, 0,
				Comparator.GREATER_EQUAL);
		OperationCond c2 = new OperationCond(mouseY,
				new FieldImpl<Integer>(canyon, SceneElementImpl.VAR_Y),
				Comparator.LESS_EQUAL);
		OperationCond c3 = new OperationCond(mouseX,
				new FieldImpl<Integer>(canyon, SceneElementImpl.VAR_X),
				Comparator.GREATER_EQUAL);
		followMouse.setCondition(new ANDCond(c1, c2, c3));

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEventType.ALWAYS, followMouse);
		canyon.getEvents().add(event);

		getComponents().add(canyon);
		getComponents().add(canyonSupport);

		// Bullet generation

		BezierShape circle = new CircleShape(10, 10, 10, 25);
		circle.setPaint(new EAdLinearGradient(EAdColor.LIGHT_GRAY,
				EAdColor.DARK_GRAY, 20, 20));
		EAdSceneElementDef bullet = new SceneElementDefImpl(circle);
		bullet.setId("bullet");

		PhApplyImpluseEf applyForce = new PhApplyImpluseEf();
		applyForce.setForce(new MathOp("([0] - [1]) * 500", mouseX, canyonX),
				new MathOp("([0] - [1]) * 500", mouseY, canyonY));
		AddActorReferenceEf addEffect = new AddActorReferenceEf(
				bullet, new EAdPositionImpl(Corner.CENTER, 140, height - 5),
				applyForce);

		getBackground().addBehavior(MouseEventImpl.MOUSE_LEFT_PRESSED,
				addEffect);

		// Add text
		CaptionImpl c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("radians: #0 ");
		c.getFields().add(rotationField);
		c.setBubblePaint(EAdColor.TRANSPARENT);
		SceneElementImpl e = new SceneElementImpl( c);
		e.setPosition(120, 375);
		getComponents().add(e);

	}

	protected void addPendulum(PhysicsEffect effect) {

		SceneElementImpl holder = new SceneElementImpl(
				new RectangleShape(100, 10, new EAdLinearGradient(
						EAdColor.DARK_BROWN, EAdColor.LIGHT_BROWN, 100, 10)));
		holder.setId("holder");
		holder.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 50));

		SceneElementImpl rope = new SceneElementImpl(
				new RectangleShape(150, 10, new EAdLinearGradient(
						EAdColor.YELLOW, EAdColor.LIGHT_BROWN, 150, 10)));
		rope.setId("rope");
		rope.setPosition(new EAdPositionImpl(Corner.CENTER, 450, 50));
		rope.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		rope.setVarInitialValue(PhysicsEffect.VAR_PH_FRICTION, 0.7f);
		getComponents().add(rope);
		getComponents().add(holder);

		effect.addJoint(holder, rope);

	}

	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.getResources().addAsset(
				backgroundDef.getInitialBundle(),
				SceneElementDefImpl.appearance,
				new ImageImpl("@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(
				new FieldImpl<Integer>(getBackground(),
						SceneElementImpl.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEventType.ADDED_TO_SCENE,
				effect);

		this.getBackground().getEvents().add(event);

	}
	
	private void addSea(){
		SceneElementImpl wave1 = new SceneElementImpl( new ImageImpl("@drawable/wave1.png"));
		wave1.setId("wave1");
		SceneElementImpl wave2 = new SceneElementImpl(new ImageImpl("@drawable/wave2.png"));
		wave2.setId("wave2");
		ComplexSceneElementImpl waves = new ComplexSceneElementImpl();
		waves.setId("waves");
		waves.getComponents().add(wave1);
		addGoal(waves);
		waves.getComponents().add(wave2);
		waves.setPosition(EAdPositionImpl.Corner.BOTTOM_LEFT, -50, 600);
		
		this.getComponents().add(waves);
		
	}
	
	private void addGoal(ComplexSceneElementImpl waves){
		BezierShape shape = new BezierShape();
		shape.moveTo(0, 0);
		shape.lineTo(40, 0);
		shape.lineTo(35, 40);
		shape.lineTo(5, 40);
		shape.setClosed(true);
		shape.setPaint(EAdColor.RED);
		SceneElementImpl goal = new SceneElementImpl( shape);
		goal.setId("goal");
		goal.setPosition(540, 0);
		waves.getComponents().add(goal);
	}

}
