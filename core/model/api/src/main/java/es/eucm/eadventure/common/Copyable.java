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

package es.eucm.eadventure.common;

/**
 * Interface for copyable objects.
 *
 */
public interface Copyable<E> extends Cloneable {

	/**
	 * Creates a shallow copy of this object.
	 * 
	 * @return a shallow copy of this object.
	 * 
	 * @throws CopyNotSupportedException if the copy cannot be created.
	 */
	public E copy();
	
	/**
	 * Creates a shallow of deep copy of this object.
	 * 
	 * @param deepCopy Controls if a shallow or a deep copy is done.
	 * 
	 * @return a shallow copy of this object if {@code deepCopy == false} or
	 * a deep copy if {@code deepCopy == true}.
	 * 
	 * @throws CopyNotSupportedException if the copy cannot be created.
	 */
	public E copy(boolean deepCopy);
}
