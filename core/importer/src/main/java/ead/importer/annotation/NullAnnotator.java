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

package ead.importer.annotation;

import ead.importer.annotation.ImportAnnotator;
import ead.common.model.EAdElement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An annotator that does not record anything.
 * Useful for "bare" imports; does support some minor debugging.
 * @author mfreire
 */
public class NullAnnotator implements ImportAnnotator {
	private static final Logger logger = LoggerFactory
			.getLogger("NullAnnotator");

	private static ArrayList<EAdElement> stack = new ArrayList<EAdElement>();

	private static class PlaceHolder implements EAdElement {
		private String id;

		public PlaceHolder(String id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj != null && ((PlaceHolder) obj).id.equals(id));
		}

		@Override
		public int hashCode() {
			return this.id.hashCode();
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "<" + id + ">";
		}
	}

	@Override
	public void annotate(Type key, Object... values) {
		annotate(stack.get(stack.size()-1), key, values);
	}

	@Override
	public void annotate(EAdElement element, Type key, Object... values) {
		if (logger.isDebugEnabled()) {
			if (element == null)
				element = new PlaceHolder(values[0].toString());
			switch (key) {
			case Open: {
				logger.debug("Entering {}", element);
				stack.add(element);
				break;
			}
			case Close: {
				logger.debug("Exiting {}", element);
				int i = stack.lastIndexOf(element);
				if (i < 0) {
					logger
							.error(
									"Exiting {} -- no such element currently open in {}",
									new Object[] { element, stack });
				} else if (i != stack.size() - 1) {
					logger.error("Exiting {} -- element is not last in {}",
							new Object[] { element, stack });
					stack.remove(i);
				} else {
					stack.remove(i);
				}
				break;
			}
			case Comment: {
				logger.debug("Annotating {}({}) with {}: Comment --> {}",
						new Object[] { element, stack, key, values[0] });
				break;
			}
			default: {
				logger.debug("Annotating {}({}) with {}: {} --> {}",
						new Object[] { element, stack, key, values[0],
								values[1] });
			}
			}
		}
	}
}
