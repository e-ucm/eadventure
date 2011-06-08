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

package es.eucm.eadventure.common.model.effects.impl.variables;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.EAdVar;

/**
 * Effect for changing a variable's value
 * 
 */
@Element(runtime = EAdChangeVarValueEffect.class, detailed = EAdChangeVarValueEffect.class)
public class EAdChangeVarValueEffect extends AbstractEAdEffect {

	/**
	 * Variable to be changed
	 */
	@Param("var")
	private EAdVar<?> var;

	/**
	 * Operation to be done. The result of this operation should be assigned to
	 * the variable
	 */
	@Param("operation")
	private EAdOperation operation;

	/**
	 * Creates an empty effect
	 * 
	 * @param id
	 *            Elements's id
	 */
	public EAdChangeVarValueEffect(String id) {
		super(id);
	}

	/**
	 * Creates an effect with the required parameters
	 * 
	 * @param id
	 *            Elements id
	 * @param var
	 *            The var to be changed
	 * @param operation
	 *            The operation to be performed to obtain the value of the var
	 */
	public EAdChangeVarValueEffect(String id, EAdVar<?> var,
			EAdOperation operation) {
		super(id);
		this.var = var;
		this.operation = operation;
	}

	/**
	 * Sets the var to be updated with the operation result
	 * 
	 * @param var
	 *            the variable
	 */
	public void setVar(EAdVar<?> var) {
		this.var = var;
	}

	/**
	 * Sets the operation to be done by this effect. The result of this
	 * operation should be assigned to the variable
	 * 
	 * @param operation
	 *            the operation
	 */
	public void setOperation(EAdOperation operation) {
		this.operation = operation;
	}

	/**
	 * Returns the var to be updated with the operation result
	 * 
	 * @return the var to be updated with the operation result
	 */
	public EAdVar<?> getVar() {
		return var;
	}

	/**
	 * Returns the operation to be done by this effect
	 * 
	 * @return the operation to be done by this effect
	 */
	public EAdOperation getOperation() {
		return operation;
	}

}
