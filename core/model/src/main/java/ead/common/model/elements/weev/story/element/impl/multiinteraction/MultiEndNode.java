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

package ead.common.model.elements.weev.story.element.impl.multiinteraction;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.weev.story.element.impl.AbstractNode;
import ead.common.model.weev.story.elements.Node;

/**
 * A group of {@link EndNode}s that represents a set or combination of possible
 * end states in a {@link MultiInteractionNode}.
 */
@Element
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
		this.nodes = new EAdList<Node>();
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
