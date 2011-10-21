package es.eucm.eadventure.common.model.weev.story.elements;

import es.eucm.eadventure.common.model.weev.story.StoryElement;

/**
 * A node (i.e. a point in the story) in the {@link Story} of the {@link WEEVModel}
 */
public interface Node extends StoryElement {

	/**
	 * @return the {@link StoryList} of {@link Hint}s associated to this node
	 */
	StoryList<Hint> getHintList();
	
	//TODO get/set story block?
	
}
