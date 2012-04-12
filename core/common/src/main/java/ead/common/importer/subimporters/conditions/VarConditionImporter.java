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

package ead.common.importer.subimporters.conditions;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.annotation.ImportAnnotator;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;

public class VarConditionImporter implements
		EAdElementImporter<VarCondition, OperationCond> {

	private EAdElementFactory factory;

	protected ImportAnnotator annotator;

	@Inject
	public VarConditionImporter(EAdElementFactory factory,
			ImportAnnotator annotator) {
		this.factory = factory;
		this.annotator = annotator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OperationCond init(VarCondition oldObject) {
		Comparator op = getOperator(oldObject.getState());
		EAdField<Integer> var = (EAdField<Integer>) factory.getVarByOldId(
				oldObject.getId(), Condition.VAR_CONDITION);

		OperationCond condition = new OperationCond(var,
				oldObject.getValue(), op);
		return condition;
	}

	@Override
	public OperationCond convert(VarCondition oldObject, Object object) {
		return (OperationCond) object;
	}

	private Comparator getOperator(int op) {
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
		}
		return Comparator.EQUAL;
	}

}
