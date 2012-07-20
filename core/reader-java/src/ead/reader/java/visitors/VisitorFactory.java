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

package ead.reader.java.visitors;

import java.util.HashMap;
import java.util.Map;

public class VisitorFactory {

	private Map<String, NodeVisitor<?>> map;
	
	private static VisitorFactory instance;
	
	private VisitorFactory() {
		map = new HashMap<String, NodeVisitor<?>>();
		addVisitor(new ElementNodeVisitor());
		addVisitor(new ParamNodeVisitor());
		addVisitor(new AssetNodeVisitor());
		addVisitor(new ListNodeVisitor());
		addVisitor(new ResourcesNodeVisitor());
		addVisitor(new MapNodeVisitor());
	}
	
	private void addVisitor(NodeVisitor<?> nodeVisitor) {
		map.put(nodeVisitor.getNodeType(), nodeVisitor);
	}
	
	static public NodeVisitor<?> getVisitor(String key) {
		if (instance == null)
			instance = new VisitorFactory();
		NodeVisitor<?> visitor = instance.map.get(key);
		if (visitor == null)
			throw new RuntimeException("missing visitor " + key);
		return visitor;
	}
}
