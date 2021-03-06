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

package es.eucm.ead.importer.subconverters.actors.actions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.resources.ResourcesConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.ead.legacyplugins.model.LegacyVars;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.ORCond;
import es.eucm.ead.model.elements.effects.ActorActionsEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.predef.effects.MoveActiveElementToMouseEf;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts Actions to scene element definitions
 */
@Singleton
public class ActionsConverter {

	static private Logger logger = LoggerFactory
			.getLogger(ActionsConverter.class);

	private final Image[] actionImages = new Image[] {
			new Image("@drawable/drag-normal.png"),
			new Image("@drawable/giveto-normal.png"),
			new Image("@drawable/grab-normal.png"),
			new Image("@drawable/use-normal.png"),
			new Image("@drawable/usewith-normal.png"),
			new Image("@drawable/examine-normal.png"),
			new Image("@drawable/talk-normal.png"),
			new Image("@drawable/use-normal.png") };

	private final Image[] actionImagesOver = new Image[] {
			new Image("@drawable/drag-pressed.png"),
			new Image("@drawable/giveto-pressed.png"),
			new Image("@drawable/grab-pressed.png"),
			new Image("@drawable/use-pressed.png"),
			new Image("@drawable/usewith-pressed.png"),
			new Image("@drawable/examine-pressed.png"),
			new Image("@drawable/talk-pressed.png"),
			new Image("@drawable/use-pressed.png") };

	private ResourcesConverter resourceConverter;

	private StringsConverter stringsConverter;

	private EffectsConverter effectsConverter;

	private ConditionsConverter conditionsConverter;

	private ModelQuerier modelQuerier;

	// Aux. variables

	/**
	 * Actions converted
	 */
	private EAdList<SceneElementDef> definitions;

	/**
	 * Map holding actions and its related trigger macro effect
	 */
	private Map<Integer, TriggerMacroEf> actions;

	/**
	 * Map holding custom actions and its related trigger macro effect
	 */
	private Map<String, TriggerMacroEf> customActions;

	/**
	 * Map holding custom interact actions and its related trigger macro effect
	 */
	private Map<String, TriggerMacroEf> customInteractActions;

	/**
	 * Map holding actions and its related trigger macro effect
	 */
	private Map<Integer, Condition> actionsConditions;

	/**
	 * Map holding custom actions and its related trigger macro effect
	 */
	private Map<String, Condition> customActionsConditions;

	/**
	 * Map holding custom interact actions and its related trigger macro effect
	 */
	private Map<String, Condition> customInteractActionsConditions;

	/**
	 * Map relating trigger macro effects with their action definition
	 */
	private Map<TriggerMacroEf, SceneElementDef> macros;

	@Inject
	public ActionsConverter(ResourcesConverter resourceConverter,
			StringsConverter stringsConverter,
			EffectsConverter effectsConverter,
			ConditionsConverter conditionsConverter, ModelQuerier modelQuerier) {
		this.resourceConverter = resourceConverter;
		this.stringsConverter = stringsConverter;
		this.effectsConverter = effectsConverter;
		this.conditionsConverter = conditionsConverter;
		this.modelQuerier = modelQuerier;
		// We keep three maps because in actions list we can have repeated
		// actions. In the new model, this actions are all simplified in one
		// definition with one trigger macro with one effect for every of the
		// actions in their conditions.
		actions = new HashMap<Integer, TriggerMacroEf>();
		customActions = new HashMap<String, TriggerMacroEf>();
		customInteractActions = new HashMap<String, TriggerMacroEf>();
		// We keep other three maps to hold visibility conditions for the
		// actions
		actionsConditions = new HashMap<Integer, Condition>();
		customActionsConditions = new HashMap<String, Condition>();
		customInteractActionsConditions = new HashMap<String, Condition>();
		// And finally, a list with all the actions definitions
		definitions = new EAdList<SceneElementDef>();
		// A map relating trigger macro effects with actions definitions. We need them
		// to update the visibility condition
		macros = new LinkedHashMap<TriggerMacroEf, SceneElementDef>();
	}

