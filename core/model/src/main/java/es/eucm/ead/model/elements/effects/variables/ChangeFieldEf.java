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

package es.eucm.ead.model.elements.effects.variables;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

/**
 * Effect to change a field from an element during runtime
 */
@Element
public class ChangeFieldEf extends Effect {

	/**
	 * The owner of the field
	 */
	@Param
	private BasicElement element;

	/**
	 * Name of the variable to change
	 */
	@Param
	private String varName;

	/**
	 * Operation to be done. The result of this operation should be assigned to
	 * the variable
	 */
	@Param
	private Operation operation;

	/**
	 * Creates an empty effect
	 */
	public ChangeFieldEf() {
	}

	public ChangeFieldEf(BasicElement element, String varName,
			Operation operation) {
		this.element = element;
		this.varName = varName;
		this.operation = operation;
	}

	/**
	 * Effect to change the given field with the result of the given operation. If the field is {@code null} th
	 *
	 * @param field     the field
	 * @param operation the operation
	 */
	public ChangeFieldEf(ElementField field, Operation operation) {
		this.element = field.getElement();
		this.varName = field.getVarName();
		this.operation = operation;
	}

	/**
	 * Sets the operation to be done by this effect. The result of this
	 * operation should be assigned to the fields contained by the effect
	 *
	 * @param operation the operation
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @return the operation to be done by this effect
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @return Returns the owner of the field. Could be null, meaning that the owner should be the element owning the effect
	 */
	public BasicElement getElement() {
		return element;
	}

	/**
	 * Sets the owner of the field. If null, the engine will interpret that the owner is the element that launched the effect
	 *
	 * @param element the owner of the field
	 */
	public void setElement(BasicElement element) {
		this.element = element;
	}

	/**
	 *
	 * @return the variable name to change
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 *
	 * @param varName the variable name to change
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String toString() {
		return (element == null ? "" : element.getId()) + "." + varName + "="
				+ operation;
	}

}
