package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdPhysicsEffect.PhType;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class PhysicsScene extends EmptyScene {

	public PhysicsScene() {
		this.setBackgroundFill(new EAdLinearGradient(EAdColor.BLUE, EAdColor.CYAN));
		EAdBasicSceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory()
				.createSceneElement("GO Physics!", 400, 200);
		e.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 100));
		
		
		RectangleShape groundS = new RectangleShape( 600, 20 );
		groundS.setFill(new EAdLinearGradient( EAdColor.BROWN, EAdColor.DARK_BROWN));
		EAdBasicSceneElement ground = new EAdBasicSceneElement("ground", groundS);
		ground.setPosition(new EAdPositionImpl(Corner.CENTER, 400, 500));
		
		getSceneElements().add(e);
		

		EAdPhysicsEffect effect = new EAdPhysicsEffect();
		effect.addSceneElement(e);
		effect.addSceneElement(ground);
		getSceneElements().add(ground);
		
		e.setVarInitialValue(EAdPhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);

		EAdConditionEvent event = new EAdConditionEventImpl();
		FlagCondition condition = new FlagCondition(new EAdFieldImpl<Boolean>(
				this, EAdSceneImpl.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET, effect);

		getEvents().add(event);
	}

	@Override
	public String getDescription() {
		return "A scene showing the use of jbox2d";
	}

	public String getDemoName() {
		return "Physics Scene";
	}

}
