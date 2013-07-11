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

package ead.importer.subimporters.effects;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;

public abstract class EffectImporter<OldEffect extends AbstractEffect, NewEffect extends EAdEffect>
		implements EAdElementImporter<OldEffect, NewEffect> {

	protected static int ID_GENERATOR = 0;

	protected ImportAnnotator annotator;

	private EAdElementImporter<Conditions, EAdCondition> conditionImporter;

	public EffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			ImportAnnotator annotator) {
		this.conditionImporter = conditionImporter;
		this.annotator = annotator;
	}

	protected void importConditions(OldEffect oldEffect, NewEffect newEffect) {
		EAdCondition condition = conditionImporter.init(oldEffect
				.getConditions());
		condition = conditionImporter.convert(oldEffect.getConditions(),
				condition);
		if (condition != null)
			newEffect.setCondition(condition);
	}

	@SuppressWarnings("unchecked")
	public NewEffect convert(OldEffect oldObject, Object newElement) {
		importConditions(oldObject, (NewEffect) newElement);
		return (NewEffect) newElement;
	}

	protected boolean newEffectIsBlocking() {
		return true;
	}

}
