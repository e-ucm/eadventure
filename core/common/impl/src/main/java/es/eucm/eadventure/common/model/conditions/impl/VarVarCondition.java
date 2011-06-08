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

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.variables.impl.vars.NumberVar;

/**
 * Condition comparing the values of two variables
 * 
 */
@Element(runtime = VarVarCondition.class, detailed = VarVarCondition.class)
public class VarVarCondition extends VarCondition {

	@Param("var1")
	private NumberVar<?> var1;

	@Param("var2")
	private NumberVar<?> var2;

	/**
	 * Constructs a condition comparing two number variables
	 * 
	 * @param var1
	 *            variable 1
	 * @param var2
	 *            variable 2
	 * @param op
	 *            operator used to compare
	 */
	public VarVarCondition(NumberVar<?> var1, NumberVar<?> var2, Operator op) {
		super(op);
		this.var1 = var1;
		this.var2 = var2;
	}

	/**
	 * @return the var1
	 */
	public NumberVar<?> getVar1() {
		return var1;
	}

	/**
	 * @param var1
	 *            the var1 to set
	 */
	public void setVar1(NumberVar<?> var1) {
		this.var1 = var1;
	}

	/**
	 * @return the var2
	 */
	public NumberVar<?> getVar2() {
		return var2;
	}

	/**
	 * @param var2
	 *            the var2 to set
	 */
	public void setVar2(NumberVar<?> var2) {
		this.var2 = var2;
	}

}
