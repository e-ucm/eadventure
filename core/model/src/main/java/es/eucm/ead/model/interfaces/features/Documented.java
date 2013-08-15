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

package es.eucm.ead.model.interfaces.features;

import es.eucm.ead.model.params.text.EAdString;

/**
 * An element with documentation
 * 
 */
public interface Documented {

	/**
	 * @return the {@link EAdString} of the documentation
	 */
	EAdString getDoc();

	/**
	 * Sets the documentation associated to the element
	 * 
	 * @param documentation
	 *            the documentation
	 */
	void setDoc(EAdString documentation);

	/**
	 * 
	 * @return the {@link EAdString} of the short description for the element
	 */
	EAdString getDesc();

	/**
	 * Sets the description for the element
	 * 
	 * @param description
	 *            the description
	 */
	void setDesc(EAdString description);

	/**
	 * 
	 * @return the {@link EAdString} of the long description for the element
	 */
	EAdString getDetailDesc();

	/**
	 * Sets the detailed description for the element
	 * 
	 * @param detailedDescription
	 *            the detailed description
	 */
	void setDetailDesc(EAdString detailedDescription);

}
