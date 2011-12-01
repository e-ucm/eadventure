package es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.PhShape;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class PhApplyForceGO extends AbstractEffectGO<PhApplyImpluse> {

	private OperatorFactory operatorFactory;

	@Inject
	public PhApplyForceGO(AssetHandler assetHandler,
			StringHandler stringsReader, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void initilize() {
		super.initilize();
		float x = operatorFactory.operate(Float.class, element.getxForce());
		float y = operatorFactory.operate(Float.class, element.getyForce());
		ValueMap valueMap = gameState.getValueMap();
		Body b = valueMap.getValue(element.getSceneElement(),
				PhysicsEffectGO.VAR_PH_BODY);
		if (b != null) {
			b.applyLinearImpulse(new Vec2(x, y), b.getWorldCenter());
		} else {
			World w = valueMap.getValue(null, PhysicsEffectGO.VAR_PH_WORLD);
			if (w != null) {
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_FRICTION, 0.1f);
				valueMap.setValue(element.getSceneElement(),
						EAdPhysicsEffect.VAR_PH_DENSITY, 1.0f);
				PhysicsEffectGO.createBody(w, (EAdSceneElementDef) element.getSceneElement(),
						valueMap);
				b = valueMap.getValue(element.getSceneElement(),
						PhysicsEffectGO.VAR_PH_BODY);
				if (b != null) {
					b.setType(BodyType.DYNAMIC);
//					b.setLinearVelocity();
					b.applyForce(new Vec2(600, 100), b.getWorldCenter());
				}
			}
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
