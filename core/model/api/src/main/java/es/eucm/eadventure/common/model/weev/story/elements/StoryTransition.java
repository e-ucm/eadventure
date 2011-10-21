package es.eucm.eadventure.common.model.weev.story.elements;

import es.eucm.eadventure.common.model.weev.Transition;
import es.eucm.eadventure.common.model.weev.story.StoryElement;

/**
 * A transition or arc between one {@link Node} and another in the {@link Story}
 * element of the {@link WEEVModel}
 */
public interface StoryTransition extends StoryElement, Transition<Node> {

	/**
	 * @return the {@link StoryList} of {@link Effect} associated to this transition
	 */
	StoryList<Effect> getEffectList();

}
