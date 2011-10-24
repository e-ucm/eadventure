package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;

/**
 * Effect to change the state of an {@link Actor}.
 * <p>
 * This effect is the WEEV implementation of the {@link EAdModifyActorState} in
 * eAdventure. This effect allows to change or modify the actor state using the
 * values in {@link EAdModifyActorState.Modification}.
 */
@Element(detailed = ChangeActorState.class, runtime = ChangeActorState.class)
public class ChangeActorState extends AbstractEffect {

	/**
	 * The change or {@link EAdModifyActorState.Modification} to be applied to
	 * the {@link Actor}
	 */
	@Param(value = "modification")
	private EAdModifyActorState.Modification modification;

	/**
	 * The {@link Actor} where to apply the change
	 */
	@Param(value = "actor")
	private Actor actor;

	/**
	 * @param actor
	 *            The {@link Actor} where to apply the change
	 * @param modification
	 *            The change or {@link EAdModifyActorState.Modification} to be
	 *            applied to the {@link Actor}
	 */
	public ChangeActorState(Actor actor,
			EAdModifyActorState.Modification modification) {
		this.actor = actor;
		this.modification = modification;
	}

	public EAdModifyActorState.Modification getModification() {
		return modification;
	}

	public void setModification(EAdModifyActorState.Modification modification) {
		this.modification = modification;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
