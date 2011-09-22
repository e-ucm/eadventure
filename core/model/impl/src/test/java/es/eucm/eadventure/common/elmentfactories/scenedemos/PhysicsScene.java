package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarVarCondition;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyForce;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyActionType;
import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.model.guievents.impl.EAdKeyEventImpl;
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
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		init();
	}
	
	protected void init(){
		setBackgroundFill(new EAdLinearGradient(EAdColor.CYAN,
				EAdColor.BLUE));
		EAdBasicSceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement("GO Physics!", 400, 200);
		e.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 100));

		getSceneElements().add(e);

		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2", new RectangleShape(10, 100, EAdColor.BROWN));
		getSceneElements().add(e2);
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

		getEvents().add(event);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new PhApplyForce(e,
				new EAdPositionImpl(0, 100)));

		addCanon(effect);
		addWalls(effect);
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
		wall.setPosition(new EAdPositionImpl(Corner.CENTER, 775, 300 ));
		wall.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION, (float) Math.PI / 2.0f);
		
		EAdBasicSceneElement wall2 = new EAdBasicSceneElement("wall", groundS);
		wall2.setPosition(new EAdPositionImpl(Corner.CENTER, 25, 300 ));
		wall2.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION, (float) Math.PI / 2.0f);
		
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

	private void addCanon(EAdPhysicsEffect effect) {

		EAdBasicSceneElement canyon = new EAdBasicSceneElement("canyon",
				new ImageImpl("@drawable/canyon.png"));
		EAdBasicSceneElement canyonSupport = new EAdBasicSceneElement(
				"canyonSupport", new ImageImpl("@drawable/canyonbottom.png"));

		canyon.setPosition(45, 450);
		canyonSupport.setPosition(40, 480);

		getSceneElements().add(canyon);
		getSceneElements().add(canyonSupport);

		EAdField<Float> rotationField = new EAdFieldImpl<Float>(canyon,
				EAdBasicSceneElement.VAR_ROTATION);

		LiteralExpressionOperation op1 = new LiteralExpressionOperation(
				"[0] + 0.1", rotationField);

		EAdChangeFieldValueEffect goUpEffect = new EAdChangeFieldValueEffect(
				"goUp", rotationField, op1);

		LiteralExpressionOperation op2 = new LiteralExpressionOperation(
				"[0] - 0.1", rotationField);

		EAdChangeFieldValueEffect goDownEffect = new EAdChangeFieldValueEffect(
				"goDown", rotationField, op2);

		getBackground().addBehavior(
				new EAdKeyEventImpl(KeyActionType.KEY_PRESSED,
						KeyCode.ARROW_DOWN), goUpEffect);
		getBackground()
				.addBehavior(
						new EAdKeyEventImpl(KeyActionType.KEY_PRESSED,
								KeyCode.ARROW_UP), goDownEffect);

		EAdChangeFieldValueEffect followMouse = new EAdChangeFieldValueEffect(
				"followMouse");

		EAdField<Integer> mouseX = new EAdFieldImpl<Integer>(null,
				SystemVars.MOUSE_X);
		EAdField<Integer> mouseY = new EAdFieldImpl<Integer>(null,
				SystemVars.MOUSE_Y);

		String expression = "- acos( ( [2] - [0] ) / sqrt( sqr( [2] - [0] ) + sqr( [3] - [1] ) ) )";
		LiteralExpressionOperation op = new LiteralExpressionOperation(
				expression, new EAdFieldImpl<Integer>(canyon,
						EAdBasicSceneElement.VAR_X), new EAdFieldImpl<Integer>(
						canyon, EAdBasicSceneElement.VAR_Y), mouseX, mouseY);
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

	}

}
