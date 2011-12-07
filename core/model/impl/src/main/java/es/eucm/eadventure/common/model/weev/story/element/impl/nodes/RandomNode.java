/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

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
