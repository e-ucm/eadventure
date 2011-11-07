package es.eucm.eadventure.engine.core.platform.impl;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.effects.impl.physics.EAdPhysicsEffect;
import es.eucm.eadventure.common.model.effects.impl.physics.PhApplyImpluse;
import es.eucm.eadventure.engine.core.PluginHandler;
import es.eucm.eadventure.engine.core.gameobjects.factories.EffectGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics.PhApplyForceGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.physics.PhysicsEffectGO;

@Singleton
public class JavaPluginHandler implements PluginHandler {

	@Override
	public void install(EffectGOFactory effectFactory) {
		effectFactory.put(EAdPhysicsEffect.class, PhysicsEffectGO.class);
		effectFactory.put(PhApplyImpluse.class, PhApplyForceGO.class);
	}

	@Override
	public void install(SceneElementGOFactory sceneElementFactory) {
		
		
	}

	@Override
	public void install(EventGOFactory eventGOFactory) {
		
		
	}

}
