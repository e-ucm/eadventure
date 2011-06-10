package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.impl.EAdMapImpl;

/**
 * An effect that launches a random effect from a list of effects
 * 
 * 
 */
@Element(runtime = EAdRandomEffect.class, detailed = EAdRandomEffect.class)
public class EAdRandomEffect extends AbstractEAdEffect {

	/**
	 * Effect's list
	 */
	private EAdMap<EAdEffect, Float> effects;

	public EAdRandomEffect(String id) {
		super(id);
		effects = new EAdMapImpl<EAdEffect, Float>(id + "_randomEffectMap",
				EAdEffect.class, Float.class);
	}

	/**
	 * Adds an effect with a probability
	 * 
	 * @param effect
	 *            the effect
	 * @param probability
	 *            the probability
	 */
	public void addEffect(EAdEffect effect, float probability) {
		effects.put(effect, probability);
	}

	/**
	 * Returns the effects
	 * 
	 * @return the effects
	 */
	public EAdMap<EAdEffect, Float> getEffects() {
		return effects;
	}

}
