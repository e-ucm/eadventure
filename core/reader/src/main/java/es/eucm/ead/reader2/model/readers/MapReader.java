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

package es.eucm.ead.reader2.model.readers;

import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.reader2.model.ObjectsFactory;
import es.eucm.ead.reader2.model.ReaderVisitor;
import es.eucm.ead.tools.xml.XMLNode;

@SuppressWarnings("rawtypes")
public class MapReader extends AbstractReader<EAdMap> {

	private static final EAdMap EMPTY_MAP = new EAdMap();

	public MapReader(ObjectsFactory elementsFactory, ReaderVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	@Override
	public EAdMap read(XMLNode node) {

		if (node.hasChildNodes()) {
			EAdMap map = new EAdMap();
			MapVisitorListener listener = new MapVisitorListener(map);
			for (XMLNode n : node.getChildren()) {
				readerVisitor.loadElement(n, listener);
			}
			return map;
		} else {
			return EMPTY_MAP;
		}

	}

	public class MapVisitorListener implements ReaderVisitor.VisitorListener {
		private EAdMap map;

		private boolean waitingKey;

		private Object key;

		private boolean keyReference, valueReference;

		public MapVisitorListener(EAdMap map) {
			this.map = map;
			this.keyReference = valueReference = false;
			this.waitingKey = true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean loaded(XMLNode node, Object object,
				boolean isNullInOrigin) {
			// If object is null, element is pending to be loaded, it's
			// a reference
			if (object == null && !isNullInOrigin) {
				object = node.getNodeText();
				if (waitingKey) {
					keyReference = true;
				} else {
					valueReference = true;
				}
			}

			if (waitingKey) {
				key = object;
				waitingKey = false;
			} else {
				try {
					if (keyReference || valueReference) {
						// The key and/or the value are references and they're
						// not prepared to be added to the map
						return false;
					} else {
						map.put(key, object);
					}
				} catch (Exception e) {
					// The key is not prepared to generate a valid hash. Let's
					// wait until it is
					// readerVisitor.addMapKeyValue(map, key, object, keyReference,
					// valueReference);
				} finally {
					key = null;
					keyReference = false;
					valueReference = false;
					waitingKey = true;
				}
			}

			// We return true because we have another mechanism to deal with
			// references in maps, and we don't want the step to be added again
			return true;

		}

	}

}
