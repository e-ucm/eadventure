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

import java.util.Iterator;

import ead.common.interfaces.Param;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.conditions.enums.ConditionOperator;
import ead.common.model.elements.extra.EAdList;

public abstract class ListedCond extends ResourcedElement implements
		EAdCondition {

	@Param("conditions")
	private EAdList<EAdCondition> conditions;

	@Param("operator")
	private ConditionOperator operator;

	public ListedCond() {
		this(null);
	}

	public ListedCond(ConditionOperator operator) {
		this(operator, (EAdCondition) null);
	}

	public ListedCond(ConditionOperator operator, EAdCondition... condition) {
		super();
		conditions = new EAdList<EAdCondition>();
		for (int i = 0; i < condition.length; i++)
			if (condition[i] != null)
				conditions.add(condition[i]);
		this.operator = operator;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	public void addCondition(EAdCondition condition) {
		conditions.add(condition);
	}

	public void replaceCondition(EAdCondition oldCondition,
			EAdCondition newCondition) {
		if (conditions.remove(oldCondition))
			conditions.add(newCondition);
	}

	public boolean removeCondition(EAdCondition condition) {
		if (conditions.size() == 1)
			return false;
		else
			return (conditions.remove(condition));
	}

	public Iterator<EAdCondition> getConditionsIterator() {
		return conditions.iterator();
	}

	public EAdList<EAdCondition> getConditions() {
		return conditions;
	}

	public abstract EmptyCond getNullOperator();

	@Override
	public String toString() {
		String value = "(";
		if (conditions.size() > 0) {
			value += conditions.get(0);
			for (int i = 1; i < conditions.size(); i++) {
				value += " " + operator + " " + conditions.get(i);
			}
			return value + ")";
		}
		return "Empty list";
	}

	public void setConditions(EAdList<EAdCondition> conditions) {
		this.conditions = conditions;
	}

	public void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

}
