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

package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import java.util.Map;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import es.eucm.eadventure.common.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();
		
		EAdMap<Object, Object> map = null;
		
		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = (EAdMap<Object, Object>) field.getFieldValue(parent);
		}
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt - 1; i+=2)
		{
			type = nl.item(i).getNodeName();
			Object key = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null, map.getKeyClass());
			type = nl.item(i+1).getNodeName();
			Object value = VisitorFactory.getVisitor(type).visit(nl.item(i+1), null, null, map.getValueClass());
			map.put(key, value);
		}

		return map;
	}
	
	private EAdMap<Object, Object> createNewMap(Node node) {
		Node n = node.getAttributes().getNamedItem(DOMTags.KEY_CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		ClassType<?> keyClassType = TypeOracle.Instance.getClassType(clazz);

		n = node.getAttributes().getNamedItem(DOMTags.VALUE_CLASS_AT);
		String value_clazz = n.getNodeValue();
		value_clazz = translateClass(clazz);

		ClassType<?> valueClassType = TypeOracle.Instance.getClassType(value_clazz);

		return new EAdMapImpl<Object, Object>(keyClassType.getClass(), valueClassType.getClass());
	}
	
	@Override
	public String getNodeType() {
		return DOMTags.MAP_TAG;
	}

}
