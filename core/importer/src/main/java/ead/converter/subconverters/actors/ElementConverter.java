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
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.ResourcedElement;
import es.eucm.ead.model.elements.effects.ActorActionsEf;
import es.eucm.ead.model.elements.effects.DragEf;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.scenes.EAdSceneElementDef;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import ead.converter.ModelQuerier;
import ead.converter.StringsConverter;
import ead.converter.UtilsConverter;
import ead.converter.resources.ResourcesConverter;
import ead.converter.subconverters.actors.actions.ActionsConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import ead.plugins.engine.bubbledescription.BubbleNameEv;
import es.eucm.eadventure.common.data.adventure.DescriptorData;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

import java.util.ArrayList;
import java.util.List;

@Singleton
public abstract class ElementConverter {

	protected ResourcesConverter resourceConverter;

	protected UtilsConverter utilsConverter;

	protected ActionsConverter actionsConverter;

	protected ModelQuerier modelQuerier;

	protected ConditionsConverter conditionsConverter;

	protected EffectsConverter effectsConverter;

	private List<DropEvent> dropEvents;

	private StringsConverter stringsConverter;

	@Inject
	public ElementConverter(ResourcesConverter resourceConverter,
			UtilsConverter utilsConverter, ActionsConverter actionsConverter,
			ModelQuerier modelQuerier, ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter, StringsConverter stringsConverter) {
		this.resourceConverter = resourceConverter;
		this.utilsConverter = utilsConverter;
		this.actionsConverter = actionsConverter;
		this.modelQuerier = modelQuerier;
		this.conditionsConverter = conditionsConverter;
		this.effectsConverter = effectsConverter;
		this.dropEvents = new ArrayList<DropEvent>();
		this.stringsConverter = stringsConverter;
	}

	public EAdSceneElementDef convert(Element a) {
		SceneElementDef definition = new SceneElementDef();
		definition.setId(a.getId());
		// Appearance
		convert(a, getResourceType(), definition,
				ResourcedElement.INITIAL_BUNDLE, SceneElementDef.appearance);
		// Descriptions (Player has no description)
        if ( !Player.IDENTIFIER.endsWith(a.getId())){
		    addDescription(a, definition);
        }
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

	public void addDescription(Element element, EAdSceneElementDef def) {
		// XXX Multiple descriptions
		if (element.getDescriptions().size() > 0) {
			String name = element.getDescription(0).getName();
			if (!name.equals("")) {
				EAdString string = stringsConverter.convert(name, true);
				def.setVarInitialValue(BubbleNameEv.VAR_BUBBLE_NAME, string);
				def.setVarInitialValue(BubbleNameEv.VAR_BUBBLE_OPERATIONS,
						stringsConverter.getOperations(name));
			}
			// Descriptions are only showed if the default click action is "show details"
			if (modelQuerier.getAventureData().getDefaultClickAction() == DescriptorData.DefaultClickAction.SHOW_DETAILS) {
				String description = element.getDescription(0).getName();
				SpeakEf speakDesc = modelQuerier.getSpeakFor(Player.IDENTIFIER,
						stringsConverter.convert(description, false));
				def.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, speakDesc);
			}
		}
	}

	public void addActions(Element element, EAdSceneElementDef def) {
		// Add actions
		if (element.getActions().size() > 0) {
			EAdList<EAdSceneElementDef> actions = actionsConverter.convert(def,
					element.getActions());
			def.setVarInitialValue(ActorActionsEf.VAR_ACTIONS, actions);
			modelQuerier.addActionsInteraction(def, new ActorActionsEf(def));

			// Add drag & drop
			TriggerMacroEf drags = null;
			for (Action a : element.getActions()) {
				if (a.getType() == Action.DRAG_TO) {
					if (drags == null) {
						drags = new TriggerMacroEf();
					}
					// click effects and effects and action are the same, so add all
					EAdCondition cond = conditionsConverter.convert(a
							.getConditions());
					DragEf drag = new DragEf();
					drag.setReturnAfterDrag(element.isReturnsWhenDragged());
					drags.putEffect(cond, drag);

					dropEvents.add(new DropEvent(element.getId(), a
							.getTargetId(), effectsConverter.convert(a
							.getEffects())));
				}
			}

			if (drags != null) {
				def.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, drags);
			}

			if (element.getActions().size() > 0) {
				// XXX For now, we use the default exit image
				utilsConverter.addCursorChange(def, MouseHud.EXIT_CURSOR);
			}

		}
	}

	public List<DropEvent> getDropEvents() {
		return dropEvents;
	}

	protected EAdDrawable getDrawable(Resources r, String resourceId) {
		String assetPath = r.getAssetPath(resourceId);
		return assetPath == null ? null : resourceConverter.getImage(assetPath);
	}

	protected abstract String getResourceType();

	public static class DropEvent {
		public String owner;

		public String target;

		public EAdList<EAdEffect> effects;

		public DropEvent(String owner, String target, EAdList<EAdEffect> effects) {
			this.owner = owner;
			this.target = target;
			this.effects = effects;
		}

	}
}
