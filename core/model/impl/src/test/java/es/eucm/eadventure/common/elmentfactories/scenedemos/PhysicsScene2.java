package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhShape;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene2 extends PhysicsScene {

	public PhysicsScene2() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.YELLOW, EAdColor.ORANGE));
	}

	protected void init() {

		RectangleShape rShape = new RectangleShape(10, 100, EAdColor.BROWN);

		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2", rShape);
		getElements().add(e2);
		e2.setPosition(new EAdPositionImpl(Corner.CENTER, 500, 200));
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 4.0f);

		EAdPhysicsEffect effect = new EAdPhysicsEffect();
		effect.addSceneElement(e2);

		EAdBasicSceneElement e3 = new EAdBasicSceneElement("e3", rShape);
		getElements().add(e3);
		e3.setPosition(new EAdPositionImpl(Corner.CENTER, 200, 100));
		e3.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		effect.addSceneElement(e3);

		BezierShape circle = new CircleShape(20, 20, 20, 60);
		circle.setFill(new EAdLinearGradient(EAdColor.GREEN,
				new EAdColor(0, 100, 0)));

		EAdBasicSceneElement b = new EAdBasicSceneElement("ball", circle);
		b.setPosition(new EAdPositionImpl(Corner.CENTER, 500, 0));
		getElements().add(b, 0);
		effect.addSceneElement(b);
		b.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
		getBackground().addBehavior(
				EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				new PhApplyImpluse(b, new LiteralExpressionOperation(
						"xImpulse", "0"), new LiteralExpressionOperation(
						"yImpulse", "-100")));
		b.setVarInitialValue(EAdPhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
		b.setVarInitialValue(EAdPhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				EAdBasicSceneElement e = new EAdBasicSceneElement("ball" + i
						+ "" + j, circle);
				e.setPosition(new EAdPositionImpl(Corner.CENTER, i * 60 + 200,
						j * 60 + 200));
				getElements().add(e);
				effect.addSceneElement(e);
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE,
						PhType.DYNAMIC);
				getBackground().addBehavior(
						EAdMouseEventImpl.MOUSE_LEFT_CLICK,
						new PhApplyImpluse(e, new LiteralExpressionOperation(
								"xImpulse", "0"),
								new LiteralExpressionOperation("yImpulse",
										"-100")));
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_SHAPE,
						PhShape.CIRCULAR);
			}

		EAdConditionEvent event = new EAdConditionEventImpl();
		OperationCondition condition = new OperationCondition(new EAdFieldImpl<Boolean>(
				this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET, effect);

		//getEvents().add(event);
		
		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, effect);

		addWalls(effect);
	}

	@Override
	public String getDescription() {
		return "A scene showing the use of jbox2d";
	}

	public String getDemoName() {
		return "Physics Scene 2";
	}

}
