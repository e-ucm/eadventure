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

package es.eucm.eadventure.common.model.variables.impl.operations;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

/**
 * Represents an operation that has a boolean as a result
 * 
 * 
 */
@Element(runtime = BooleanOperation.class, detailed = BooleanOperation.class)
public class BooleanOperation extends EAdOperationImpl {

	/**
	 * Set to false boolean operation
	 */
	public static BooleanOperation FALSE_OP = new BooleanOperation(
			EmptyCondition.FALSE_EMPTY_CONDITION);
	
	/**
	 * Set to true boolean operation
	 */
	public static BooleanOperation TRUE_OP = new BooleanOperation(
			 EmptyCondition.TRUE_EMPTY_CONDITION);

	/**
	 * Operation to be done
	 */
	@Param("condition")
	private EAdCondition condition;

	public BooleanOperation() {
		this( EmptyCondition.TRUE_EMPTY_CONDITION);
	}
	
	public BooleanOperation( EAdCondition condition) {
		super();
		setId("booleanOperation" + condition.toString());
		this.condition = condition;
	}

	/**
	 * Returns the condition for this operation
	 * 
	 * @return the condition for this operation
	 */
	public EAdCondition getCondition() {
		return condition;
	}

	/**
	 * Sets the condition for this operation
	 * 
	 * @param condition
	 *            Can be any EAdCondition which always returns a boolean value
	 */
	public void setCondition(EAdCondition condition) {
		this.condition = condition;
	}
	
	public boolean equals( Object object ){
		if ( object instanceof BooleanOperation ){
			return super.equals(object) && condition.equals(((BooleanOperation) object).condition);
		}
		return false;
	}
	
	public String toString(){
		return condition + "";
	}

}
