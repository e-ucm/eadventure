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

package es.eucm.eadventure.common.impl.importer.subimporters.effects.variables;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.IncrementVarEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectImporter;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

public class IncrementVarImporter extends
		EffectImporter<IncrementVarEffect, EAdChangeVarValueEffect> {
	private EAdElementFactory factory;

	private static int ID_GENERATOR = 0;

	@Inject
	public IncrementVarImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdChangeVarValueEffect init(IncrementVarEffect oldObject) {
		EAdVar<?> var = factory.getVarByOldId(oldObject.getTargetId(),
				Condition.VAR_CONDITION);

		LiteralExpressionOperation op = new LiteralExpressionOperation(
				"literalExpression", "[0] + "
						+ oldObject.getIncrement(), var);
		EAdChangeVarValueEffect effect = new EAdChangeVarValueEffect(
				"changeVarValueFromIncrement" + ID_GENERATOR++, var, op);
		super.importConditions(oldObject, effect);
		effect.setQueueable(true);
		return effect;
	}
	
	@Override
	public EAdChangeVarValueEffect convert(IncrementVarEffect oldObject, Object object) {
		return (EAdChangeVarValueEffect) object;
	}
}
