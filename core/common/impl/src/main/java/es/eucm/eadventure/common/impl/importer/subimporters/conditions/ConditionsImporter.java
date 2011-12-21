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
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadventure.common.data.chapter.conditions.VarCondition;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.ORCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class ConditionsImporter implements
		EAdElementImporter<Conditions, EAdCondition> {

	private EAdElementImporter<FlagCondition, OperationCondition> flagConditionImporter;
	private EAdElementImporter<VarCondition, OperationCondition> varConditionImporter;
	private EAdElementFactory factory;

	@Inject
	public ConditionsImporter(
			EAdElementImporter<FlagCondition, OperationCondition> flagConditionImporter,
			EAdElementImporter<VarCondition, OperationCondition> varConditionImporter,
			EAdElementFactory factory) {
		this.factory = factory;
		this.flagConditionImporter = flagConditionImporter;
		this.varConditionImporter = varConditionImporter;
	}

	@Override
	public EAdCondition init(Conditions oldObject) {
		return new ANDCondition();
	}

	@Override
	public EAdCondition convert(Conditions oldObject, Object object) {
		ANDCondition newCondition = (ANDCondition) object;
		
		if ( oldObject.getSimpleConditions().size() == 0 && oldObject.getEitherConditionsBlockCount() == 0){
			return EmptyCondition.TRUE_EMPTY_CONDITION;
		}
		
		for (Condition c : oldObject.getSimpleConditions()) {
			EAdCondition newC = getSimpleCondition(c);
			if (newC != null) {
				newCondition.addCondition(newC);
			}

		}

		for (int i = 0; i < oldObject.getEitherConditionsBlockCount(); i++) {
			ORCondition orCondition = new ORCondition();
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
