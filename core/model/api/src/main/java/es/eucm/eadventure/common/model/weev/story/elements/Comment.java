package es.eucm.eadventure.common.model.weev.story.elements;

import es.eucm.eadventure.common.model.weev.story.StoryElement;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A comment in the {@link Story}, which will be represented in the visual representation
 */
public interface Comment extends StoryElement {

	/**
	 * @return the {@StoryElement} with which this comment is associated
	 */
	StoryElement getStoryElement();
	
	/**
	 * @return the text of the comment
	 */
	EAdString getText();
	
}
