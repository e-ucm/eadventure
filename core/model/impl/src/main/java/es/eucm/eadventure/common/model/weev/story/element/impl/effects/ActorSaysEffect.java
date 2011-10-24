package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.Actor;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Actor (i.e. character, NPC, etc) says something effect.
 */
@Element(detailed = ActorSaysEffect.class, runtime = ActorSaysEffect.class)
public class ActorSaysEffect extends AbstractEffect {

	/**
	 * The {@link Actor} that "speaks" the text
	 */
	@Param(value = "actor")
	private Actor actor;

	/**
	 * The {@link EAdString} of the text said by the actor
	 */
	@Param(value = "text")
	private EAdString text;

	/**
	 * @param actor
	 *            The {@link Actor} that "speaks"
	 */
	public ActorSaysEffect(Actor actor) {
		this.actor = actor;
		text = EAdString.newEAdString("text");
	}

	public EAdString getText() {
		return text;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
