package es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyForce;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class PhApplyForceGO extends AbstractEffectGO<PhApplyForce>{

	@Inject
	public PhApplyForceGO(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}
	
	@Override
	public void initilize() {
		super.initilize();
		Body b = valueMap.getValue(element.getElement(), PhysicsEffectGO.VAR_PH_BODY);
		if ( b != null ){
			EAdPosition p = element.getForce();
			b.setLinearVelocity(new Vec2(p.getX(), p.getY()));
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
