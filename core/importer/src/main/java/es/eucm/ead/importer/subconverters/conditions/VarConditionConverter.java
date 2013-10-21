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

package es.eucm.ead.importer.subconverters.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.conditions.enums.Comparator;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;

@Singleton
public class VarConditionConverter {

	private ModelQuerier modelQuerier;

	@Inject
	public VarConditionConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
	}

	public OperationCond convert(VarCondition oldObject) {
		// [COND - Variable]
		Comparator op = getComparator(oldObject.getState());
		ElementField var = modelQuerier.getVariable(oldObject.getId());
		OperationCond condition = new OperationCond(var, oldObject.getValue(),
				op);
		return condition;
	}

	/**
	 * Returns the corresponding comparator for the given operation constant
	 * 
	 * @param op
	 * @return
	 */
	private Comparator getComparator(int op) {
		switch (op) {
		case VarCondition.VAR_EQUALS:
			return Comparator.EQUAL;
		case VarCondition.VAR_GREATER_EQUALS_THAN:
			return Comparator.GREATER_EQUAL;
		case VarCondition.VAR_GREATER_THAN:
			return Comparator.GREATER;
		case VarCondition.VAR_LESS_EQUALS_THAN:
			return Comparator.LESS_EQUAL;
		case VarCondition.VAR_LESS_THAN:
			return Comparator.LESS;
		case VarCondition.VAR_NOT_EQUALS:
			return Comparator.DIFFERENT;
		}
		return Comparator.EQUAL;
	}

}
