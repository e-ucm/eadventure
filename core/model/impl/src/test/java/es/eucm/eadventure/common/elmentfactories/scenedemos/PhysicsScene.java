package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyForce;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyActionType;
import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.model.guievents.impl.EAdKeyEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		this.setBackgroundFill(new EAdLinearGradient(EAdColor.BLUE,
				EAdColor.CYAN));
		EAdBasicSceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement("GO Physics!", 400, 200);
		e.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 100));

		RectangleShape groundS = new RectangleShape(750, 20);
		groundS.setFill(new EAdLinearGradient(EAdColor.BROWN,
				EAdColor.DARK_BROWN));
		EAdBasicSceneElement ground = new EAdBasicSceneElement("ground",
				groundS);
		ground.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 545));

		getSceneElements().add(e);
		
		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2", new ImageImpl("@drawable/canyon.png"));
		getSceneElements().add(e2);
		e2.setPosition(new EAdPositionImpl( Corner.CENTER, 500, 300));

		EAdPhysicsEffect effect = new EAdPhysicsEffect();
		effect.addSceneElement(e);
		effect.addSceneElement(e2);
		effect.addSceneElement(ground);
		getSceneElements().add(ground);

		e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		e.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION, (float) Math.PI / 6);
		e2.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);

		EAdConditionEvent event = new EAdConditionEventImpl();
		FlagCondition condition = new FlagCondition(new EAdFieldImpl<Boolean>(
				this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET, effect);

		getEvents().add(event);
		
		e.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new PhApplyForce( e, new EAdPositionImpl(0, 100)));

		addCanon();
	}

	@Override
	public String getDescription() {
		return "A scene showing the use of jbox2d";
	}

	public String getDemoName() {
		return "Physics Scene";
	}

	private void addCanon() {

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

		getBackground()
				.addBehavior(
						new EAdKeyEventImpl(KeyActionType.KEY_PRESSED,
								KeyCode.ARROW_DOWN), goUpEffect);
		getBackground()
				.addBehavior(
						new EAdKeyEventImpl(KeyActionType.KEY_PRESSED,
								KeyCode.ARROW_UP), goDownEffect);

	}

}
