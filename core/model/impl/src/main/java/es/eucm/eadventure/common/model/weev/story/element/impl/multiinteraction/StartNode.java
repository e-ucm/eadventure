package es.eucm.eadventure.common.model.weev.story.element.impl.multiinteraction;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;

/**
 * Start node of a set of transitions in a {@link MultiInteractionNode}
 */
@Element(detailed = StartNode.class, runtime = StartNode.class)
public class StartNode extends AbstractNode {

	/**
	 * The {@link MultiInteractionNode} where this node is
	 */
	private MultiInteractionNode multiInteractionNode;
	
	public StartNode(MultiInteractionNode multiInteractionNode) {
		this.multiInteractionNode = multiInteractionNode;
	}

	public MultiInteractionNode getMultiInteractionNode() {
		return multiInteractionNode;
	}
	
}
