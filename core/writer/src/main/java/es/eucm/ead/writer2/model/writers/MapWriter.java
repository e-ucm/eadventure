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

package es.eucm.ead.writer2.model.writers;

import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer2.model.WriterContext;
import es.eucm.ead.writer2.model.WriterVisitor;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class MapWriter implements Writer<EAdMap> {

	private WriterVisitor writerVisitor;

	public MapWriter(WriterVisitor writerVisitor) {
		this.writerVisitor = writerVisitor;
	}

	@Override
	public XMLNode write(EAdMap object, WriterContext context) {
		XMLNode node = new XMLNode(DOMTags.MAP_TAG);

		MapWriterListener listener = new MapWriterListener(node);
		Set<Map.Entry> set = object.entrySet();
		for (Map.Entry entry : set) {
			writerVisitor.writeElement(entry.getKey(), object, listener);
			writerVisitor.writeElement(entry.getValue(), object, listener);
		}
		return node;
	}

	public static class MapWriterListener implements
			WriterVisitor.VisitorListener {

		private XMLNode map;

		private XMLNode key;

		public MapWriterListener(XMLNode map) {
			this.map = map;
			this.key = null;
		}

		public XMLNode getKey() {
			return key;
		}

		@Override
		public void load(XMLNode node, Object object) {
			if (key == null) {
				key = node;
			} else {
				map.append(key);
				map.append(node);
				key = null;
			}
		}

	}
}
