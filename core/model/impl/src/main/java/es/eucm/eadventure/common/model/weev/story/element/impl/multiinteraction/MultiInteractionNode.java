package es.eucm.eadventure.common.model.weev.story.element.impl.multiinteraction;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractAreaNode;
import es.eucm.eadventure.common.model.weev.story.elements.Node;

/**
 * A multi-interaction node is a node that contains multiple story lines or
 * series of actions that can be performed out-of-order.
 * <p>
 * In particular, all the lines start in a {@link StartNode}. When all the
 * {@link Node}s within a particular {@link MultiEndNode} are reached, the
 * transitions from this {@link MultiEndNode} become available.
 */
@Element(detailed = MultiInteractionNode.class, runtime = MultiInteractionNode.class)
public class MultiInteractionNode extends AbstractAreaNode implements Node {

	/**
	 * The list of {@link StartNode}s
	 */
	private EAdList<StartNode> startNodes;

	/**
	 * The list of {@link MultiEndNode}s
	 */
	private EAdList<MultiEndNode> multiEndNodes;

	public MultiInteractionNode() {
		this.startNodes = new EAdListImpl<StartNode>(StartNode.class);
		this.multiEndNodes = new EAdListImpl<MultiEndNode>(MultiEndNode.class);
	}

	public EAdList<StartNode> getStartNodes() {
		return startNodes;
	}

	public EAdList<MultiEndNode> getMultiEndNodes() {
		return multiEndNodes;
	}

}
