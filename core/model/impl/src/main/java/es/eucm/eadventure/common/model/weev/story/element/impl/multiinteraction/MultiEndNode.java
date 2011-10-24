package es.eucm.eadventure.common.model.weev.story.element.impl.multiinteraction;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;
import es.eucm.eadventure.common.model.weev.story.elements.Node;

/**
 * A group of {@link EndNode}s that represents a set or combination of possible
 * end states in a {@link MultiInteractionNode}.
 */
@Element(detailed = MultiEndNode.class, runtime = MultiEndNode.class)
public class MultiEndNode extends AbstractNode {

	/**
	 * The {@link MultiInteractionNode} of this multi end node.
	 */
	@Param(value = "multiInteracitonNode")
	private MultiInteractionNode multiInteractionNode;

	/**
	 * The list of {@link Node}s that are included in this multi end node
	 */
	private EAdList<Node> nodes;

	public MultiEndNode(MultiInteractionNode multiInteractionNode) {
		this.multiInteractionNode = multiInteractionNode;
		this.nodes = new EAdListImpl<Node>(Node.class);
	}

	public MultiInteractionNode getMultiInteractionNode() {
		return multiInteractionNode;
	}

	public void setMultiInteractionNode(
			MultiInteractionNode multiInteractionNode) {
		this.multiInteractionNode = multiInteractionNode;
	}

	public EAdList<Node> getNodes() {
		return nodes;
	}

}
