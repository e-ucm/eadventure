package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;

/**
 * Effect to change the {@link Actor} visibility (i.e. hide or show)
 */
@Element(detailed = ChangeActorVisibilityEffect.class, runtime = ChangeActorVisibilityEffect.class)
public class ChangeActorVisibilityEffect extends AbstractEffect {

	/**
	 * <p>
	 * The change to the visibility:
	 * <li>SHOW: display the actor in the spaces where it is placed</li>
	 * <li>HIDE: hide the actor, even in the spaces where it is placed</li>
	 * </p>
	 * 
	 */
	public static enum Change {
		SHOW, HIDE
	}

	/**
	 * The {@link Change} to the {@link Actor}'s visibility
	 */
	@Param(value = "change")
	private Change change;

	/**
	 * The {@link Actor} to which to apply the change
	 */
	@Param(value = "actor")
	private Actor actor;

	/**
	 * @param actor
	 *            The {@link Actor} to which to apply the change
	 * @param change
	 *            The {@link Change} to the {@link Actor}'s visibility
	 */
	public ChangeActorVisibilityEffect(Actor actor, Change change) {
		this.actor = actor;
		this.change = change;
	}

	public Change getChange() {
		return change;
	}

	public void setChange(Change change) {
		this.change = change;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	
}
