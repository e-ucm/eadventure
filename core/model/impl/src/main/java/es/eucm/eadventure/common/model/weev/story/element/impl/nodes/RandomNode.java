package es.eucm.eadventure.common.model.weev.story.element.impl.nodes;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractNode;
import es.eucm.eadventure.common.model.weev.story.elements.Node;

/**
 * A random node is a {@link Node} where the transitions that lead from it are
 * dependent on their probabilities.
 * <p>
 * This node has a list of other nodes, each associated with a probability,
 * whose transitions are used accordingly.
 */
@Element(detailed = RandomNode.class, runtime = RandomNode.class)
public class RandomNode extends AbstractNode implements Node {

	/**
	 * The list of result nodes with their probabilities
	 */
	EAdList<ChanceNode> chanceNodeList;

	public RandomNode() {
		chanceNodeList = new EAdListImpl<ChanceNode>(ChanceNode.class);
	}

	/**
	 * @return returns the list of result nodes and their probabilities
	 */
	public EAdList<ChanceNode> getChanceNodeList() {
		return chanceNodeList;
	}

	/**
	 * A node, to be used in {@link RandomNode}s, with an associated probability
	 */
	@Element(detailed = ChanceNode.class, runtime = ChanceNode.class)
	public static class ChanceNode extends AbstractNode {

		@Param(value = "chance")
		private Integer chance;

		public ChanceNode(Integer chance) {
			this.chance = chance;
		}

		public Integer getChance() {
			return chance;
		}

		public void setChance(Integer chance) {
			this.chance = chance;
		}

	}
}
