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

package es.eucm.eadventure.common.model.conditions.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.variables.EAdField;

/**
 * Condition comparing a variable with a value
 * 
 * 
 */
@Element(runtime = VarValCondition.class, detailed = VarValCondition.class)
public class VarValCondition extends VarCondition {

	@Param("variable")
	private EAdField<? extends Number> var;

	@Param("value")
	private Float val;

	/**
	 * Constructs a condition comparing a variables with a value
	 * 
	 * @param var
	 *            variable
	 * @param val
	 *            value
	 * @param value
	 *            operator used to compare
	 */
	public VarValCondition(EAdField<? extends Number> var, Number val, Operator op) {
		super(op);
		this.var = var;
		this.val = val.floatValue();
	}
	
	public VarValCondition(String id, EAdField<? extends Number> var, Number val, Operator op) {
		super(op);
		this.var = var;
		this.val = val.floatValue();
		this.id = id;
	}

	/**
	 * @return the var
	 */
	public EAdField<? extends Number> getVar() {
		return var;
	}

	/**
	 * @param var
	 *            the var to set
	 */
	public void setVar(EAdField<? extends Number> var) {
		this.var = var;
	}

	/**
	 * @return the val
	 */
	public Number getVal() {
		return val;
	}

	/**
	 * @param val
	 *            the val to set
	 */
	public void setVal(Float val) {
		this.val = val;
	}

	
	
}
