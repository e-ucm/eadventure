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

package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.impl.EAdAddActorReferenceEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.common.model.effects.impl.physics.PhType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		init();
	}

	protected void init() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.CYAN, EAdColor.BLUE,
				800, 600));

		// Add sky

		addSky();

		EAdPhysicsEffect effect = new EAdPhysicsEffect();

		EAdConditionEvent event = new EAdConditionEventImpl();
		OperationCondition condition = new OperationCondition(
				new EAdFieldImpl<Boolean>(this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEventType.CONDITIONS_MET, effect);

		// getEvents().add(event);
		getBackground()
				.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, effect);

		addCanyon(effect);
//		addWalls(effect);
//		addPendulum(effect);
		addSea();
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing the use of jbox2d";
	}

	protected void addWalls(EAdPhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setPaint(new EAdLinearGradient(EAdColor.BROWN,
				EAdColor.DARK_BROWN, 750, 50));
		EAdBasicSceneElement ground = new EAdBasicSceneElement(
				groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 545));

		EAdBasicSceneElement wall = new EAdBasicSceneElement( groundS);
		wall.setId("wall");
		wall.setPosition(new EAdPositionImpl(Corner.CENTER, 775, 300));
		wall.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		EAdBasicSceneElement wall2 = new EAdBasicSceneElement( groundS);
		wall2.setId("wall2");
		wall2.setPosition(new EAdPositionImpl(Corner.CENTER, 25, 300));
		wall2.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
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

	private void addCanyon(EAdPhysicsEffect effect) {
		
		int height = 100;

		EAdBasicSceneElement canyon = new EAdBasicSceneElement(
				new ImageImpl("@drawable/canyon.png"));
		canyon.setId("canyon");
		EAdBasicSceneElement canyonSupport = new EAdBasicSceneElement(
				 new ImageImpl("@drawable/canyonbottom.png"));
		canyonSupport.setId("canyonSupport");
		
		canyon.setPosition(new EAdPositionImpl(Corner.CENTER, 130, height));
		canyonSupport.setPosition(new EAdPositionImpl(100, height));
		
		ComposedDrawableImpl composed = new ComposedDrawableImpl();
		composed.addDrawable(new RectangleShape(80, 600, new EAdLinearGradient(EAdColor.BROWN, EAdColor.LIGHT_BROWN, 80, 0)));
		composed.addDrawable(new ImageImpl("@drawable/grass.png"), 0, -15);
		
		EAdBasicSceneElement grass = new EAdBasicSceneElement( composed);
		grass.setId("grass");
		grass.setPosition(90, height + 60);
		effect.getElements().add(grass);
		
		this.getComponents().add(grass);

		EAdField<Float> rotationField = new EAdFieldImpl<Float>(canyon,
				EAdBasicSceneElement.VAR_ROTATION);

		EAdChangeFieldValueEffect followMouse = new EAdChangeFieldValueEffect();
		followMouse.setId("followMouse");

		EAdField<Integer> mouseX = SystemFields.MOUSE_X;
		EAdField<Integer> mouseY = SystemFields.MOUSE_Y;
		EAdField<Integer> canyonX = new EAdFieldImpl<Integer>(canyon,
				EAdBasicSceneElement.VAR_X);

		EAdField<Integer> canyonY = new EAdFieldImpl<Integer>(canyon,
				EAdBasicSceneElement.VAR_Y);

		String expression = "- acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )";
		MathOperation op = new MathOperation(expression, canyonX, canyonY,
				mouseX, mouseY);
		followMouse.setOperation(op);
		followMouse.addField(rotationField);
		OperationCondition c1 = new OperationCondition(mouseX, 0,
				Comparator.GREATER_EQUAL);
		OperationCondition c2 = new OperationCondition(mouseY,
				new EAdFieldImpl<Integer>(canyon, EAdBasicSceneElement.VAR_Y),
				Comparator.LESS_EQUAL);
		OperationCondition c3 = new OperationCondition(mouseX,
				new EAdFieldImpl<Integer>(canyon, EAdBasicSceneElement.VAR_X),
				Comparator.GREATER_EQUAL);
		followMouse.setCondition(new ANDCondition(c1, c2, c3));

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.addEffect(SceneElementEventType.ALWAYS, followMouse);
		canyon.getEvents().add(event);

		getComponents().add(canyon);
		getComponents().add(canyonSupport);

		// Bullet generation

		BezierShape circle = new CircleShape(10, 10, 10, 25);
		circle.setPaint(new EAdLinearGradient(EAdColor.LIGHT_GRAY,
				EAdColor.DARK_GRAY, 20, 20));
		EAdSceneElementDef bullet = new EAdSceneElementDefImpl(circle);
		bullet.setId("bullet");

		PhApplyImpluse applyForce = new PhApplyImpluse();
		applyForce.setForce(new MathOperation("([0] - [1]) * 500", mouseX, canyonX),
				new MathOperation("([0] - [1]) * 500", mouseY, canyonY));
		EAdAddActorReferenceEffect addEffect = new EAdAddActorReferenceEffect(
				bullet, new EAdPositionImpl(Corner.CENTER, 140, height - 5),
				applyForce);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				addEffect);

		// Add text
		CaptionImpl c = EAdElementsFactory.getInstance().getCaptionFactory()
				.createCaption("radians: #0 ");
		c.getFields().add(rotationField);
		c.setBubblePaint(EAdColor.TRANSPARENT);
		EAdBasicSceneElement e = new EAdBasicSceneElement( c);
		e.setPosition(120, 375);
		getComponents().add(e);

	}

	protected void addPendulum(EAdPhysicsEffect effect) {

		EAdBasicSceneElement holder = new EAdBasicSceneElement(
				new RectangleShape(100, 10, new EAdLinearGradient(
						EAdColor.DARK_BROWN, EAdColor.LIGHT_BROWN, 100, 10)));
		holder.setId("holder");
		holder.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 50));

		EAdBasicSceneElement rope = new EAdBasicSceneElement(
				new RectangleShape(150, 10, new EAdLinearGradient(
						EAdColor.YELLOW, EAdColor.LIGHT_BROWN, 150, 10)));
		rope.setId("rope");
		rope.setPosition(new EAdPositionImpl(Corner.CENTER, 450, 50));
		rope.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		rope.setVarInitialValue(EAdPhysicsEffect.VAR_PH_FRICTION, 0.7f);
		getComponents().add(rope);
		getComponents().add(holder);

		effect.addJoint(holder, rope);

	}

	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.getResources().addAsset(
				backgroundDef.getInitialBundle(),
				EAdSceneElementDefImpl.appearance,
				new ImageImpl("@drawable/sky.png"));

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();

		EAdInterpolationEffect effect = new EAdInterpolationEffect(
				new EAdFieldImpl<Integer>(getBackground(),
						EAdBasicSceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEventType.ADDED_TO_SCENE,
				effect);

		this.getBackground().getEvents().add(event);

	}
	
	private void addSea(){
		EAdBasicSceneElement wave1 = new EAdBasicSceneElement( new ImageImpl("@drawable/wave1.png"));
		wave1.setId("wave1");
		EAdBasicSceneElement wave2 = new EAdBasicSceneElement(new ImageImpl("@drawable/wave2.png"));
		wave2.setId("wave2");
		EAdComplexElementImpl waves = new EAdComplexElementImpl();
		waves.setId("waves");
		waves.getComponents().add(wave1);
		addGoal(waves);
		waves.getComponents().add(wave2);
		waves.setPosition(EAdPositionImpl.Corner.BOTTOM_LEFT, -50, 600);
		
		this.getComponents().add(waves);
		
	}
	
	private void addGoal(EAdComplexElementImpl waves){
		BezierShape shape = new BezierShape();
		shape.moveTo(0, 0);
		shape.lineTo(40, 0);
		shape.lineTo(35, 40);
		shape.lineTo(5, 40);
		shape.setClosed(true);
		shape.setPaint(EAdColor.RED);
		EAdBasicSceneElement goal = new EAdBasicSceneElement( shape);
		goal.setId("goal");
		goal.setPosition(540, 0);
		waves.getComponents().add(goal);
	}

}
