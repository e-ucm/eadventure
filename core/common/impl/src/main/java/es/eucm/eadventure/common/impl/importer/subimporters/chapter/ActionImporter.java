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
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdInventoryEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.enums.InventoryEffectAction;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdInventoryImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.EAdDragEvent;
import es.eucm.eadventure.common.model.guievents.enums.DragAction;
import es.eucm.eadventure.common.model.guievents.impl.EAdDragEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.enums.Alignment;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class ActionImporter implements EAdElementImporter<Action, EAdAction> {

	private StringHandler stringHandler;

	private EffectsImporterFactory effectsImporterFactory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	private EAdElementFactory factory;

	public static final String DRAWABLE_PATH = "@" + ResourceImporter.DRAWABLE;

	@Inject
	public ActionImporter(StringHandler stringHandler,
			EffectsImporterFactory effectsImporterFactory,
			ResourceImporter resourceImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory) {
		this.stringHandler = stringHandler;
		this.effectsImporterFactory = effectsImporterFactory;
		this.conditionsImporter = conditionsImporter;
		this.resourceImporter = resourceImporter;
		this.factory = factory;
	}

	@Override
	public EAdAction init(Action oldObject) {
		EAdAction basicAction = new EAdBasicAction();
		basicAction.setId(oldObject.getTargetId() + "_action");
		return basicAction;
	}

	@Override
	public EAdAction convert(Action oldObject, Object object) {
		return null;
	}

	public EAdCondition setCondition(Action oldObject, EAdAction action,
			EAdCondition previousCondition) {
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		if (previousCondition == null && condition == null)
			return EmptyCondition.TRUE_EMPTY_CONDITION;
		else if (previousCondition != null && condition != null)
			return new ANDCondition(condition, new NOTCondition(
					previousCondition));
		else if (condition == null)
			return previousCondition;
		else
			return condition;

	}

	public EAdAction convert(Action oldObject, EAdBasicAction action,
			EAdSceneElementDef owner, EAdCondition condition,
			boolean isActiveArea) {

		// Action name
		setName(oldObject, action);

		// Add appearance
		addAppearance(oldObject, action);

		// Add effects
		addEffects(oldObject, action, owner, condition, isActiveArea);

		return action;
	}

	private void setName(Action oldObject, EAdBasicAction action) {
		String actionName = "Action";
		if (oldObject instanceof CustomAction) {
			CustomAction customAction = (CustomAction) oldObject;
			actionName = customAction.getName();
		} else {
			actionName = getActionName(oldObject.getType());
		}
		stringHandler.setString(action.getName(), actionName);
	}

	private void addEffects(Action oldObject, EAdBasicAction action,
			EAdSceneElementDef actor, EAdCondition condition,
			boolean isActiveArea) {
		// Add effects
		EAdTriggerMacro triggerEffects = effectsImporterFactory
				.getTriggerEffects(oldObject.getEffects());

		// Add default effects for the action
		EAdEffect defaultEffect = getDefaultEffects(oldObject, actor,
				isActiveArea, action);
		if (defaultEffect != null) {
			if (triggerEffects == null) {
				triggerEffects = new EAdTriggerMacro(new EAdMacroImpl());
			}
			triggerEffects.getMacro().getEffects().add(defaultEffect, 0);
		}

		// Add conditions and get to
		if (triggerEffects != null) {
			triggerEffects.setId("actionEffectTrigger");
			triggerEffects.setCondition(condition);

			if (!factory.isFirstPerson() && oldObject.isNeedsGoTo()) {
				EAdMoveActiveElement moveActiveElement = new EAdMoveActiveElement();
				moveActiveElement.setId("moveToActionTarget");
				moveActiveElement.setTarget(actor);
				moveActiveElement.getNextEffects().add(triggerEffects);
				action.getEffects().add(moveActiveElement);
			} else {
				action.getEffects().add(triggerEffects);
			}
		}

		// Add no effects
		EAdTriggerMacro triggerNotEffects = effectsImporterFactory
				.getTriggerEffects(oldObject.getNotEffects());
		if (triggerNotEffects != null) {
			triggerNotEffects.setId("actionNotEffectTrigger");
			triggerNotEffects.setCondition(new NOTCondition(condition));
			action.getEffects().add(triggerNotEffects);
		}
	}

	private void addAppearance(Action oldObject, EAdBasicAction action) {
		// If it's a standard action
		if (oldObject.getType() != Action.CUSTOM
				&& oldObject.getType() != Action.CUSTOM_INTERACT) {
			action.getResources().addAsset(action.getNormalBundle(),
					EAdBasicAction.appearance,
					new ImageImpl(getDrawablePath(oldObject.getType())));
			action.getResources()
					.addAsset(
							action.getHighlightBundle(),
							EAdBasicAction.appearance,
							new ImageImpl(getHighlightDrawablePath(oldObject
									.getType())));
		} else {
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
		}
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

	public EAdEffect getDefaultEffects(Action a, EAdSceneElementDef actor,
			boolean isActiveArea, EAdAction newAction) {
		for (AbstractEffect e : a.getEffects().getEffects())
			if (e instanceof CancelActionEffect)
				return null;
		switch (a.getType()) {
		case Action.GRAB:
			if (!isActiveArea) {

				EAdField<EAdSceneElement> sceneElement = new EAdFieldImpl<EAdSceneElement>(
						actor, EAdSceneElementDefImpl.VAR_SCENE_ELEMENT);

				EAdField<Boolean> inInventory = new EAdFieldImpl<Boolean>(
						sceneElement, EAdInventoryImpl.VAR_IN_INVENTORY);

				EAdInventoryEffect addToInventory = new EAdInventoryEffect(
						actor, InventoryEffectAction.ADD_TO_INVENTORY);
				addToInventory.setId("grabEffect");

				OperationCondition c = new OperationCondition(inInventory);
				addToInventory.setCondition(new NOTCondition(c));

				EAdChangeFieldValueEffect change = new EAdChangeFieldValueEffect();
				change.addField(inInventory);
				change.setOperation(BooleanOperation.TRUE_OP);

				addToInventory.getNextEffects().add(change);

				return addToInventory;
			}
			break;
		}
		return null;
	}

	private static EAdString examineString;

	private static Image examineImage, examineOverImage;

	private static void initExamineAction(StringHandler handler) {
		examineString = EAdString.newEAdString("examineActionName");
		handler.setString(examineString, "Examine");

		examineImage = new ImageImpl(getDrawablePath(Action.EXAMINE));
		examineOverImage = new ImageImpl(
				getHighlightDrawablePath(Action.EXAMINE));
	}

	/**
	 * Add examine if there's no examine action added
	 * 
	 * @param actor
	 *            the new actor
	 * @param element
	 *            the old element
	 */
	public void addExamine(EAdSceneElementDef actor, List<Action> actionsList) {
		boolean add = true;
		for (Action a : actionsList) {
			if (a.getType() == Action.EXAMINE) {
				add = false;
			}
		}

		if (add) {
			if (examineString == null)
				initExamineAction(stringHandler);

			EAdBasicAction examineAction = new EAdBasicAction(examineString);
			examineAction.setId(actor.getId() + "_examinate");

			// Effect
			EAdSpeakEffect effect = new EAdSpeakEffect(actor.getDetailDesc());
			effect.setId("examinate");
			effect.setAlignment(Alignment.CENTER);

			stringHandler.setString(examineAction.getName(), "Examine");
			examineAction.getEffects().add(effect);

			// Appearance
			examineAction.getResources().addAsset(
					examineAction.getNormalBundle(), EAdBasicAction.appearance,
					examineImage);
			examineAction.getResources().addAsset(
					examineAction.getHighlightBundle(),
					EAdBasicAction.appearance, examineOverImage);

			actor.getActions().add(examineAction);
		}
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

	public void addAllActions(List<Action> actionsList,
			EAdSceneElementDefImpl actor, boolean isActiveArea) {

		// add examine
		addExamine(actor, actionsList);

		HashMap<Integer, EAdAction> actions = new HashMap<Integer, EAdAction>();
		HashMap<String, EAdAction> customActions = new HashMap<String, EAdAction>();
		HashMap<EAdAction, EAdCondition> previousConditions = new HashMap<EAdAction, EAdCondition>();

		for (Action a : actionsList) {
			EAdAction action = null;

			// Init action
			switch (a.getType()) {
			case Action.CUSTOM:
				CustomAction customAction = (CustomAction) a;
				if (customActions.containsKey(customAction.getName())) {
					action = customActions.get(customAction.getName());
				} else {
					action = init(a);
					actor.getActions().add(action);
					customActions.put(customAction.getName(), action);
				}
				break;
			case Action.EXAMINE:
			case Action.USE:
			case Action.GRAB:
			case Action.TALK_TO:
			case Action.GIVE_TO:
			case Action.USE_WITH:
				if (actions.containsKey(a.getType())) {
					action = actions.get(a.getType());
				} else {
					action = init(a);
					actor.getActions().add(action);
					actions.put(a.getType(), action);
				}
				break;

			}

			// Set condition
			EAdCondition c = setCondition(a, action,
					previousConditions.get(action));
			previousConditions.put(action, c);

			// Add effects
			if (isInteraction(a)) {
				addInteraction(a, actor, c);

			} else
				action = convert(a, (EAdBasicAction) action, actor, c, isActiveArea);

		}
	}

	private boolean isInteraction(Action a) {
		return a.getType() == Action.GIVE_TO || a.getType() == Action.USE_WITH
				|| a.getType() == Action.CUSTOM_INTERACT;
	}

	private void addInteraction(Action a, EAdSceneElementDefImpl actor,
			EAdCondition condition) {

		EAdTriggerMacro effect = effectsImporterFactory.getTriggerEffects(a
				.getEffects());
		EAdInventoryEffect removeFromInventory = new EAdInventoryEffect(actor,
				InventoryEffectAction.REMOVE_FROM_INVENTORY);
		if (!hasCancelEffect(a.getEffects())) {
			effect.getNextEffects().add(removeFromInventory);
		}
		effect.setCondition(condition);

		EAdSceneElementDefImpl target = (EAdSceneElementDefImpl) factory
				.getElementById(a.getTargetId());
		EAdDragEvent dragEvent = new EAdDragEventImpl(actor, DragAction.DROP);
		target.addBehavior(dragEvent, effect);

	}

	private boolean hasCancelEffect(Effects effects) {
		if (effects == null)
			return false;

		for (AbstractEffect e : effects.getEffects()) {
			if (e instanceof CancelActionEffect)
				return true;
		}
		return false;
	}

}
