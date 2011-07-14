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

package es.eucm.eadventure.common.impl.importer.subimporters.conditions;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

public class VarConditionImporter implements EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition>{

	private EAdElementFactory factory;

	@Inject
	public VarConditionImporter(EAdElementFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public VarCondition init(
			es.eucm.eadventure.common.data.chapter.conditions.VarCondition oldObject) {
		Operator op = getOperator( oldObject.getState() );
		IntegerVar var = (IntegerVar) factory.getVarByOldId(oldObject.getId(), Condition.VAR_CONDITION);
		
		VarValCondition condition = new VarValCondition(var, oldObject.getValue(), op );
		return condition;
	}
	

	@Override
	public VarCondition convert(
			es.eucm.eadventure.common.data.chapter.conditions.VarCondition oldObject, Object object) {
		return (VarValCondition) object;
	}

	private VarValCondition.Operator getOperator( int op ){
		switch( op ){
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_EQUALS:
			return Operator.EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_GREATER_EQUALS_THAN:
			return Operator.GREATER_EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_GREATER_THAN:
			return Operator.GREATER;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_LESS_EQUALS_THAN:
			return Operator.LESS_EQUAL;
		case es.eucm.eadventure.common.data.chapter.conditions.VarCondition.VAR_LESS_THAN:
			return Operator.LESS;
		}
		return Operator.EQUAL;
	}

}
