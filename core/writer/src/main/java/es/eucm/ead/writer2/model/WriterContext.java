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

package es.eucm.ead.writer2.model;

import es.eucm.ead.tools.xml.XMLNode;

public interface WriterContext {

	/**
	 * An id for the context
	 * @return
	 */
	int getContextId();

	/**
	 * Generates a new id, valid for this context
	 * @return
	 */
	String generateNewId();

	/**
	 * Returns if the given id has already been processed by this context
	 * @param id
	 * @return
	 */
	boolean containsId(String id);

	/**
	 * Translate the class to a symbolic string
	 * @param type
	 * @return
	 */
	String translateClass(Class<?> type);

	/**
	 * Translates the field to a symbolic string
	 * @param name
	 * @return
	 */
	String translateField(String name);

	/**
	 * Translates the param to a symbolic string
	 * @param param
	 * @return
	 */
	String translateParam(String param);

	/**
	 * Process the object that is going to be written. This method can be used to analyze de model from the context,
	 * since ALL objects in the model pass over here.
	 *
	 * @param object
	 * @param node
	 */
	Object process(Object object, XMLNode node);
}
