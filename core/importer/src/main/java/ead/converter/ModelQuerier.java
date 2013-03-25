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

package ead.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.params.variables.VarDef;
import ead.converter.subconverters.conditions.ConditionConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.Macro;

@Singleton
public class ModelQuerier {

	private ConditionConverter conditionConverter;

	private EffectsConverter effectsConverter;

	private AdventureData adventureData;

	private EAdChapter currentChapter;

	private Map<String, EAdField<Boolean>> flagFields;
	private Map<String, EAdField<Integer>> variableFields;
	private Map<String, EAdCondition> globalStates;
	private Map<String, EffectsMacro> macros;

	@Inject
	public ModelQuerier() {
		flagFields = new HashMap<String, EAdField<Boolean>>();
		variableFields = new HashMap<String, EAdField<Integer>>();
		globalStates = new HashMap<String, EAdCondition>();
		macros = new HashMap<String, EffectsMacro>();
	}

	public void setConditionConverter(ConditionConverter conditionConverter) {
		this.conditionConverter = conditionConverter;
	}

	public void setEffectsConverter(EffectsConverter effectsConverter) {
		this.effectsConverter = effectsConverter;
	}

	public void setAdventureData(AdventureData adventureData) {
		this.adventureData = adventureData;
	}

	public void setCurrentChapter(EAdChapter chapter, Chapter c) {
		this.currentChapter = chapter;
		flagFields.clear();
		variableFields.clear();
		// Add global states
		globalStates.clear();
		for (GlobalState g : c.getGlobalStates()) {
			EAdCondition cond = conditionConverter.convert(g);
			globalStates.put(g.getId(), cond);
		}

		macros.clear();
		// Add macros
		for (Macro m : c.getMacros()) {
			EffectsMacro macro = new EffectsMacro();
			List<EAdEffect> effect = effectsConverter.convert(m);
			if (effect.size() > 0) {
				macro.getEffects().add(effect.get(0));
			}
			macros.put(m.getId(), macro);
		}
	}

	public EAdField<Boolean> getFlag(String id) {
		EAdField<Boolean> field = flagFields.get(id);
		if (field == null) {
			field = new BasicField<Boolean>(currentChapter,
					new VarDef<Boolean>(id, Boolean.class, false));
			flagFields.put(id, field);
		}
		return field;
	}

	public EAdField<Integer> getVariable(String id) {
		EAdField<Integer> field = variableFields.get(id);
		if (field == null) {
			field = new BasicField<Integer>(currentChapter,
					new VarDef<Integer>(id, Integer.class, 0));
			variableFields.put(id, field);
		}
		return field;
	}

	public EAdCondition getGlobalState(String id) {
		return globalStates.get(id);
	}

	public EffectsMacro getMacro(String id) {
		return macros.get(id);
	}

}
