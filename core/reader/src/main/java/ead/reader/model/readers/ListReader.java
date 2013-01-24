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

package ead.reader.model.readers;

import ead.common.model.elements.extra.EAdList;
import ead.common.util.EAdPosition;
import ead.reader.model.ObjectsFactory;
import ead.reader.model.XMLVisitor;
import ead.reader.model.XMLVisitor.VisitorListener;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

@SuppressWarnings("rawtypes")
public class ListReader extends AbstractReader<EAdList> {

	public ListReader(ObjectsFactory elementsFactory, XMLVisitor visitor) {
		super(elementsFactory, visitor);
	}

	@SuppressWarnings( { "unchecked" })
	@Override
	public EAdList read(XMLNode node) {
		Class<?> clazz = this.getNodeClass(node);
		EAdList list = new EAdList();
		if (clazz == Integer.class || clazz == Float.class
				|| clazz == EAdPosition.class) {
			readSimpleList(list, node.getNodeText(), clazz);
		} else {
			XMLNodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				xmlVisitor.loadElement(children.item(i),
						new ListVisitorListener(list));
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private void readSimpleList(EAdList list, String nodeText, Class<?> clazz) {
		String values[] = nodeText.split("\\|");
		for (String v : values) {
			Object o = null;
			try {
				if (clazz == Integer.class) {
					o = Integer.parseInt(v);
				} else if (clazz == Float.class) {
					o = Float.parseFloat(v);
				} else {
					o = new EAdPosition(v);
				}
			} catch (Exception e) {

			}

			if (o == null) {
				logger
						.warn(
								"Value for list not admitted {}. This might causes problems",
								v);
			} else {
				list.add(o);
			}
		}

	}

	public static class ListVisitorListener implements VisitorListener {
		private EAdList list;

		public ListVisitorListener(EAdList list) {
			this.list = list;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean loaded(XMLNode node, Object object,
				boolean isNullInOrigin) {
			if (object != null || isNullInOrigin) {
				list.add(object);
				return true;
			}
			return false;
		}

	}

}
