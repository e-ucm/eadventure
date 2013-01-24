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
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.weev.story.element.impl.AbstractAreaNode;
import ead.common.model.weev.story.elements.Node;

/**
 * A multi-interaction node is a node that contains multiple story lines or
 * series of actions that can be performed out-of-order.
 * <p>
 * In particular, all the lines start in a {@link StartNode}. When all the
 * {@link Node}s within a particular {@link MultiEndNode} are reached, the
 * transitions from this {@link MultiEndNode} become available.
 */
@Element
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
		this.startNodes = new EAdList<StartNode>();
		this.multiEndNodes = new EAdList<MultiEndNode>();
	}

	public EAdList<StartNode> getStartNodes() {
		return startNodes;
	}

	public EAdList<MultiEndNode> getMultiEndNodes() {
		return multiEndNodes;
	}

}
