package es.eucm.eadventure.common.model.weev.story.section;

import es.eucm.eadventure.common.model.weev.story.elements.Node;
import es.eucm.eadventure.common.params.EAdString;

/**
 * A {@link Node} that marks a possible end point for a {@link StorySection}
 */
public interface StorySectionEnd extends Node {

	/**
	 * @return the name of the story section end
	 */
	EAdString getName();
	
}
