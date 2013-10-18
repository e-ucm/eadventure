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

package es.eucm.ead.model.elements.conditions;

import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

import java.util.List;

/**
 * Condition comparing the values of two variables
 * 
 */
@Element
public class OperationCond extends Condition {

	public static final ValueOp TRUE = new ValueOp(Boolean.TRUE);
	public static final ValueOp FALSE = new ValueOp(Boolean.FALSE);

	static {
		// Set engine ids for statics
		TRUE.setId("#engine.value.true");
		FALSE.setId("#engine.value.false");
	}

	@Param
	private Operation op1;

	@Param
	private Operation op2;

	@Param
	private Comparator comparator;

	public OperationCond() {
		super();
	}

	public OperationCond(Operation op1, Operation op2, Comparator comparator) {
		super();
		this.op1 = op1;
		this.op2 = op2;
		this.comparator = comparator;
	}

	/**
	 * @return the value
	 */
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * @param comparator
	 *            the value to set
	 */
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public OperationCond(Operation operation, int value, Comparator comparator) {
		this(operation, new ValueOp(value), comparator);
	}

	public OperationCond(Operation op, Object object, Comparator comparator) {
		this(op, new ValueOp(object), comparator);
	}

	public OperationCond(ElementField<Boolean> field) {
		this(field, new ValueOp(Boolean.TRUE), Comparator.EQUAL);
	}

	/**
	 * @return the var1
	 */
	public Operation getOp1() {
		return op1;
	}

	/**
	 * @param op1
	 *            the var1 to set
	 */
	public void setOp1(Operation op1) {
		this.op1 = op1;
	}

	/**
	 * @return the var2
	 */
	public Operation getOp2() {
		return op2;
	}

	/**
	 * @param op2
	 *            the var2 to set
	 */
	public void setOp2(Operation op2) {
		this.op2 = op2;
	}

	@Override
	public String toString() {
		return op1 + " " + comparator + " than " + op2;
	}

	public void extractFields(List<ElementField<?>> fields) {
		if (op1 instanceof ElementField) {
			fields.add((ElementField<?>) op1);
		}

		if (op2 instanceof ElementField) {
			fields.add((ElementField<?>) op2);
		}
	}

}
