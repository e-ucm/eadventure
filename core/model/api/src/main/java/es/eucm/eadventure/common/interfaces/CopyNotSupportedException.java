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

package es.eucm.eadventure.common.interfaces;


/**
 * Thrown to indicate an error during the {@code copy()} methods of the
 * {@link es.eucm.eadventure.utils.Copyable}.
 * 
 */
public class CopyNotSupportedException extends RuntimeException {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = -2912689297682758853L;

	/**
	 * Constructs a {@link CopyNotSupportedException} with the specified detail message.
	 * 
	 * @param message Detail message.
	 */
	public CopyNotSupportedException(String message){
		super(message);
	}

	/**
	 * Constructs a {@link CopyNotSupportedException} with the specified cause.
	 * 
	 * @param cause The cause of this exception.
	 */
	public CopyNotSupportedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link CopyNotSupportedException} with the specified detail message and cause.
	 * 
	 * @param message Detail message.
	 * @param cause The cause of this exception.
	 */
	public CopyNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}
}
