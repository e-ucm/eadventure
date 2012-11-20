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

package ead.common.model.elements.variables.operations;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.variables.AbstractOperation;

/**
 * 
 * An assign operation. Assigns a given value to the variable
 * 
 */
@Element
public class ValueOp extends AbstractOperation {

	@Param("value")
	private Object value;

	public ValueOp() {
		super();
	}

	/**
	 * Creates an assign operation
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value to be assigned
	 */
	public ValueOp(Object value) {
		super();
		this.value = value;
	}

	/**
	 * Returns the value to be assigned
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	public String toString() {
		return value + "";
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
