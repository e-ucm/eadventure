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

package ead.converter.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.ModelQuerier;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.actors.actions.ActionsConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.elements.Item;

@Singleton
public class ItemConverter extends ElementConverter {

	@Inject
	public ItemConverter(ResourcesConverter resourceConverter,
			UtilsConverter utilsConverter, ActionsConverter actionsConverter,
			ModelQuerier modelQuerier, ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter) {
		super(resourceConverter, utilsConverter, actionsConverter,
				modelQuerier, conditionsConverter, effectsConverter);
	}

	public EAdSceneElementDef convert(Element a) {
		EAdSceneElementDef definition = super.convert(a);
		convert(a, Item.RESOURCE_TYPE_IMAGEOVER, definition,
				ResourcedElement.INITIAL_BUNDLE, SceneElementDef.overAppearance);
		return definition;
	}

	@Override
	public String getResourceType() {
		return Item.RESOURCE_TYPE_IMAGE;
	}

	public void addActions(Element e, EAdSceneElementDef def) {
		Item i = (Item) e;
		// If first action, the first action that meets the conditions is launched
		if (i.getBehaviour() == Item.BehaviourType.FIRST_ACTION) {
			TriggerMacroEf action = new TriggerMacroEf();
			for (Action a : e.getActions()) {
				EAdCondition cond = conditionsConverter.convert(a
						.getConditions());
				EAdList<EAdEffect> effects = effectsConverter.convert(a
						.getEffects());
				action.putEffects(cond, effects);
			}
			def.addBehavior(modelQuerier.getActionsInteraction(), action);
			// XXX For now, we use the default exit image
			utilsConverter.addCursorChange(def, MouseHud.EXIT_CURSOR);
		} else {
			super.addActions(e, def);
		}
	}

}
