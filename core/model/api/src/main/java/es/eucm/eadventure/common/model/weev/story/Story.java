package es.eucm.eadventure.common.model.weev.story;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.weev.WEEVElement;
import es.eucm.eadventure.common.model.weev.story.elements.Comment;
import es.eucm.eadventure.common.model.weev.story.elements.Node;
import es.eucm.eadventure.common.model.weev.story.elements.Transition;
import es.eucm.eadventure.common.model.weev.story.section.StorySection;

/**
 * The story representation of the {@link WEEVModel}
 * <p>
 * The story consists of the {@link Node}s, the {@link Transition}s between them
 * and other elements, such as {@link Comments} that make up the story
 * representation of a game
 */
public interface Story extends WEEVElement {

	/**
	 * @return the list of {@link Node}s that make up the story
	 */
	EAdList<Node> getNodes();

	/**
	 * @return the initial {@link Node} in the story
	 */
	Node getInitialNode();

	/**
	 * @return the list of {@link Transition}s that make up the story
	 */
	EAdList<Transition> getTransitions();

	/**
	 * @return the list of {@link Comment}s in the story
	 */
	EAdList<Comment> getComments();

	/**
	 * @return the list of {@link StorySection}s in the story
	 */
	EAdList<StorySection> getStorySections();

}
