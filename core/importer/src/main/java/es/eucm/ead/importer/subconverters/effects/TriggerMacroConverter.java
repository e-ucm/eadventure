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

package es.eucm.ead.importer.subconverters.effects;

import es.eucm.ead.importer.EAdElementsCache;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.WaitUntilEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;

import java.util.ArrayList;
import java.util.List;

public class TriggerMacroConverter implements
		EffectConverter<MacroReferenceEffect> {

	public static final String IN_MACRO = "in_macro";
	private final EAdElementsCache elementsCache;

	private ModelQuerier modelQuerier;

	public TriggerMacroConverter(ModelQuerier modelQuerier,
			EAdElementsCache elementsCache) {
		this.modelQuerier = modelQuerier;
		this.elementsCache = elementsCache;
	}

	public List<Effect> convert(MacroReferenceEffect e) {
		ArrayList<Effect> list = new ArrayList<Effect>();

		ElementField field = new ElementField(modelQuerier.getCurrentChapter(),
				IN_MACRO + e.getTargetId(), false);
		WaitUntilEf waitMacro = new WaitUntilEf(new NOTCond(new OperationCond(
				field)));

		int ref = elementsCache.newReference(e.getTargetId());
		waitMacro.setId(e.getTargetId() + "$" + ref);

		waitMacro
				.addSimultaneousEffect(new ChangeFieldEf(field, EmptyCond.TRUE));

		EAdList<Effect> macro = modelQuerier.getMacro(e.getTargetId());
		waitMacro.addSimultaneousEffect(macro.get(0));

		list.add(waitMacro);
		return list;
	}
}