	/**
	 * Converts a list of actions to a list scene element definitions
	 *
	 * @param owner the owner of the actions
	 * @param ac    the list of actions
	 * @return the list of actions converted
	 */
	public EAdList<SceneElementDef> convert(SceneElementDef owner,
			List<Action> ac) {
		// Clean maps
		actions.clear();
		customActions.clear();
		customInteractActions.clear();
		actionsConditions.clear();
		customActionsConditions.clear();
		customInteractActionsConditions.clear();
		definitions.clear();
		macros.clear();
		// Conversion
		for (Action a : ac) {
			if (a.getType() != Action.DRAG_TO)
				convert(owner, a);
		}
		EAdList<SceneElementDef> actions = new EAdList<SceneElementDef>();
		actions.addAll(definitions);
		return actions;
	}

	/**
	 * Converts an action to a scene element definition
	 *
	 * @param a
	 * @return
	 */
	public void convert(SceneElementDef owner, Action a) {

		TriggerMacroEf triggerMacroEf;
		Condition visibility;

		// Fetch if the there is already an action like "a"
		if (a.getType() == Action.CUSTOM) {
			String name = ((CustomAction) a).getName();
			triggerMacroEf = customActions.get(name);
			visibility = customActionsConditions.get(name);
		} else if (a.getType() == Action.CUSTOM_INTERACT) {
			String name = ((CustomAction) a).getName();
			triggerMacroEf = customInteractActions.get(name);
			visibility = customInteractActionsConditions.get(name);
		} else {
			triggerMacroEf = actions.get(a.getType());
			visibility = actionsConditions.get(a.getType());
		}

		// If not, create one
		boolean created = false;
		if (triggerMacroEf == null) {
			created = true;
			triggerMacroEf = new TriggerMacroEf();
			visibility = conditionsConverter.convert(a.getConditions());

			SceneElementDef def = new SceneElementDef();
			addAppearance(a, def);
			addName(a, def);
			def.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, triggerMacroEf);
			// Add the definition to the list
			definitions.add(def);
			macros.put(triggerMacroEf, def);
		}

		// We add the effects to the macro and updates the visibility condition
		visibility = addEffects(owner, a, triggerMacroEf, visibility);
		// Update visibility
		macros.get(triggerMacroEf).putProperty(ActorActionsEf.VAR_ACTION_COND,
				visibility);

