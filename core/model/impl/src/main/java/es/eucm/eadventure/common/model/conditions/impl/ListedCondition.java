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

import java.util.Iterator;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;

public abstract class ListedCondition extends AbstractEAdElement implements EAdCondition {

	/**
	 * Operator for conditions
	 * 
	 *
	 */
	public enum Operator {
		/**
		 * AND operator
		 */
		AND, 
		
		/**
		 * OR operator
		 */
		OR, 
		
		/**
		 * OTHER operator
		 */
		OTHER
	}
	
	private EAdElementList<EAdCondition> conditions;
	
	@Param("operator")
	private Operator operator;
	
	public ListedCondition( String id, Operator operator ){
		this( id, operator, (EAdCondition) null );
	}
	
	public ListedCondition(String id, Operator operator, EAdCondition... condition) {
		super(id);
		conditions = new EAdElementListImpl<EAdCondition>(EAdCondition.class);
		for (int i = 0; i < condition.length; i++)
			if ( condition[i] != null )
				conditions.add(condition[i]);
		this.operator = operator;
	}
		
	public ListedCondition(Operator operator, EAdCondition... condition) {
		this("conditionId", operator, condition);
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public void addCondition(EAdCondition condition) {
		conditions.add(condition);
	}
	
	public void replaceCondition(EAdCondition oldCondition, EAdCondition newCondition) {
		if (conditions.remove(oldCondition))
			conditions.add(newCondition);
	}
	
	public boolean removeCondition(EAdCondition condition) {
		if (conditions.size() == 1)
			return false;
		else return (conditions.remove(condition));
	}
	
	public Iterator<EAdCondition> getConditions() {
		return conditions.iterator();
	}
	
	public EAdElementList<EAdCondition> getConds() {
		return conditions;
	}
	
	public abstract EmptyCondition getNullOperator();
}
