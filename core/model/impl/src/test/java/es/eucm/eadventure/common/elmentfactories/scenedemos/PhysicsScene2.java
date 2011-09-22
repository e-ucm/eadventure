package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhShape;
import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyForce;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene2 extends PhysicsScene {

	public PhysicsScene2() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.CYAN, EAdColor.BLUE));
	}

	protected void init() {

		EAdBasicSceneElement e2 = new EAdBasicSceneElement("e2",
				new RectangleShape(10, 100, EAdColor.BROWN));
		getSceneElements().add(e2);
		e2.setPosition(new EAdPositionImpl(Corner.CENTER, 500, 300));
		e2.setVarInitialValue(EAdBasicSceneElement.VAR_ROTATION,
				(float) Math.PI / 2.0f);

		EAdPhysicsEffect effect = new EAdPhysicsEffect();
		effect.addSceneElement(e2);

		BezierShape circle = new CircleShape(20, 20, 20, 25);
		circle.setFill(new EAdLinearGradient(EAdColor.LIGHT_GRAY, EAdColor.BLACK));
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				EAdBasicSceneElement e = new EAdBasicSceneElement("ball" + i
						+ "" + j, circle);
				e.setPosition(new EAdPositionImpl(Corner.CENTER, i * 60 + 200,
						j * 60 + 200));
				getSceneElements().add(e);
				effect.addSceneElement(e);
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE,
						PhType.DYNAMIC);
				getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
						new PhApplyForce(e, new EAdPositionImpl(0, 300)));
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_SHAPE,
						PhShape.CIRCULAR);
			}

		EAdConditionEvent event = new EAdConditionEventImpl();
		FlagCondition condition = new FlagCondition(new EAdFieldImpl<Boolean>(
				this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET, effect);

		getEvents().add(event);

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
