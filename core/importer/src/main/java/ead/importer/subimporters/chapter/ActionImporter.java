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

package ead.importer.subimporters.chapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.BasicInventory;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.ORCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.enums.InventoryEffectAction;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.predef.effects.MoveActiveElementToMouseEf;
import ead.common.params.guievents.DragGEv;
import ead.common.params.guievents.MouseGEv;
import ead.common.params.guievents.enums.DragGEvType;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.enums.Alignment;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.EffectsImporterFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.CancelActionEffect;
import es.eucm.eadventure.common.data.chapter.effects.Effects;

public class ActionImporter implements
		EAdElementImporter<Action, EAdSceneElementDef> {

	private static Logger logger = LoggerFactory
			.getLogger(ActionImporter.class);

	private StringHandler stringHandler;

	private EffectsImporterFactory effectsImporterFactory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	private EAdElementFactory factory;

	public static final String DRAWABLE_PATH = "@" + ResourceImporter.DRAWABLE;

	protected ImportAnnotator annotator;

	@Inject
	public ActionImporter(StringHandler stringHandler,
			EffectsImporterFactory effectsImporterFactory,
			ResourceImporter resourceImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.effectsImporterFactory = effectsImporterFactory;
		this.conditionsImporter = conditionsImporter;
		this.resourceImporter = resourceImporter;
		this.factory = factory;
		this.annotator = annotator;
	}

	@Override
	public EAdSceneElementDef init(Action oldObject) {
		EAdSceneElementDef basicAction = new SceneElementDef();
		return basicAction;
	}

	@Override
	public EAdSceneElementDef convert(Action oldObject, Object object) {
		return null;
	}

	public EAdCondition[] setCondition(Action oldObject,
			EAdSceneElementDef action, EAdCondition previousCondition) {
		EAdCondition conditions[] = new EAdCondition[2];
		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject.getConditions(),
				condition);

		EAdCondition andCondition;
		if (previousCondition == null && condition == null) {
			andCondition = EmptyCond.TRUE_EMPTY_CONDITION;
		} else if (previousCondition != null && condition != null) {
			andCondition = new ANDCond(condition,
					new NOTCond(previousCondition));
		} else if (condition == null) {
			andCondition = previousCondition;
		} else {
			andCondition = condition;
		}

		conditions[0] = andCondition;
		conditions[1] = condition;
		return conditions;

	}

	public void convert(Action oldObject, SceneElementDef action,
			EAdSceneElementDef owner, EAdCondition condition,
			boolean isActiveArea, TriggerMacroEf effecttrigger,
			TriggerMacroEf notEffectTrigger) {

		// Action name
		setName(oldObject, action);

		// Add appearance
		addAppearance(oldObject, action);

		// Add effects
		addEffects(effecttrigger, notEffectTrigger, oldObject, action, owner,
				condition, isActiveArea);
	}

	private void setName(Action oldObject, SceneElementDef action) {
		String actionName;
		if (oldObject instanceof CustomAction) {
			CustomAction customAction = (CustomAction) oldObject;
			actionName = customAction.getName();
		} else {
			actionName = getActionName(oldObject.getType());
		}
		EAdString nameString = stringHandler.generateNewString();
		stringHandler.setString(nameString, actionName);
		action.setVarInitialValue(SceneElementDef.VAR_DOC_NAME, nameString);
	}

	private void addEffects(TriggerMacroEf effectTrigger,
			TriggerMacroEf notEffectTrigger, Action oldObject,
			SceneElementDef action, EAdSceneElementDef actor,
			EAdCondition condition, boolean isActiveArea) {
		// Add effects
		EffectsMacro macro = effectsImporterFactory.getMacroEffects(oldObject
				.getEffects());

		// Add default effects for the action
		EAdEffect defaultEffect = getDefaultEffects(oldObject, actor,
				isActiveArea, action);

		if (defaultEffect != null) {
			if (macro == null) {
				macro = new EffectsMacro();
			}
			macro.getEffects().add(0, defaultEffect);
		}

		// Add conditions and get to
		if (macro != null) {
			effectTrigger.putMacro(macro, condition);
		}

		// Add no effects
		EffectsMacro notEffects = effectsImporterFactory
				.getMacroEffects(oldObject.getNotEffects());
		if (notEffects != null) {
			notEffectTrigger.putMacro(notEffects, new NOTCond(condition));
		}
	}

	private void addAppearance(Action oldObject, SceneElementDef action) {
		// If it's a standard action
		if (oldObject.getType() != Action.CUSTOM
				&& oldObject.getType() != Action.CUSTOM_INTERACT) {
			action.addAsset(SceneElementDef.appearance, new Image(
					getDrawablePath(oldObject.getType())));
			action.addAsset(SceneElementDef.overAppearance, new Image(
					getHighlightDrawablePath(oldObject.getType())));
		} else {
			Map<String, String> resourcesStrings = new LinkedHashMap<String, String>();
			Map<String, Object> resourcesClasses = new LinkedHashMap<String, Object>();

			resourcesStrings.put("buttonOver", SceneElementDef.appearance);
			resourcesStrings
					.put("buttonNormal", SceneElementDef.overAppearance);
			resourcesClasses.put("buttonOver", Image.class);
			resourcesClasses.put("buttonNormal", Image.class);
			resourceImporter.importResources(action, ((CustomAction) oldObject)
					.getResources(), resourcesStrings, resourcesClasses);
		}
	}

	public static String getDrawablePath(int actionType) {
		String image;
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
			boolean isActiveArea, EAdSceneElementDef newAction) {
		for (AbstractEffect e : a.getEffects().getEffects()) {
			if (e instanceof CancelActionEffect) {
				return null;
			}
		}
		switch (a.getType()) {
		case Action.GRAB:
			if (!isActiveArea) {

				EAdField<EAdSceneElement> sceneElement = new BasicField<EAdSceneElement>(
						actor, SceneElementDef.VAR_SCENE_ELEMENT);

				EAdField<Boolean> inInventory = new BasicField<Boolean>(
						sceneElement, BasicInventory.VAR_IN_INVENTORY);

				ModifyInventoryEf addToInventory = new ModifyInventoryEf(actor,
						InventoryEffectAction.ADD_TO_INVENTORY);

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

	private static Image examineImage, examineOverImage;

	private static void initExamineAction(StringHandler handler) {
		examineString = new EAdString("engine.Examine");
		examineImage = new Image(getDrawablePath(Action.EXAMINE));
		examineOverImage = new Image(getHighlightDrawablePath(Action.EXAMINE));
	}

	/**
	 * Add examine if there's no examine action added
	 * 
	 * @param actor
	 *            the new actor
	 * @param sound
	 * @param element
	 *            the old element
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public void addExamine(EAdSceneElementDef actor, List<Action> actionsList,
			EAdEffect sound) {
		for (Action a : actionsList) {
			if (a.getType() == Action.EXAMINE) {
				return;
			}
		}

		if (examineString == null) {
			initExamineAction(stringHandler);
		}

		SceneElementDef examineAction = new SceneElementDef();
		examineAction.setVarInitialValue(SceneElementDef.VAR_DOC_NAME,
				examineString);
		if (sound != null) {
			examineAction.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, sound);
		}

		// Effect
		EAdField<EAdString> descField = new BasicField<EAdString>(actor,
				SceneElementDef.VAR_DOC_DETAILED_DESC);

		SpeakEf effect = new SpeakEf();
		stringHandler.setString(effect.getCaption().getText(), "[0]");
		effect.getCaption().getFields().add(descField);

		effect.setAlignment(Alignment.CENTER);
		examineAction.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		// Appearance
		examineAction.addAsset(SceneElementDef.appearance, examineImage);
		examineAction
				.addAsset(SceneElementDef.overAppearance, examineOverImage);

		EAdList list = (EAdList) actor.getVars()
				.get(ActorActionsEf.VAR_ACTIONS);
		if (list == null) {
			list = new EAdList<EAdSceneElementDef>();
			actor.setVarInitialValue(ActorActionsEf.VAR_ACTIONS, list);
		}

		list.add(examineAction);
	}

	public static String getHighlightDrawablePath(int actionType) {
		String image;
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

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public void addAllActions(List<Action> actionsList, SceneElementDef actor,
			boolean isActiveArea, EAdEffect sound) {

		logger.debug("adding all actions for list of {} actions, "
				+ "with an actor called {}, areaActive {}, and sound {}",
				new Object[] { actionsList.size(), actor.getId(), isActiveArea,
						sound });

		// add examine
		addExamine(actor, actionsList, sound);

		// Yeah, I know. But all of them are necessary.
		Map<Integer, EAdSceneElementDef> actions = new LinkedHashMap<Integer, EAdSceneElementDef>();
		Map<String, EAdSceneElementDef> customActions = new LinkedHashMap<String, EAdSceneElementDef>();
		Map<String, EAdSceneElementDef> interactActions = new LinkedHashMap<String, EAdSceneElementDef>();
		Map<EAdSceneElementDef, EAdCondition> previousConditions = new LinkedHashMap<EAdSceneElementDef, EAdCondition>();
		Map<EAdSceneElementDef, EAdCondition> orConditions = new LinkedHashMap<EAdSceneElementDef, EAdCondition>();
		Map<EAdSceneElementDef, TriggerMacroEf> effectsTriggers = new LinkedHashMap<EAdSceneElementDef, TriggerMacroEf>();
		Map<EAdSceneElementDef, TriggerMacroEf> notEffectsTriggers = new LinkedHashMap<EAdSceneElementDef, TriggerMacroEf>();
		Map<EAdSceneElementDef, EAdSceneElementDef> targets = new LinkedHashMap<EAdSceneElementDef, EAdSceneElementDef>();
		Map<EAdSceneElementDef, Boolean> getsTo = new LinkedHashMap<EAdSceneElementDef, Boolean>();

		// Actions list
		EAdList list = (EAdList) actor.getVars()
				.get(ActorActionsEf.VAR_ACTIONS);
		if (list == null) {
			list = new EAdList<EAdSceneElementDef>();
			actor.setVarInitialValue(ActorActionsEf.VAR_ACTIONS, list);
		}

		for (Action a : actionsList) {
			EAdSceneElementDef action = null;

			// Init action
			switch (a.getType()) {
			case Action.CUSTOM:
				CustomAction customAction = (CustomAction) a;
				if (customActions.containsKey(customAction.getName())) {
					action = customActions.get(customAction.getName());
				} else {
					action = init(a);
					list.add(action);
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
					list.add(action);
					actions.put(a.getType(), action);
				}
				break;
			case Action.CUSTOM_INTERACT:
				customAction = (CustomAction) a;
				String name = customAction.getName() + ""
						+ customAction.getTargetId();
				if (interactActions.containsKey(name)) {
					action = interactActions.get(name);
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

			// Effects
			TriggerMacroEf effectTrigger = effectsTriggers.get(action);
			if (effectTrigger == null) {
				effectTrigger = new TriggerMacroEf();
				effectsTriggers.put(action, effectTrigger);
			}

			// Not effects
			TriggerMacroEf notEffectTrigger = null;
			if (a.isActivatedNotEffects()) {
				notEffectTrigger = notEffectsTriggers.get(action);
				if (notEffectTrigger == null) {
					notEffectTrigger = new TriggerMacroEf();
					notEffectsTriggers.put(action, notEffectTrigger);
				}
			}

			// Set condition
			EAdCondition conds[] = setCondition(a, action, previousConditions
					.get(action));
			EAdCondition c = conds[0];
			previousConditions.put(action, c);

			// Or condition
			EAdCondition orCondition = orConditions.get(action);
			if (orCondition == null) {
				orCondition = conds[1];
			} else {
				orCondition = new ORCond(conds[1], orCondition);
			}
			orConditions.put(action, orCondition);

			// Add effects
			if (a.getType() == Action.DRAG_TO) {
				EAdSceneElementDef target = addDrag(effectTrigger,
						notEffectTrigger, a, actor, c);
				targets.put(action, target);
			} else if (isInteraction(a)) {
				EAdSceneElementDef target = addInteraction(effectTrigger,
						notEffectTrigger, a, actor, c);
				targets.put(action, target);

			} else {
				convert(a, (SceneElementDef) action, actor, c, isActiveArea,
						effectTrigger, notEffectTrigger);
			}

			getsTo.put(action, a.isNeedsGoTo());
		}

		logger.debug("iterating {} effect triggers...", effectsTriggers.size());

		// First, effects for every action are added. All actions with the
		// same name are merged in only one, with one big trigger macro effect,
		// whose effects are conditioned by the old action conditions, chained
		// with ORs
		for (Entry<EAdSceneElementDef, TriggerMacroEf> e : effectsTriggers
				.entrySet()) {
			EAdSceneElementDef a = e.getKey();
			TriggerMacroEf trigger = e.getValue();
			EAdCondition orCondition = orConditions.get(e.getKey());
			trigger.setCondition(orCondition);
			if (logger.isDebugEnabled()) {
				Object v = getsTo.get(a);
				logger.debug("will operate on {}, {}, {}, {}", new Object[] {
						v, trigger, actor, a });
				if (v == null) {
					for (EAdSceneElementDef x : getsTo.keySet()) {
						logger.debug("\t{} {} :: {}", new Object[] { x,
								x.getId(), x.equals(a) });
					}
				}
			}
			addMoveTo(getsTo.get(a), trigger, actor, a);
		}

		// Now, the non-effects are added in a second trigger effect whose
		// condition is enabled when any of the main effects for the action are
		// active
		for (Entry<EAdSceneElementDef, TriggerMacroEf> e : notEffectsTriggers
				.entrySet()) {
			EAdSceneElementDef a = e.getKey();
			TriggerMacroEf trigger = e.getValue();
			EAdCondition orCondition = orConditions.get(e.getKey());
			trigger.setCondition(new NOTCond(orCondition));
			addMoveTo(getsTo.get(a), trigger, actor, a);
		}

		for (Entry<EAdSceneElementDef, EAdSceneElementDef> e : targets
				.entrySet()) {
			DragGEv dragEvent = new DragGEv(actor.getId(), DragGEvType.DROP);
			TriggerMacroEf trigger = effectsTriggers.get(e.getKey());
			e.getValue().addBehavior(dragEvent, trigger);
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	private void addMoveTo(boolean needsGoTo, TriggerMacroEf triggerEffect,
			EAdSceneElementDef actor, EAdSceneElementDef action) {
		EAdList list = (EAdList) actor.getVars()
				.get(ActorActionsEf.VAR_ACTIONS);
		if (!factory.isFirstPerson() && needsGoTo) {
			MoveActiveElementToMouseEf moveActiveElement = new MoveActiveElementToMouseEf();
			moveActiveElement.setTarget(actor);
			moveActiveElement.getNextEffects().add(triggerEffect);
			list.add(moveActiveElement);
		} else {
			list.add(triggerEffect);
		}
	}

	private EAdSceneElementDef addDrag(TriggerMacroEf effectTrigger,
			TriggerMacroEf notEffectTrigger, Action a, SceneElementDef actor,
			EAdCondition c) {
		EAdElement element = factory.getElementById(a.getTargetId());
		EAdSceneElementDef target = null;

		if (element instanceof EAdSceneElementDef) {
			target = (EAdSceneElementDef) element;
		} else if (element instanceof EAdSceneElement) {
			target = ((EAdSceneElement) element).getDefinition();
		}

		EffectsMacro macro = this.effectsImporterFactory.getMacroEffects(a
				.getEffects());
		if (effectTrigger != null) {
			effectTrigger.putMacro(macro, c);
		}

		EffectsMacro noEffectsMacro = this.effectsImporterFactory
				.getMacroEffects(a.getNotEffects());
		if (noEffectsMacro != null) {
			notEffectTrigger.putMacro(noEffectsMacro, new NOTCond(c));
		}

		factory.addDraggableActor(actor);

		return target;

	}

	private boolean isInteraction(Action a) {
		return a.getType() == Action.GIVE_TO || a.getType() == Action.USE_WITH
				|| a.getType() == Action.CUSTOM_INTERACT;
	}

	private EAdSceneElementDef addInteraction(TriggerMacroEf effectTrigger,
			TriggerMacroEf notEffectTrigger, Action a, SceneElementDef actor,
			EAdCondition condition) {

		EffectsMacro macro = effectsImporterFactory.getMacroEffects(a
				.getEffects());

		if (macro != null) {
			ModifyInventoryEf removeFromInventory = new ModifyInventoryEf(
					actor, InventoryEffectAction.REMOVE_FROM_INVENTORY);
			if (a.getType() == Action.GIVE_TO
					&& !hasCancelEffect(a.getEffects())) {
				macro.getEffects().add(removeFromInventory);
			}
		}
		effectTrigger.putMacro(macro, condition);

		EffectsMacro noEffectsMacro = this.effectsImporterFactory
				.getMacroEffects(a.getNotEffects());
		if (noEffectsMacro != null) {
			notEffectTrigger.putMacro(noEffectsMacro, new NOTCond(condition));
		}

		EAdElement e = factory.getElementById(a.getTargetId());
		EAdSceneElementDef target = null;
		if (e instanceof EAdSceneElement) {
			target = ((EAdSceneElement) e).getDefinition();
		} else if (e instanceof EAdSceneElementDef) {
			target = (EAdSceneElementDef) e;
		}

		return target;

	}

	private boolean hasCancelEffect(Effects effects) {
		if (effects != null) {
			for (AbstractEffect e : effects.getEffects()) {
				if (e instanceof CancelActionEffect) {
					return true;
				}
			}
		}
		return false;
	}
}