		// We update the maps
		if (a.getType() == Action.CUSTOM) {
			String name = ((CustomAction) a).getName();
			if (created) {
				customActions.put(name, triggerMacroEf);
			}
			customActionsConditions.put(name, visibility);
		} else if (a.getType() == Action.CUSTOM_INTERACT) {
			String name = ((CustomAction) a).getName();
			if (created) {
				customInteractActions.put(name, triggerMacroEf);
			}
			customInteractActionsConditions.put(name, visibility);
		} else {
			if (created) {
				actions.put(a.getType(), triggerMacroEf);
			}
			actionsConditions.put(a.getType(), visibility);
		}
	}

	/**
	 * Add effects to the macro
	 *
	 * @param a
	 * @param triggerMacroEf
	 * @param visibility
	 * @return the visibility condition updated
	 */
	private Condition addEffects(SceneElementDef owner, Action a,
			TriggerMacroEf triggerMacroEf, Condition visibility) {
		List<Effect> effects = effectsConverter.convert(a.getEffects());
		if (effects.size() == 0) {
			logger.debug("Action '" + getActionName(a.getType())
					+ "' of element '" + owner.getId() + "' has no effects.");
		}
		// I think that click effects should always be empty for actions, but... whatever
		List<Effect> clickEffects = effectsConverter.convert(a
				.getClickEffects());
		if (clickEffects.size() > 0) {
			if (effects.size() > 0) {
				effects.get(effects.size() - 1).addNextEffect(
						clickEffects.get(0));
			}
			effects.addAll(clickEffects);
		}

		Effect firstEffect = null;
		// Add move to, if necessary
		if (modelQuerier.getAventureData().getPlayerMode() == AdventureData.MODE_PLAYER_3RDPERSON) {
			MoveActiveElementToMouseEf moveTo = new MoveActiveElementToMouseEf();
			moveTo.setTarget(owner);
			firstEffect = moveTo;
			if (effects.size() > 0) {
				moveTo.addNextEffect(effects.get(0));
			}
			effects.add(0, moveTo);
		} else if (effects.size() > 0) {
			firstEffect = effects.get(0);
		}

		Condition condition = conditionsConverter.convert(a.getConditions());
		if (effects.size() > 0) {
			// We add the effect to the macro
			if (firstEffect != null) {
				triggerMacroEf.putEffect(condition, firstEffect);
			}
			// We expand the visibility condition
			visibility = new ORCond(visibility, condition);
		}

		// If the action has not effects, it is always visible. By the way, the OR will be simplified in
		// the writing process to TRUE, even if the condition is modified by posteriors actions
		if (a.isActivatedNotEffects()) {
			List<Effect> notEffects = effectsConverter.convert(a
					.getNotEffects());
			if (notEffects.size() > 0) {
				triggerMacroEf.putEffect(new NOTCond(condition), notEffects
						.get(0));
				visibility = new ORCond(EmptyCond.TRUE);
			}
		}
		return visibility;
	}

	/**
	 * Adds the name of the action
	 *
	 * @param a
	 * @param def
	 */
	private void addName(Action a, SceneElementDef def) {
		String name = null;
		if (a instanceof CustomAction) {
			CustomAction ca = (CustomAction) a;
			name = ca.getName();
		} else {
			name = getActionName(a.getType());
		}
		// In eAd1, expressions are not evaluated for action names
		EAdString string = stringsConverter.convert(name, false);
		def.putProperty(LegacyVars.BUBBLE_NAME, string);
	}

	/**
	 * Add appearance to the button
	 *
	 * @param a
	 * @param def
	 */
	protected void addAppearance(Action a, SceneElementDef def) {
		if (a instanceof CustomAction) {
			// Custom actions has their own images
			CustomAction ca = (CustomAction) a;
			if (ca.getResources().size() > 1) {
				logger
						.warn(
								"Weird. Custom action {} contains more than one set of resources. Custom actions should only contain one set of resources",
								((CustomAction) a).getName());
			}
			// Possible resources: (not in constants) "buttonOver",
			// "buttonNormal", "buttonSound", "actionAnimation"

			// XXX Resource group Animations: "actionAnimation"
			// XXX Resource group Button: "buttonSound"
			Resources r = ca.getResources().get(0);

			String appearance = r.getAssetPath("buttonNormal");
			String overAppearance = r.getAssetPath("buttonOver");

			if (appearance != null) {
				def.setAppearance(new Image(resourceConverter
						.getPath(appearance)));
			} else {
				def.setAppearance(getImage(Action.USE, false));
			}

			if (overAppearance != null) {
				def.setOverAppearance(new Image(resourceConverter
						.getPath(overAppearance)));
			} else {
				def.setOverAppearance(getImage(Action.USE, true));
			}

		} else {
			// Normal actions use standard images
			def.setAppearance(getImage(a.getType(), false));
			def.setOverAppearance(getImage(a.getType(), true));
		}
	}

	/**
	 * Returns the corresponding image for the given action type
	 *
	 * @param actionType
	 * @return
	 */
	public Image getImage(int actionType, boolean over) {
		int image;
		switch (actionType) {
		case Action.DRAG_TO:
			image = 0;
			break;
		case Action.GIVE_TO:
			image = 1;
			break;
		case Action.GRAB:
			image = 2;
			break;
		case Action.USE:
			image = 3;
			break;
		case Action.USE_WITH:
			image = 4;
			break;
		case Action.EXAMINE:
			image = 5;
			break;
		case Action.TALK_TO:
			image = 6;
			break;
		default:
			image = 0;
		}
		return over ? actionImagesOver[image] : actionImages[image];
	}

	/**
	 * Returns the name (in English) of the given action
	 *
	 * @param actionType
	 * @return
	 */
	private String getActionName(int actionType) {
		switch (actionType) {
		case Action.DRAG_TO:
			return "Drag to";
		case Action.EXAMINE:
			return "Examine";
		case Action.GIVE_TO:
			return "Give to";
		case Action.GRAB:
			return "Grab";
		case Action.TALK_TO:
			return "Talk to";
		case Action.USE:
			return "Use";
		case Action.USE_WITH:
			return "Use with";
		case Action.CUSTOM:
			return "Custom";
		case Action.CUSTOM_INTERACT:
			return "Custom interact";
		default:
			return "Action";
		}
	}

}
