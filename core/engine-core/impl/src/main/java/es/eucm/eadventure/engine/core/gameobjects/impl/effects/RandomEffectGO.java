package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;


/**
 * <p>
 * {@link GameObject} for the {@link EAdRandomEffect} effect
 * </p>
 *
 */
public class RandomEffectGO extends AbstractEffectGO<EAdRandomEffect> {

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
				gameState.addEffect(effect);
				break;
			}
			acc += inc;
			
		}
	}

}
