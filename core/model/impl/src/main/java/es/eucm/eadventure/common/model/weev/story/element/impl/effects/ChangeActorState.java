package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.EAdInventoryEffect;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;

/**
 * Effect to change the state of an {@link Actor}.
 * <p>
 * This effect is the WEEV implementation of the {@link EAdInventoryEffect} in
 * eAdventure. This effect allows to change or modify the actor state using the
 * values in {@link EAdInventoryEffect.Action}.
 */
@Element(detailed = ChangeActorState.class, runtime = ChangeActorState.class)
public class ChangeActorState extends AbstractEffect {

	/**
	 * The change or {@link EAdInventoryEffect.Action} to be applied to
	 * the {@link Actor}
	 */
	@Param(value = "modification")
	private EAdInventoryEffect.Action modification;

	/**
	 * The {@link Actor} where to apply the change
	 */
	@Param(value = "actor")
	private Actor actor;

	/**
	 * @param actor
	 *            The {@link Actor} where to apply the change
	 * @param modification
	 *            The change or {@link EAdInventoryEffect.Action} to be
	 *            applied to the {@link Actor}
	 */
	public ChangeActorState(Actor actor,
			EAdInventoryEffect.Action modification) {
		this.actor = actor;
		this.modification = modification;
	}

	public EAdInventoryEffect.Action getModification() {
		return modification;
	}

	public void setModification(EAdInventoryEffect.Action modification) {
		this.modification = modification;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
