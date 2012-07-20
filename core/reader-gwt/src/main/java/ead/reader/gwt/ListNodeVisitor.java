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

package ead.reader.gwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdListImpl;
import ead.common.util.EAdPosition;
import ead.reader.DOMTags;

public class ListNodeVisitor extends NodeVisitor<EAdList<Object>> {

	private static final Logger logger = LoggerFactory.getLogger("ListNodeVisitor");

	@SuppressWarnings("unchecked")
	@Override
	public EAdList<Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();

		EAdList<Object> list = null;

		if (field == null || parent == null) {
			list = createNewList(node);
		} else {
			try {
				list = (EAdList<Object>) field.getFieldValue(parent);
				list.clear();
			} catch (ClassCastException e) {
                logger.error("Failed to cast as list, field {} in {}",
                    new Object[] {field.getName(), parent}, e);
			}
		}

		// TODO: do for more types?
		if (list.getValueClass() == Integer.class ||
				list.getValueClass() == Float.class ||
				list.getValueClass() == EAdPosition.class || list.getValueClass() == EAdPosition.class) {
			String value = GWTReader.getNodeText(node);
			if (value != null && !value.equals("")) {
				String[] values = value.split("\\|");
				for (int i = 0; i < values.length; i++)
					list.add(ObjectFactory.getObject(values[i], list.getValueClass()));
			}
		} else {
			String type;
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				type = nl.item(i).getNodeName();
				Object object = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null, list.getValueClass());
				if (object instanceof ProxyElement) {
					((ProxyElement) object).setList(list, i);
				}
				list.add(object);
			}
		}
		return list;

	}

	private EAdList<Object> createNewList(Node node) {
		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);

		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);

		return new EAdListImpl<Object>(classType.getClass());
	}

	@Override
	public String getNodeType() {
		return DOMTags.LIST_TAG;
	}

}
