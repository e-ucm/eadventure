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

package es.eucm.ead.model.elements.operations;

import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

@Element
public class ConditionedOp extends Operation {

	@Param
	private Condition condition;

	@Param
	private Operation opTrue;

	@Param
	private Operation opFalse;

	public ConditionedOp() {

	}

	public ConditionedOp(Condition c, Operation opTrue, Operation opFalse) {
		super();
		this.opTrue = opTrue;
		this.opFalse = opFalse;
		this.condition = c;
	}

	public Condition getCondition() {
		return condition;
	}

	public Operation getOpTrue() {
		return opTrue;
	}

	public Operation getOpFalse() {
		return opFalse;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setOpTrue(Operation opTrue) {
		this.opTrue = opTrue;
	}

	public void setOpFalse(Operation opFalse) {
		this.opFalse = opFalse;
	}

	public String toString() {
		return condition + "?" + opTrue + ":" + opFalse;
	}
}
