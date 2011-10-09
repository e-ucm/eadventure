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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.CancelActionEffect;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState.Modification;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class ActionImporter implements EAdElementImporter<Action, EAdAction> {

	private StringHandler stringHandler;

	private EffectsImporterFactory effectsImporterFactory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	public static final String DRAWABLE_PATH = "@" + ResourceImporter.DRAWABLE;

	@Inject
	public ActionImporter(StringHandler stringHandler,
			EffectsImporterFactory effectsImporterFactory,
			ResourceImporter resourceImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter) {
		this.stringHandler = stringHandler;
		this.effectsImporterFactory = effectsImporterFactory;
		this.conditionsImporter = conditionsImporter;
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdAction init(Action oldObject) {
		return new EAdBasicAction(oldObject.getTargetId() + "_action");
	}

	@Override
	public EAdAction convert(Action oldObject, Object object) {
		return null;
	}

	public EAdAction convert(Action oldObject, Object object, EAdActor actor) {
		EAdBasicAction action = (EAdBasicAction) object;

		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);
		if (condition != null)
			action.setCondition(condition);
		else
			action.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);

		EAdMacro effects = new EAdMacroImpl("actionEffects");
		EAdTriggerMacro triggerEffects = new EAdTriggerMacro(
				"actionEffectTrigger", effects);
		triggerEffects.setCondition(action.getCondition());
		action.getEffects().add(triggerEffects);

		String actionName = "Action";
		if (oldObject instanceof CustomAction) {
			CustomAction customAction = (CustomAction) oldObject;
			actionName = customAction.getName();
		} else {
			actionName = getActionName(oldObject.getType());
		}

		stringHandler.setString(action.getName(), actionName);

		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect effect = effectsImporterFactory.getEffect(e);
			if (effect != null)
				effects.getEffects().add(effect);
		}

		if (oldObject.getNotEffects() != null
				&& !oldObject.getNotEffects().isEmpty()) {
			EAdMacro notEffects = new EAdMacroImpl("actionNotEffects");
			EAdTriggerMacro triggerNotEffects = new EAdTriggerMacro(
					"actionNotEffectTrigger", notEffects);
			triggerNotEffects.setCondition(new NOTCondition(action
					.getCondition()));
			action.getEffects().add(triggerNotEffects);
			action.setCondition(EmptyCondition.TRUE_EMPTY_CONDITION);

			for (Effect e : oldObject.getEffects().getEffects()) {
				EAdEffect effect = effectsImporterFactory.getEffect(e);
				if (effect != null)
					notEffects.getEffects().add(effect);
			}
		}

		// FIXME keep distance?

		if (oldObject instanceof CustomAction) {
			// TODO highlight and pressed are now appearances, but resource
			// converter does not support it.
			// old resources are named "buttonOver" and "buttonPressed"

			Map<String, String> resourcesStrings = new HashMap<String, String>();
			Map<String, Object> resourcesClasses = new HashMap<String, Object>();

			EAdBundleId temp = action.getInitialBundle();
			action.setInitialBundle(action.getHighlightBundle());

			resourcesStrings.put("buttonOver", EAdBasicAction.appearance);
			resourcesClasses.put("buttonOver", ImageImpl.class);
			resourceImporter.importResources(action,
					((CustomAction) oldObject).getResources(),
					resourcesStrings, resourcesClasses);

			action.setInitialBundle(temp);

			resourcesStrings.clear();
			resourcesClasses.clear();
			resourcesStrings.put("buttonNormal", EAdBasicAction.appearance);
			resourcesClasses.put("buttonNormal", ImageImpl.class);

			resourceImporter.importResources(action,
					((CustomAction) oldObject).getResources(),
					resourcesStrings, resourcesClasses);
		} else {
			action.getResources().addAsset(action.getNormalBundle(),
					EAdBasicAction.appearance,
					new ImageImpl(getDrawablePath(oldObject.getType())));
			action.getResources()
					.addAsset(
							action.getHighlightBundle(),
							EAdBasicAction.appearance,
							new ImageImpl(getHighlightDrawablePath(oldObject
									.getType())));

			List<EAdEffect> list = getEffects(oldObject.getType(), oldObject
					.getEffects().getEffects(), actor);
			for (EAdEffect e : list)
				action.getEffects().add(e);
		}

		return action;
	}

	public static String getDrawablePath(int actionType) {
		String image = null;
		switch (actionType) {
		case Action.DRAG_TO:
			image = "drag-normal.png";
			break;
		case Action.GIVE_TO:
			image = "giveto-normal.png";
			break;
		case Action.GRAB:
			image = "grab-normal.png";
			break;
		case Action.USE:
			image = "use-normal.png";
			break;
		case Action.USE_WITH:
			image = "usewith-normal.png";
			break;
		case Action.EXAMINE:
			image = "examine-normal.png";
			break;
		case Action.TALK_TO:
			image = "talk-normal.png";
			break;
		default:
			image = "use-normal.png";
		}
		image = DRAWABLE_PATH + "/" + image;

		return image;
	}

	public static List<EAdEffect> getEffects(int actionType,
			List<AbstractEffect> originalList, EAdActor actor) {
		List<EAdEffect> list = new ArrayList<EAdEffect>();
		for (AbstractEffect e : originalList)
			if (e instanceof CancelActionEffect)
				return list;
		switch (actionType) {
		case Action.GRAB:
			EAdModifyActorState modifyState = new EAdModifyActorState(
					"grabEffect", actor, Modification.PLACE_IN_INVENTORY);
			list.add(modifyState);
			break;
		// TODO Effects for the rest of actions
		case Action.DRAG_TO:
		case Action.GIVE_TO:
		case Action.USE:

		case Action.USE_WITH:

		case Action.EXAMINE:

		case Action.TALK_TO:

		default:
		}
		return list;
	}

	public static String getHighlightDrawablePath(int actionType) {
		String image = null;
		switch (actionType) {
		case Action.DRAG_TO:
			image = "drag-pressed.png";
			break;
		case Action.GIVE_TO:
			image = "giveto-pressed.png";
			break;
		case Action.GRAB:
			image = "grab-pressed.png";
			break;
		case Action.USE:
			image = "use-pressed.png";
			break;
		case Action.USE_WITH:
			image = "usewith-pressed.png";
			break;
		case Action.EXAMINE:
			image = "examine-pressed.png";
			break;
		case Action.TALK_TO:
			image = "talk-pressed.png";
			break;
		default:
			image = "use-pressed.png";
		}
		image = DRAWABLE_PATH + "/" + image;

		return image;

	}

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
		default:
			return "Action";
		}
	}

}
