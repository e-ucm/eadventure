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

package ead.importer;

/**
 * <p>
 * An importer for converting old model {@link AdventureData} objects into new
 * model {@link EAdventureModel} objects
 * </p>
 * <p>
 * The conversion of elements is divided in two steps (initialization and
 * conversion, through the {@link GenericImporter#init} and
 * {@link GenericImporter#convert} methods respectably) in order to support
 * recursive conversion of elements. Using the conversion method directly
 * resulted in infinite loops, as expected when an element had a reference to
 * itself in one of its children.
 * 
 * @param <OldT>
 *            Old class class name from old model {@link AdventureData}
 * @param <NewT>
 *            New class class name from new model {@link EAdventureModel}
 */
public interface GenericImporter<OldT, NewT> {

	/**
	 * Returns a new-model equivalent to the old-model oldObject. Null if
	 * conversion failed. Its parameters will be filled through
	 * {@link GenericImporter#convert(Object, Object)}
	 * 
	 * @param oldObject
	 * @return
	 */
	NewT init(OldT oldObject);

	/**
	 * Converts the parameters of the old object to those of the new one
	 * 
	 * @param oldObject
	 *            the old object
	 * @param newElement
	 *            the new element. It will be filled with the parameters from
	 *            oldObject
	 * @return
	 */
	NewT convert(OldT oldObject, Object newElement);
}
