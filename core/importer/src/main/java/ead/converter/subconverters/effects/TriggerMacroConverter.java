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

package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.WaitUntilEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.params.variables.VarDef;
import ead.converter.ModelQuerier;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;

public class TriggerMacroConverter implements
		EffectConverter<MacroReferenceEffect> {

	public static final VarDef<Boolean> IN_MACRO = new VarDef<Boolean>(
			"in_macro", Boolean.class, false);

	private ModelQuerier modelQuerier;

	public TriggerMacroConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
	}

	public List<EAdEffect> convert(MacroReferenceEffect e) {
		ArrayList<EAdEffect> list = new ArrayList<EAdEffect>();
		TriggerMacroEf effect = new TriggerMacroEf();
		EffectsMacro macro = modelQuerier.getMacro(e.getTargetId());
		effect.putMacro(macro, EmptyCond.TRUE);
		list.add(effect);
		// Add IN_MACRO field to hold next effects until the macro ends
		BasicField<Boolean> field = new BasicField<Boolean>(effect, IN_MACRO);
		ChangeFieldEf macroIn = new ChangeFieldEf(field, EmptyCond.TRUE);
		effect.addSimultaneousEffect(macroIn);

		ChangeFieldEf macroOut = new ChangeFieldEf(field, EmptyCond.FALSE);
		macro.getEffects().get(macro.getEffects().size() - 1).addNextEffect(
				macroOut);

		// Waits until the macro ends
		WaitUntilEf wait = new WaitUntilEf(
				new NOTCond(new OperationCond(field)));
		effect.addSimultaneousEffect(wait);

		list.add(wait);
		return list;
	}
}
