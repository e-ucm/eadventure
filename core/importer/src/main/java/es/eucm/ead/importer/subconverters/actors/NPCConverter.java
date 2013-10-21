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

package es.eucm.ead.importer.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.elements.enums.CommonStates;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.UtilsConverter;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.actors.actions.ActionsConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

@Singleton
public class NPCConverter extends ElementConverter {

	public static final String[] NPC_STATES = { NPC.RESOURCE_TYPE_STAND_UP,
			NPC.RESOURCE_TYPE_STAND_RIGHT, NPC.RESOURCE_TYPE_STAND_DOWN,
			NPC.RESOURCE_TYPE_STAND_LEFT, NPC.RESOURCE_TYPE_SPEAK_UP,
			NPC.RESOURCE_TYPE_SPEAK_RIGHT, NPC.RESOURCE_TYPE_SPEAK_DOWN,
			NPC.RESOURCE_TYPE_SPEAK_LEFT, NPC.RESOURCE_TYPE_WALK_UP,
			NPC.RESOURCE_TYPE_WALK_RIGHT, NPC.RESOURCE_TYPE_WALK_DOWN,
			NPC.RESOURCE_TYPE_WALK_LEFT, NPC.RESOURCE_TYPE_USE_RIGHT,
			NPC.RESOURCE_TYPE_USE_RIGHT, NPC.RESOURCE_TYPE_USE_LEFT,
			NPC.RESOURCE_TYPE_USE_LEFT };

	public static final String[] NEW_STATES = {
			CommonStates.DEFAULT.toString(), CommonStates.TALKING.toString(),
			CommonStates.WALKING.toString(), CommonStates.USING.toString() };

	@Inject
	public NPCConverter(ResourcesConverter resourceConverter,
			UtilsConverter utilsConverter, ActionsConverter actionsConverter,
			ModelQuerier modelQuerier, ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter, StringsConverter stringsConverter) {
		super(resourceConverter, utilsConverter, actionsConverter,
				modelQuerier, conditionsConverter, effectsConverter,
				stringsConverter);
	}

	public SceneElementDef convert(NPC npc) {
		SceneElementDef def = super.convert(npc);
		return def;
	}

	@Override
	protected EAdDrawable getDrawable(Resources r, String resourceId) {
		StateDrawable states = new StateDrawable();
		int i = 0;
		for (String newState : NEW_STATES) {
			StateDrawable state = new StateDrawable();
			for (int j = 0; j < 4; j++) {
				resourceId = NPC_STATES[i * 4 + j];
				EAdDrawable drawable = resourceConverter.getFramesAnimation(r
						.getAssetPath(resourceId));
				Orientation o = Orientation.N;
				switch (j) {
				case 0:
					o = Orientation.N;
					break;
				case 1:
					o = Orientation.E;
					break;
				case 2:
					o = Orientation.S;
					break;
				case 3:
					o = Orientation.W;
					break;
				}
				state.addDrawable(o.toString(), drawable);
			}
			EAdDrawable result = utilsConverter.simplifyStateDrawable(state);
			if (result != null) {
				states.addDrawable(newState, result);
			}
			i++;
		}
		return utilsConverter.simplifyStateDrawable(states);
	}

	@Override
	protected String getResourceType() {
		return NPC.RESOURCE_TYPE_STAND_UP;
	}

}
