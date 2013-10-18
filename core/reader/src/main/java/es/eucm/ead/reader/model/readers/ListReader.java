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

package es.eucm.ead.reader.model.readers;

import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.reader.ObjectsFactory;
import es.eucm.ead.reader.model.ReaderVisitor;
import es.eucm.ead.tools.xml.XMLNode;

@SuppressWarnings("rawtypes")
public class ListReader extends AbstractReader<EAdList> {

	public final EAdList EMPTY_LIST = new EAdList();

	public ListReader(ObjectsFactory elementsFactory, ReaderVisitor visitor) {
		super(elementsFactory, visitor);
	}

	@Override
	public EAdList read(XMLNode node) {
		if (node.hasChildNodes()) {
			EAdList list = new EAdList();
			for (XMLNode n : node.getChildren()) {
				readerVisitor.loadElement(n, new ListVisitorListener(list));
			}
			return list;
		} else {
			return EMPTY_LIST;
		}

	}

	public static class ListVisitorListener implements
			ReaderVisitor.VisitorListener {
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
