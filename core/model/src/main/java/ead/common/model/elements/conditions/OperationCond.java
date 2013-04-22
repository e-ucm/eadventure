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

package ead.common.model.elements.conditions;

import java.util.List;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.operations.ValueOp;

/**
 * Condition comparing the values of two variables
 * 
 */
@Element
public class OperationCond extends AbstractCondition implements EAdCondition {

	public static final ValueOp TRUE = new ValueOp(Boolean.TRUE);
	public static final ValueOp FALSE = new ValueOp(Boolean.FALSE);

	@Param
	private EAdOperation op1;

	@Param
	private EAdOperation op2;

	@Param
	private Comparator operator;

	public OperationCond(EAdOperation op1, EAdOperation op2, Comparator operator) {
		super();
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public Comparator getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the value to set
	 */
	public void setComparator(Comparator operator) {
		this.operator = operator;
	}

	public OperationCond() {
		super();
	}

	public OperationCond(EAdOperation operation, int value, Comparator operator) {
		this(operation, new ValueOp(value), operator);
	}

	public OperationCond(EAdOperation op, Object object, Comparator operator) {
		this(op, new ValueOp(object), operator);
	}

	public OperationCond(EAdField<Boolean> field) {
		this(field, new ValueOp(Boolean.TRUE), Comparator.EQUAL);
	}

	/**
	 * @return the var1
	 */
	public EAdOperation getOp1() {
		return op1;
	}

	/**
	 * @param var1
	 *            the var1 to set
	 */
	public void setOp1(EAdOperation op1) {
		this.op1 = op1;
	}

	/**
	 * @return the var2
	 */
	public EAdOperation getOp2() {
		return op2;
	}

	/**
	 * @param op2
	 *            the var2 to set
	 */
	public void setOp2(EAdOperation op2) {
		this.op2 = op2;
	}

	@Override
	public String toString() {
		return op1 + " " + operator + " than " + op2;
	}

	public void addFields(List<EAdField<?>> fields) {
		if (op1 instanceof EAdField) {
			fields.add((EAdField<?>) op1);
		}

		if (op2 instanceof EAdField) {
			fields.add((EAdField<?>) op2);
		}
	}

}
