package es.eucm.eadventure.engine.core.gameobjects.factories;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;

public interface EffectGOFactory extends
		GameObjectFactory<EAdEffect, EffectGO<? extends EAdEffect>> {

}
