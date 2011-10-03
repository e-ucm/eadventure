package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdAddActorReferenceEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemVars;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		init();
	}

	protected void init() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.CYAN, EAdColor.BLUE));

		// Add sky

		addSky();
		
		EAdBasicSceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement("GO Physics!", 400, 200);
		
		e.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 200));

		getElements().add(e);

		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2",
				new RectangleShape(10, 100, EAdColor.BROWN));
		getElements().add(e2);
		e2.setPosition(new EAdPositionImpl(Corner.CENTER, 500, 300));

		EAdPhysicsEffect effect = new EAdPhysicsEffect();
		effect.addSceneElement(e);
		effect.addSceneElement(e2);

		e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		e.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 6);
		e2.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);

		EAdConditionEvent event = new EAdConditionEventImpl();
		FlagCondition condition = new FlagCondition(new EAdFieldImpl<Boolean>(
				this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET, effect);

//		getEvents().add(event);
		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, effect);

		addCanyon(effect);
		addWalls(effect);
		addPendulum(effect);
	}

	@Override
	public String getDescription() {
		return "A scene showing the use of jbox2d";
	}

	protected void addWalls(EAdPhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(750, 50);
		groundS.setFill(new EAdLinearGradient(EAdColor.BROWN,
				EAdColor.DARK_BROWN));
		EAdBasicSceneElement ground = new EAdBasicSceneElement("ground",
				groundS);
		ground.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 545));

		EAdBasicSceneElement wall = new EAdBasicSceneElement("wall", groundS);
		wall.setPosition(new EAdPositionImpl(Corner.CENTER, 775, 300));
		wall.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		EAdBasicSceneElement wall2 = new EAdBasicSceneElement("wall", groundS);
		wall2.setPosition(new EAdPositionImpl(Corner.CENTER, 25, 300));
		wall2.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		effect.addSceneElement(ground);
		getElements().add(ground);
		effect.addSceneElement(wall);
		getElements().add(wall);
		effect.addSceneElement(wall2);
		getElements().add(wall2);

	}

	public String getDemoName() {
		return "Physics Scene";
	}

	private void addCanyon(EAdPhysicsEffect effect) {

		EAdBasicSceneElement canyon = new EAdBasicSceneElement("canyon",
				new ImageImpl("@drawable/canyon.png"));
		EAdBasicSceneElement canyonSupport = new EAdBasicSceneElement(
				"canyonSupport", new ImageImpl("@drawable/canyonbottom.png"));

		canyon.setPosition(new EAdPositionImpl(Corner.CENTER, 130, 475));
		canyonSupport.setPosition(new EAdPositionImpl(100, 475));

		EAdField<Float> rotationField = new EAdFieldImpl<Float>(canyon,
				EAdBasicSceneElement.VAR_ROTATION);

		EAdChangeFieldValueEffect followMouse = new EAdChangeFieldValueEffect(
				"followMouse");

		EAdField<Integer> mouseX = new EAdFieldImpl<Integer>(null,
				SystemVars.MOUSE_X);
		EAdField<Integer> mouseY = new EAdFieldImpl<Integer>(null,
				SystemVars.MOUSE_Y);
		EAdField<Integer> canyonX = new EAdFieldImpl<Integer>(canyon,
				EAdBasicSceneElement.VAR_X);

		EAdField<Integer> canyonY = new EAdFieldImpl<Integer>(canyon,
				EAdBasicSceneElement.VAR_Y);

		String expression = "- acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )";
		LiteralExpressionOperation op = new LiteralExpressionOperation(
				expression, canyonX, canyonY, mouseX, mouseY);
		followMouse.setOperation(op);
		followMouse.addField(rotationField);
		VarValCondition c1 = new VarValCondition(mouseX, 0,
				Operator.GREATER_EQUAL);
		VarVarCondition c2 = new VarVarCondition(mouseY,
				new EAdFieldImpl<Integer>(canyon, EAdBasicSceneElement.VAR_Y),
				Operator.LESS_EQUAL);
		VarVarCondition c3 = new VarVarCondition(mouseX,
				new EAdFieldImpl<Integer>(canyon, EAdBasicSceneElement.VAR_X),
				Operator.GREATER_EQUAL);
		followMouse.setCondition(new ANDCondition(c1, c2, c3));

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.addEffect(SceneElementEvent.ALWAYS, followMouse);
		canyon.getEvents().add(event);

		getElements().add(canyon);
		getElements().add(canyonSupport);

		// Bullet generation

		EAdActor bullet = new EAdBasicActor("bullet");
		BezierShape circle = new CircleShape(10, 10, 10, 25);
		circle.setFill(new EAdLinearGradient(EAdColor.LIGHT_GRAY,
				EAdColor.DARK_GRAY));
		bullet.getResources().addAsset(bullet.getInitialBundle(),
				EAdBasicSceneElement.appearance, circle);

		PhApplyImpluse applyForce = new PhApplyImpluse();
		applyForce.setForce(new LiteralExpressionOperation("[0] - [1]", mouseX,
				canyonX), new LiteralExpressionOperation("[0] - [1]", mouseY,
				canyonY));
		EAdAddActorReferenceEffect addEffect = new EAdAddActorReferenceEffect(
				bullet, new EAdPositionImpl(Corner.CENTER, 140, 470),
				applyForce);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_PRESSED,
				addEffect);

	}

	private void addPendulum(EAdPhysicsEffect effect) {

		EAdBasicSceneElement holder = new EAdBasicSceneElement("holder",
				new RectangleShape(100, 10, new EAdLinearGradient(
						EAdColor.DARK_BROWN, EAdColor.LIGHT_BROWN)));
		holder.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 50));

		EAdBasicSceneElement rope = new EAdBasicSceneElement("rope",
				new RectangleShape(150, 10, new EAdLinearGradient(
						EAdColor.YELLOW, EAdColor.LIGHT_BROWN)));

		rope.setPosition(new EAdPositionImpl(Corner.CENTER, 450, 50));
		rope.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		rope.setVarInitialValue(EAdPhysicsEffect.VAR_PH_FRICTION, 0.7f);
		getElements().add(rope);
		getElements().add(holder);

		effect.addJoint(holder, rope);

	}

	private void addSky() {
		getBackground()
				.getResources()
				.addAsset(this.getBackground().getInitialBundle(),
						EAdBasicSceneElement.appearance,
						new ImageImpl("@drawable/sky.png"));


		EAdSceneElementEvent event = new EAdSceneElementEventImpl();

		EAdVarInterpolationEffect effect = new EAdVarInterpolationEffect("sky");
		effect.setInterpolation(new EAdFieldImpl<Integer>(getBackground(),
				EAdBasicSceneElement.VAR_X), 0, -800, 100000, LoopType.REVERSE,
				InterpolationType.LINEAR);

		event.addEffect(EAdSceneElementEvent.SceneElementEvent.ADDED_TO_SCENE,
				effect);
		
		this.getBackground().getEvents().add(event);
		
	}

}
