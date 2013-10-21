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

package es.eucm.ead.writer.model.writers.simplifiers.elements;

import es.eucm.ead.model.elements.conditions.*;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.writer.model.writers.simplifiers.ObjectSimplifier;

import java.util.ArrayList;
import java.util.List;

public class ConditionsSimplifier implements ObjectSimplifier<Condition> {

	/**
	 * An auxiliary field
	 */
	private List<Condition> conditionsAux;
	private List<Condition> conditionsAuxToAdd;

	public ConditionsSimplifier() {
		conditionsAux = new ArrayList<Condition>();
		conditionsAuxToAdd = new ArrayList<Condition>();
	}

	public Object simplify(Condition condition) {
		if (condition instanceof ListedCond) {
			condition = simplifyListed((ListedCond) condition);
		} else if (condition instanceof NOTCond) {
			condition = simplifyNot((NOTCond) condition);
		}

		return condition;
	}

	private Condition simplifyNot(NOTCond condition) {
		// If is the form NOT ( operation == value ) -> operation != value
		Condition nc = condition.getCondition();
		if (nc instanceof OperationCond) {
			Comparator oppositeComparator = getOppositeComparator(((OperationCond) nc)
					.getComparator());
			return new OperationCond(((OperationCond) nc).getOp1(),
					((OperationCond) nc).getOp2(), oppositeComparator);
		}
		return condition;
	}

	private Condition simplifyListed(ListedCond condition) {
		// Conditions can be simplify as any boolean operation
		conditionsAux.clear();
		conditionsAuxToAdd.clear();
		for (Condition c : condition.getConditions()) {
			if (c instanceof EmptyCond) {
				// If it is the condition null operator, the whole condition is
				// false
				if (!c.equals(condition.getNullOperator())) {
					return condition.getNullOperator();
				}
				// If not, it is not necessary, so we delete it
				else {
					conditionsAux.add(c);
				}
			} else if (c instanceof ListedCond) {
				Condition c2 = simplifyListed((ListedCond) c);
				if (c2 != c) {
					conditionsAux.add(c);
					// If it's an OR inside another OR, we group them and remove
					// unnecessary conditions
					if (c2 instanceof ListedCond) {
						ListedCond listed = (ListedCond) c2;
						if (listed.getNullOperator().equals(
								((ListedCond) c).getNullOperator())) {
							conditionsAuxToAdd.addAll(listed.getConditions());
						} else {
							conditionsAuxToAdd.add(c2);
						}
					} else {
						conditionsAuxToAdd.add(c2);
					}
				}
			} else if (c instanceof NOTCond) {
				Condition cond = simplifyNot((NOTCond) c);
				if (cond != c) {
					conditionsAux.add(c);
					conditionsAuxToAdd.add(cond);
				}
			}
		}

		for (Condition c : conditionsAux) {
			condition.getConditions().remove(c);
		}

		for (Condition c : conditionsAuxToAdd) {
			condition.getConditions().add(c);
		}

		if (condition.getConditions().size() == 1) {
			return condition.getConditions().get(0);
		}

		return condition;
	}

	private Comparator getOppositeComparator(Comparator operator) {
		switch (operator) {
		case GREATER:
			return Comparator.LESS_EQUAL;
		case GREATER_EQUAL:
			return Comparator.LESS;
		case EQUAL:
			return Comparator.DIFFERENT;
		case LESS_EQUAL:
			return Comparator.GREATER;
		case LESS:
			return Comparator.GREATER_EQUAL;
		case DIFFERENT:
			return Comparator.EQUAL;
		}
		return null;
	}

	@Override
	public void clear() {

	}
}
