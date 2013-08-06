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

package ead.importer.subimporters.conditions;

import com.google.inject.Inject;

import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.conditions.ANDCond;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;

public class ConditionsImporter implements
		EAdElementImporter<Conditions, EAdCondition> {

	private EAdElementImporter<FlagCondition, OperationCond> flagConditionImporter;
	private EAdElementImporter<VarCondition, OperationCond> varConditionImporter;
	private EAdElementFactory factory;

	protected ImportAnnotator annotator;

	@Inject
	public ConditionsImporter(
			EAdElementImporter<FlagCondition, OperationCond> flagConditionImporter,
			EAdElementImporter<VarCondition, OperationCond> varConditionImporter,
			EAdElementFactory factory, ImportAnnotator annotator) {
		this.factory = factory;
		this.flagConditionImporter = flagConditionImporter;
		this.varConditionImporter = varConditionImporter;
		this.annotator = annotator;
	}

	@Override
	public EAdCondition init(Conditions oldObject) {
		return new ANDCond();
	}

	@Override
	public EAdCondition convert(Conditions oldObject, Object object) {
		ANDCond newCondition = (ANDCond) object;

		if (oldObject.getSimpleConditions().size() == 0
				&& oldObject.getEitherConditionsBlockCount() == 0) {
			return EmptyCond.TRUE;
		}

		for (Condition c : oldObject.getSimpleConditions()) {
			EAdCondition newC = getSimpleCondition(c);
			if (newC != null) {
				newCondition.addCondition(newC);
			}

		}

		for (int i = 0; i < oldObject.getEitherConditionsBlockCount(); i++) {
			ORCond orCondition = new ORCond();
			Conditions conditions = oldObject.getEitherBlock(i);
			for (Condition c : conditions.getSimpleConditions()) {
				EAdCondition cond = this.getSimpleCondition(c);
				if (cond != null) {
					orCondition.addCondition(cond);
				}
			}
			if (orCondition.getConditions().size() > 0) {
				newCondition.addCondition(orCondition);
			}
		}

		return newCondition;
	}

	private EAdCondition getSimpleCondition(Condition c) {
		if (c.getType() == Condition.FLAG_CONDITION) {
			EAdCondition condition = flagConditionImporter
					.init((es.eucm.eadventure.common.data.chapter.conditions.FlagCondition) c);
			return flagConditionImporter
					.convert(
							(es.eucm.eadventure.common.data.chapter.conditions.FlagCondition) c,
							condition);
		} else if (c.getType() == Condition.VAR_CONDITION) {
			EAdCondition condition = varConditionImporter
					.init((es.eucm.eadventure.common.data.chapter.conditions.VarCondition) c);
			return varConditionImporter
					.convert(
							(es.eucm.eadventure.common.data.chapter.conditions.VarCondition) c,
							condition);
		} else if (c.getType() == Condition.GLOBAL_STATE_CONDITION) {
			return factory.getGlobalStateCondition(((GlobalStateCondition) c)
					.getId());
		}
		return null;
	}

}
