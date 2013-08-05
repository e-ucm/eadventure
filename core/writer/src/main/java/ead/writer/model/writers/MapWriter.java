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

package ead.writer.model.writers;

import es.eucm.ead.model.elements.extra.EAdMap;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

@SuppressWarnings("rawtypes")
public class MapWriter extends AbstractWriter<EAdMap> {

	private int total;

	public MapWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
		total = 0;
	}

	@Override
	public XMLNode write(EAdMap object) {
		total++;
		XMLNode node = modelVisitor.newNode(DOMTags.MAP_TAG);

		MapWriterListener listener = new MapWriterListener(node);
		for (Object key : object.keySet()) {
			Object value = object.get(key);
			modelVisitor.writeElement(key, object, listener);
			modelVisitor.writeElement(value, object, listener);
		}
		return node;
	}

	public static class MapWriterListener implements VisitorListener {

		private XMLNode map;

		private XMLNode key;

		public MapWriterListener(XMLNode map) {
			this.map = map;
			this.key = null;
		}

        public XMLNode getKey(){
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

	public int getTotal() {
		return total;
	}

	public void clear() {
		total = 0;
	}
}
