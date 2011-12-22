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

package es.eucm.eadventure.common.model.elements.conditions;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.conditions.enums.EmptyConditionValue;

@Element(runtime = EmptyCond.class, detailed = EmptyCond.class)
public class EmptyCond extends AbstractCondition {

	public static EmptyCond TRUE_EMPTY_CONDITION = new EmptyCond(
			EmptyConditionValue.TRUE);

	public static EmptyCond FALSE_EMPTY_CONDITION = new EmptyCond(
			EmptyConditionValue.FALSE);

	@Param("value")
	private EmptyConditionValue value;
	
	public EmptyCond(){
		super( );
		this.setId("emptyCondition");
	}

	public EmptyCond(EmptyConditionValue value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public EmptyConditionValue getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(EmptyConditionValue value) {
		this.value = value;
	}
	
	@Override
	public boolean equals( Object object ){
		if ( object instanceof EmptyCond ){
			return ((EmptyCond) object).value.equals(value);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ("EmptyCondition" + value).hashCode();
	}
	
	@Override
	public String toString() {
		return (value != null ? value.toString() : id + "NULL");
	}

}
