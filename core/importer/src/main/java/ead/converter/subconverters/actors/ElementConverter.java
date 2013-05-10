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

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.converter.ModelQuerier;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.actors.actions.ActionsConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

@Singleton
public abstract class ElementConverter {

	protected ResourcesConverter resourceConverter;

	protected UtilsConverter utilsConverter;

	protected ActionsConverter actionsConverter;

	protected ModelQuerier modelQuerier;

	protected ConditionsConverter conditionsConverter;

	@Inject
	public ElementConverter(ResourcesConverter resourceConverter,
			UtilsConverter utilsConverter, ActionsConverter actionsConverter,
			ModelQuerier modelQuerier, ConditionsConverter conditionsConverter) {
		this.resourceConverter = resourceConverter;
		this.utilsConverter = utilsConverter;
		this.actionsConverter = actionsConverter;
		this.modelQuerier = modelQuerier;
		this.conditionsConverter = conditionsConverter;
	}

	public EAdSceneElementDef convert(Element a) {
		SceneElementDef definition = new SceneElementDef();
		definition.setId(a.getId());
		// Appearance
		convert(a, getResourceType(), definition,
				ResourcedElement.INITIAL_BUNDLE, SceneElementDef.appearance);
		return definition;
	}

	protected EAdSceneElementDef convert(Element a, String resourceType,
			EAdSceneElementDef definition, String bundle, String resourceId) {
		// One bundle for each bundle (DUH)
		int i = 0;
		for (Resources r : a.getResources()) {
			EAdDrawable drawable = getDrawable(r, resourceType);
			if (drawable != null) {
				definition.addAsset(utilsConverter.getResourceBundleId(i),
						resourceId, drawable);
				if (i == 0) {
					definition.setVarInitialValue(SceneElement.VAR_BUNDLE_ID,
							utilsConverter.getResourceBundleId(i));
				}
			}
			i++;
		}

		// Add conditioned resources
		// The variable that changes is IN THE SCENE ELEMENT DEFINITION, so
		// every element that refers to this actor, must watch this field in
		// order to update its own state
		utilsConverter.addResourcesConditions(a.getResources(), definition,
				SceneElement.VAR_BUNDLE_ID);

		return definition;
	}

	public void addActions(Element element, EAdSceneElementDef def) {
		// Add actions
		if (element.getActions().size() > 0) {
			EAdList<EAdSceneElementDef> actions = actionsConverter
					.convert(element.getActions());
			def.setVarInitialValue(ActorActionsEf.VAR_ACTIONS, actions);
			def.addBehavior(modelQuerier.getActionsInteraction(),
					new ActorActionsEf(def));
			// XXX Process actions visibility
			// (actionsConverter.actionsConditions)

			// Add drag & drop
			// NOTE: The old version of eadventure allows complex behaviors with
			// drag and drop. So, a couple of things to note, to make conversion
			// less crazy:
			// 1) If an element has a drag action is ALWAYS draggable (no matter
			// action conditions).
			// 2) Conditions are used to decide what effects to launch

			// First, detect drag
			boolean hasDrag = false;
			for (Action a : element.getActions()) {
				if (a.getType() == Action.DRAG_TO) {
					hasDrag = true;
					break;
				}
			}

			// Then, if it has drag
			if (hasDrag) {

			}
		}
	}

	protected EAdDrawable getDrawable(Resources r, String resourceId) {
		String assetPath = r.getAssetPath(resourceId);
		return assetPath == null ? null : resourceConverter.getImage(assetPath);
	}

	protected abstract String getResourceType();
}
