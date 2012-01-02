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

package ead.common.importer.subimporters.chapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.EffectsImporterFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.EAdElement;
import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.InventoryImpl;
import ead.common.model.elements.actions.ActionImpl;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.enums.InventoryEffectAction;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.guievents.EAdDragEvent;
import ead.common.model.elements.guievents.enums.DragEventType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.FieldImpl;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.predef.effects.MoveActiveElementEf;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.basics.enums.Alignment;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.CancelActionEffect;
import es.eucm.eadventure.common.data.chapter.effects.Effects;

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
		EAdAction basicAction = new ActionImpl();
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
			return EmptyCond.TRUE_EMPTY_CONDITION;
		else if (previousCondition != null && condition != null)
			return new ANDCond(condition, new NOTCond(
					previousCondition));
		else if (condition == null)
			return previousCondition;
		else
			return condition;

	}

	public EAdAction convert(Action oldObject, ActionImpl action,
			EAdSceneElementDef owner, EAdCondition condition,
			boolean isActiveArea, TriggerMacroEf trigger ) {

		// Action name
		setName(oldObject, action);

		// Add appearance
		addAppearance(oldObject, action);

		// Add effects
		addEffects(trigger, oldObject, action, owner, condition, isActiveArea );

		return action;
	}

	private void setName(Action oldObject, ActionImpl action) {
		String actionName = "Action";
		if (oldObject instanceof CustomAction) {
			CustomAction customAction = (CustomAction) oldObject;
			actionName = customAction.getName();
		} else {
			actionName = getActionName(oldObject.getType());
		}
		stringHandler.setString(action.getName(), actionName);
	}

	private void addEffects(TriggerMacroEf triggerEffect, Action oldObject, ActionImpl action,
			EAdSceneElementDef actor, EAdCondition condition,
			boolean isActiveArea) {
		// Add effects
		EffectsMacro macro = effectsImporterFactory
				.getMacroEffects(oldObject.getEffects());

		// Add default effects for the action
		EAdEffect defaultEffect = getDefaultEffects(oldObject, actor,
				isActiveArea, action);
		
		if (defaultEffect != null) {
			if (macro == null) {
				macro = new EffectsMacro();
			}
			macro.getEffects().add(defaultEffect, 0);
		}

		// Add conditions and get to
		if (macro != null) {
			macro.setId("actionMacro");
			triggerEffect.putMacro(macro, condition);
		}

		// Add no effects
		EffectsMacro NotEffects = effectsImporterFactory
				.getMacroEffects(oldObject.getNotEffects());
		if (NotEffects != null) {
			NotEffects.setId("actionNotEffectTrigger");
			triggerEffect.putMacro(macro, new NOTCond(condition));
		}
	}

	private void addAppearance(Action oldObject, ActionImpl action) {
		// If it's a standard action
		if (oldObject.getType() != Action.CUSTOM
				&& oldObject.getType() != Action.CUSTOM_INTERACT) {
			action.getResources().addAsset(action.getNormalBundle(),
					ActionImpl.appearance,
					new ImageImpl(getDrawablePath(oldObject.getType())));
			action.getResources()
					.addAsset(
							action.getHighlightBundle(),
							ActionImpl.appearance,
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

			resourcesStrings.put("buttonOver", ActionImpl.appearance);
			resourcesClasses.put("buttonOver", ImageImpl.class);
			resourceImporter.importResources(action,
					((CustomAction) oldObject).getResources(),
					resourcesStrings, resourcesClasses);

			action.setInitialBundle(temp);

			resourcesStrings.clear();
			resourcesClasses.clear();
			resourcesStrings.put("buttonNormal", ActionImpl.appearance);
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

				EAdField<EAdSceneElement> sceneElement = new FieldImpl<EAdSceneElement>(
						actor, SceneElementDefImpl.VAR_SCENE_ELEMENT);

				EAdField<Boolean> inInventory = new FieldImpl<Boolean>(
						sceneElement, InventoryImpl.VAR_IN_INVENTORY);

				ModifyInventoryEf addToInventory = new ModifyInventoryEf(
						actor, InventoryEffectAction.ADD_TO_INVENTORY);
				addToInventory.setId("grabEffect");

				OperationCond c = new OperationCond(inInventory);
				addToInventory.setCondition(new NOTCond(c));

				ChangeFieldEf change = new ChangeFieldEf();
				change.addField(inInventory);
				change.setOperation(BooleanOp.TRUE_OP);

				addToInventory.getNextEffects().add(change);

				return addToInventory;
			}
			break;
		}
		return null;
	}

	private static EAdString examineString;

	private static ImageImpl examineImage, examineOverImage;

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

			ActionImpl examineAction = new ActionImpl(examineString);
			examineAction.setId(actor.getId() + "_examinate");

			// Effect
			SpeakEf effect = new SpeakEf(actor.getDetailDesc());
			effect.setId("examinate");
			effect.setAlignment(Alignment.CENTER);

			stringHandler.setString(examineAction.getName(), "Examine");
			examineAction.getEffects().add(effect);

			// Appearance
			examineAction.getResources().addAsset(
					examineAction.getNormalBundle(), ActionImpl.appearance,
					examineImage);
			examineAction.getResources().addAsset(
					examineAction.getHighlightBundle(),
					ActionImpl.appearance, examineOverImage);

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
			SceneElementDefImpl actor, boolean isActiveArea) {

		// add examine
		addExamine(actor, actionsList);

		HashMap<Integer, EAdAction> actions = new HashMap<Integer, EAdAction>();
		HashMap<String, EAdAction> customActions = new HashMap<String, EAdAction>();
		HashMap<String, EAdAction> interactActions = new HashMap<String, EAdAction>();
		HashMap<EAdAction, EAdCondition> previousConditions = new HashMap<EAdAction, EAdCondition>();
		HashMap<EAdAction, TriggerMacroEf> triggers = new HashMap<EAdAction, TriggerMacroEf>();
		HashMap<EAdAction, EAdSceneElementDef> targets = new HashMap<EAdAction, EAdSceneElementDef>();
		HashMap<EAdAction, Boolean> getsTo = new HashMap<EAdAction, Boolean>();

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
				if (actions.containsKey(a.getType())) {
					action = actions.get(a.getType());
				} else {
					action = init(a);
					actor.getActions().add(action);
					actions.put(a.getType(), action);
				}
				break;
			case Action.CUSTOM_INTERACT:
				customAction = (CustomAction) a;
				String name = customAction.getName() + ""
						+ customAction.getTargetId();
				if (interactActions.containsKey(name)) {
					action = actions.get(name);
				} else {
					action = init(a);
					interactActions.put(name, action);
				}
				break;
			case Action.DRAG_TO:
			case Action.GIVE_TO:
			case Action.USE_WITH:
				name = a.getType() + "" + a.getTargetId();
				if (interactActions.containsKey(name)) {
					action = interactActions.get(name);
				} else {
					action = init(a);
					interactActions.put(name, action);
				}
				break;

			}
			
			getsTo.put(action, a.isNeedsGoTo());
			
			TriggerMacroEf trigger = triggers.get(action);
			if ( trigger == null ){
				trigger = new TriggerMacroEf();
				triggers.put(action, trigger);
			}

			// Set condition
			EAdCondition c = setCondition(a, action,
					previousConditions.get(action));
			previousConditions.put(action, c);

			// Add effects
			if (a.getType() == Action.DRAG_TO) {
				EAdSceneElementDef target = addDrag(trigger, a, actor, c);
				targets.put(action, target);
			} else if (isInteraction(a)) {
				EAdSceneElementDef target = addInteraction(trigger, a, actor, c);
				targets.put(action, target);

			} else
				action = convert(a, (ActionImpl) action, actor, c,
						isActiveArea, trigger);

		}
		
		for ( Entry<EAdAction, TriggerMacroEf> e: triggers.entrySet()){
			EAdAction a = e.getKey();
			TriggerMacroEf trigger = e.getValue();
			addMoveTo( getsTo.get(a), trigger, actor, a );
		}
		
		for ( Entry<EAdAction, EAdSceneElementDef> e: targets.entrySet() ){
			EAdDragEvent dragEvent = new EAdDragEvent(actor, DragEventType.DROP);
			TriggerMacroEf trigger = triggers.get(e.getKey());
			e.getValue().addBehavior(dragEvent, trigger);
		}
	}
	
	private void addMoveTo( boolean needsGoTo, TriggerMacroEf triggerEffect, EAdSceneElementDef actor, EAdAction action ){
		if (!factory.isFirstPerson() && needsGoTo) {
			MoveActiveElementEf moveActiveElement = new MoveActiveElementEf();
			moveActiveElement.setId("moveToActionTarget");
			moveActiveElement.setTarget(actor);
			moveActiveElement.getNextEffects().add(triggerEffect);
			action.getEffects().add(moveActiveElement);
		} else {
			action.getEffects().add(triggerEffect);
		}
	}

	private EAdSceneElementDef addDrag(TriggerMacroEf triggerMacro, Action a, SceneElementDefImpl actor, EAdCondition c) {
		EAdElement element = factory.getElementById(a.getTargetId());
		EAdSceneElementDef target = null;

		if (element instanceof EAdSceneElementDef) {
			target = (EAdSceneElementDef) element;
		} else if (element instanceof EAdSceneElement) {
			target = ((EAdSceneElement) element).getDefinition();
		}

		EffectsMacro macro = this.effectsImporterFactory
				.getMacroEffects(a.getEffects());
		if (triggerMacro != null) {
			triggerMacro.putMacro(macro, c);
		}

		factory.addDraggableActor(actor);
		
		return target;

	}

	private boolean isInteraction(Action a) {
		return a.getType() == Action.GIVE_TO || a.getType() == Action.USE_WITH
				|| a.getType() == Action.CUSTOM_INTERACT;
	}

	private EAdSceneElementDef addInteraction(TriggerMacroEf triggerMacro, Action a, SceneElementDefImpl actor,
			EAdCondition condition) {

		EffectsMacro macro = effectsImporterFactory.getMacroEffects(a
				.getEffects());
		ModifyInventoryEf removeFromInventory = new ModifyInventoryEf(actor,
				InventoryEffectAction.REMOVE_FROM_INVENTORY);
		if (a.getType() == Action.GIVE_TO && !hasCancelEffect(a.getEffects())) {
			macro.getEffects().add(removeFromInventory);
		}
		triggerMacro.putMacro(macro, condition);

		EAdElement e = factory.getElementById(a.getTargetId());
		EAdSceneElementDef target = null;
		if ( e instanceof EAdSceneElement ){
			target = ((EAdSceneElement) e).getDefinition();
		}
		else if ( e instanceof EAdSceneElementDef ){
			target = (EAdSceneElementDef) e;
		}
		
		return target;

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
