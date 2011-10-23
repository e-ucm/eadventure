package es.eucm.eadventure.common.model.weev.story.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.impl.AbstractWEEVElement;
import es.eucm.eadventure.common.model.weev.story.Story;
import es.eucm.eadventure.common.model.weev.story.elements.Comment;
import es.eucm.eadventure.common.model.weev.story.elements.Node;
import es.eucm.eadventure.common.model.weev.story.elements.StoryTransition;
import es.eucm.eadventure.common.model.weev.story.section.StorySection;

/**
 * Default {@link Story} implementation
 */
@Element(detailed = StoryImpl.class, runtime = StoryImpl.class)
public class StoryImpl extends AbstractWEEVElement implements Story {

	EAdList<Node> nodes;
	
	EAdList<StoryTransition> transitions;
	
	EAdList<Comment> comments;
	
	EAdList<StorySection> storySections;
	
	@Param(value = "initialNode")
	Node initialNode;
	
	public StoryImpl() {
		nodes = new EAdListImpl<Node>(Node.class);
		transitions = new EAdListImpl<StoryTransition>(StoryTransition.class);
		comments = new EAdListImpl<Comment>(Comment.class);
		storySections = new EAdListImpl<StorySection>(StorySection.class);
	}
	
	@Override
	public EAdList<Node> getNodes() {
		return nodes;
	}

	@Override
	public Node getInitialNode() {
		return initialNode;
	}
	
	public void setInitialNode(Node initialNode) {
		this.initialNode = initialNode;
	}

	@Override
	public EAdList<StoryTransition> getTransitions() {
		return transitions;
	}

	@Override
	public EAdList<Comment> getComments() {
		return comments;
	}

	@Override
	public EAdList<StorySection> getStorySections() {
		return storySections;
	}

}
