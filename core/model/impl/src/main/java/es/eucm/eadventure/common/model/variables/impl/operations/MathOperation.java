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
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdOperationImpl;

/**
 * <p>
 * A literal arithmetical expression ( such as -5-6/(-2) + sqr(15+x) ) that can
 * be evaluated.
 * </p>
 * <p>
 * Supports the following functions: +, -, *, /, ^, %, cos, sin, tan, acos,
 * asin, atan, sqrt, sqr, log, min, max, ceil, floor, abs, neg, rndr
 * </p>
 */
@Element(runtime = MathOperation.class, detailed = MathOperation.class)
public class MathOperation extends EAdOperationImpl {

	/**
	 * Expression to be evaluated
	 */
	@Param("expression")
	private String expression;

	public MathOperation() {
		super();
		this.expression = "";
	}

	/**
	 * Creates a literal expression with the given expression
	 * 
	 * @param parent
	 * @param id
	 * @param expression
	 * @param floatVar3
	 * @param floatVar2
	 * @param fields
	 */
	public MathOperation(String expression,
			EAdField<?>... fields) {
		super();
		setId(expression);
		this.expression = expression;
		if (fields != null) {
			for (EAdField<?> f : fields) {
				varList.add(f);
			}
		}
	}
	/**
	 * Sets the literal expression to be evaluated
	 * 
	 * @param expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Returns the literal expression to be evaluated
	 * 
	 * @return the literal expression to be evaluated
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Returns an expression to increment a variable with the given value
	 * 
	 * @param var
	 *            the variable to be incremented
	 * @param increment
	 *            the increment
	 * @return the operation
	 */
	public static MathOperation getIncrementExpression(
			EAdField<?> var, Integer increment) {
		return new MathOperation(
				"[0] + " + increment, var);
	}
	
	public String toString(){
		String s = expression;
		int i = 0;
		for ( EAdField<?> f: varList){
			s = s.replace("[" + i + "]", f + "" );
			i++;
		}
		return s;
	}

}
