package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;


/**
 * <p>
 * {@link GameObject} for the {@link EAdRandomEffect} effect
 * </p>
 *
 */
public class RandomEffectGO extends AbstractEffectGO<EAdRandomEffect> {

	@Inject
	public RandomEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	public void update( GameState gameState ){
		EAdMap<EAdEffect, Float> effects = this.element.getEffects();
		float total = 0;
		for ( Float f: effects.values() ){
			total += f;
		}
		
		float random = (float) (Math.random() * total);
		float acc = 0;
		for ( EAdEffect effect: effects.keySet() ){
			float inc = effects.get(effect);
			if ( acc <= random && random < acc + inc ){
				gameState.addEffect(effect, action);
				break;
			}
			acc += inc;
			
		}
	}
}
