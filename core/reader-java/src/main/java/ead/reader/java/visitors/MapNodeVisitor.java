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

import java.lang.reflect.Field;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.reader.adventure.DOMTags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	private static final Logger logger = LoggerFactory.getLogger("MapNodeVisitor");

	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();

		EAdMap<Object, Object> map = null;

		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = getMapFromParent(field, parent);
		}

		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i+=2)
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

		n = node.getAttributes().getNamedItem(DOMTags.VALUE_CLASS_AT);
		String value_clazz = n.getNodeValue();
		value_clazz = translateClass(clazz);


		Class<?> keyClass = null;
		Class<?> valueClass = null;
		try {
			keyClass = ClassLoader.getSystemClassLoader().loadClass(clazz);
			valueClass =  ClassLoader.getSystemClassLoader().loadClass(value_clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return new EAdMapImpl<Object, Object>(keyClass, valueClass);
	}

	@SuppressWarnings("unchecked")
	private EAdMap<Object, Object> getMapFromParent(Field field, Object parent) {
		boolean accessible = field.isAccessible();
		EAdMap<Object, Object> list = null;
		try {
			field.setAccessible(true);
			list = (EAdMap<Object, Object>) field.get(parent);
		} catch (Exception e) {
			logger.error("Error retrieving parent map for {} from {}",
                    new Object[] {field.getName(), parent}, e);
			error = true;
		} finally {
			field.setAccessible(accessible);
		}
		return list;
	}

	@Override
	public String getNodeType() {
		return DOMTags.MAP_TAG;
	}

}
