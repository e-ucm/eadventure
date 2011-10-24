package es.eucm.eadventure.common.model.weev.story.element.impl.nodes;

import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractAreaNode;
import es.eucm.eadventure.common.params.EAdString;

/**
 * Representation in the WEEV model of a parallel story line.
 * <p>
 * Parallel story lines are sections of the story that can be performed
 * concurrently with the rest of the game and have no influence towards the
 * outcomes
 */
public class ParallelStory extends AbstractAreaNode {

	private EAdString name;

	public ParallelStory() {
		name = EAdString.newEAdString("name");
	}

	public EAdString getName() {
		return name;
	}

}
