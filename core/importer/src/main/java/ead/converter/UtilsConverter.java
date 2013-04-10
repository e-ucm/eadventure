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

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.interfaces.features.Evented;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.compounds.StateDrawable;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.WatchFieldEv;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.params.variables.EAdVarDef;
import ead.converter.subconverters.conditions.ConditionConverter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

@Singleton
public class UtilsConverter {

	private ConditionConverter conditionsConverter;

	@Inject
	public UtilsConverter(ConditionConverter conditionsConverter) {
		this.conditionsConverter = conditionsConverter;
	}

	public void addResourcesConditions(List<Resources> resources, EAdElement e,
			EAdVarDef<String> stateVar) {
		if (resources.size() > 1) {
			WatchFieldEv watchField = new WatchFieldEv();
			EAdField<String> stateField = new BasicField<String>(e, stateVar);
			// An effect that changes the state attending to the conditions
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			int i = 0;
			for (Resources r : resources) {
				EAdCondition cond = conditionsConverter.convert(r
						.getConditions());
				for (EAdField<?> f : conditionsConverter
						.getFieldsLastCondition()) {
					watchField.watchField(f);
				}
				ChangeFieldEf changeState = new ChangeFieldEf(stateField,
						new ValueOp(this.getResourceBundleId(i)));
				triggerMacro.putEffect(changeState, cond);
			}

			watchField.addEffect(triggerMacro);
			((Evented) e).getEvents().add(watchField);
			i++;
		}
	}

	public String getResourceBundleId(int index) {
		return "bundle" + index;
	}

	/**
	 * Watches the var in the definition
	 * @param sceneElement
	 * @param varState
	 */
	public void addWatchDefinitionField(EAdSceneElement sceneElement,
			EAdVarDef<String> varState) {
		WatchFieldEv watchField = new WatchFieldEv();
		watchField.watchField(new BasicField<String>(sceneElement
				.getDefinition(), varState));
		watchField.addEffect(new ChangeFieldEf(new BasicField<String>(
				sceneElement, varState), new BasicField<String>(sceneElement
				.getDefinition(), varState)));
		sceneElement.getEvents().add(watchField);
	}

	/**
	 * Simplifies a state drawables to its minimum
	 * @param drawable
	 * @return
	 */
	public EAdDrawable simplifyStateDrawable(StateDrawable drawable) {
		if (drawable.getDrawables().isEmpty()) {
			return null;
		} else if (drawable.getDrawablesCollection().size() == 1) {
			return drawable.getDrawable(drawable.getStates().iterator().next());
		}
		return drawable;
	}

	/**
	 * Add a event to watch a condition, and set the field to the value of the condition
	 * @param field
	 * @param conditions
	 */
	public void addWatchCondition(EAdSceneElement sceneElement,
			EAdField<Boolean> field, Conditions c) {
		WatchFieldEv watchField = new WatchFieldEv();
		EAdCondition cond = conditionsConverter.convert(c);
		for (EAdField<?> f : conditionsConverter.getFieldsLastCondition()) {
			watchField.watchField(f);
		}
		watchField.addEffect(new ChangeFieldEf(field, cond));
		sceneElement.getEvents().add(watchField);
	}

}
