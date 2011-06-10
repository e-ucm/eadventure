package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.behavior.EAdBehavior;
import es.eucm.eadventure.common.model.behaviors.impl.EAdBehaviorImpl;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;

public abstract class AbstractEAdElementWithBehavior extends AbstractEAdElement {

	@Param("behavior")
	protected EAdBehavior behavior;

	public AbstractEAdElementWithBehavior(String id) {
		super(id);
		this.behavior = new EAdBehaviorImpl(id + "behaviors");
	}
	
	public EAdElementList<EAdEffect> getEffects(EAdGUIEvent event) {
		return behavior.getEffects(event);
	}

	/**
	 * Adds a behavior to this actor attached to the given event
	 * 
	 * @param event
	 *            the event
	 * @param behavior
	 *            the effect
	 */
	public void addBehavior(EAdGUIEvent event, EAdEffect effect) {
		behavior.addBehavior(event, effect);
	}


	public void addBehavior(EAdGUIEvent event,
			EAdElementList<EAdEffect> effects) {
		behavior.addBehavior(event, effects);
	}

	/**
	 * Sets the behavior for this actor
	 * 
	 * @param behavior
	 *            the behavior
	 */
	public void setBehavior(EAdBehavior behavior) {
		this.behavior = behavior;
	}

	/**
	 * Returns the behavior for this actor
	 * 
	 * @return the behavior for this actor
	 */
	public EAdBehavior getBehavior() {
		return behavior;
	}

}
