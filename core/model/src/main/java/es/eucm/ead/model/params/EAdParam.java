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

package es.eucm.ead.model.params;

import com.gwtent.reflection.client.Reflectable;

/**
 * <p>
 * An eAdventure parameter is data that it's used by the {@link EAdElement}s. A
 * parameter is able to store itself in a string, and is able to reconstruct it
 * from it.
 * </p>
 * 
 * <p>
 * Override {@link Object#equals(Object)} in implementing classes.
 * </p>
 */
@Reflectable
public interface EAdParam {

	/**
	 * Return a string representing the parameter
	 * 
	 * @return
	 */
	String toStringData();

	/**
	 * Parses a string created with {@link EAdParam#toStringData()} and sets the
	 * parameter to the proper values
	 * 
	 * @param data
	 *            the string to be parsed
	 * @return if the data was correctly parsed. If it returns false, the
	 *         parameter has been set to its default value
	 * 
	 */
	boolean parse(String data);

}
