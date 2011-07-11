package es.eucm.eadventure.common.model.effects.impl.sceneelements;

import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;

/**
 * Applies an effect to the active element in the scene
 * 
 *
 */
public class EAdActiveElementEffect extends AbstractEAdEffect {
	
	private EAdSceneElementEffect effect;

	public EAdActiveElementEffect(String id) {
		super(id);
	}
	
	public void setSceneElementEffect( EAdSceneElementEffect effect ){
		this.effect = effect;
	}
	
	public EAdSceneElementEffect getSceneElementEffect( ){
		return effect;
	}
	
	

}
