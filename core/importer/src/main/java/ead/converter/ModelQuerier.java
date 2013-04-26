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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.effects.EmptyEffect;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.fills.Paint;
import ead.common.model.params.guievents.EAdGUIEvent;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.paint.EAdPaint;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.VarDef;
import ead.converter.subconverters.ConversationsConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.conditions.Condition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalStateCondition;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.common.data.chapter.effects.MacroReferenceEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;

@Singleton
public class ModelQuerier {

	private static Logger logger = LoggerFactory.getLogger("ModelQuerier");

	private ConditionsConverter conditionConverter;

	private EffectsConverter effectsConverter;

	private UtilsConverter utilsConverter;

	private ConversationsConverter conversationsConverter;

	private EAdElementsCache elementsCache;

	private AdventureData adventureData;

	private EAdChapter currentChapter;

	private Chapter oldChapter;

	private Map<String, EAdField<Boolean>> flagFields;
	private Map<String, EAdField<Integer>> variableFields;
	private Map<String, EAdCondition> globalStates;
	private Map<String, EAdList<EAdEffect>> macros;
	private Map<String, EAdEffect> conversations;
	private Map<String, EAdPaint> npcTexts;
	private Map<String, EAdPaint> npcBubbles;

	// Auxiliary variables:
	// These stacks are to solve cycles and assure that everything is loaded an
	// the right moment in macros and global state (a macro calling another
	// macro, a global state depending on another global state)
	private ArrayList<Macro> macrosToLoad;

	private ArrayList<GlobalState> globalStatesToLoad;

	@Inject
	public ModelQuerier(EAdElementsCache elementsCache) {
		this.elementsCache = elementsCache;
		flagFields = new HashMap<String, EAdField<Boolean>>();
		variableFields = new HashMap<String, EAdField<Integer>>();
		globalStates = new HashMap<String, EAdCondition>();
		macros = new HashMap<String, EAdList<EAdEffect>>();

		macrosToLoad = new ArrayList<Macro>();
		globalStatesToLoad = new ArrayList<GlobalState>();

		// Conversations
		conversations = new HashMap<String, EAdEffect>();
		npcTexts = new HashMap<String, EAdPaint>();
		npcBubbles = new HashMap<String, EAdPaint>();

	}

	public void setConditionConverter(ConditionsConverter conditionConverter) {
		this.conditionConverter = conditionConverter;
	}

	public void setEffectsConverter(EffectsConverter effectsConverter) {
		this.effectsConverter = effectsConverter;
	}

	public void setAdventureData(AdventureData adventureData) {
		this.adventureData = adventureData;
	}

	public void setConversationsConverter(
			ConversationsConverter conversationsConverter) {
		this.conversationsConverter = conversationsConverter;
	}

	public void setUtilsConverter(UtilsConverter utilsConverter) {
		this.utilsConverter = utilsConverter;
	}

	public AdventureData getAventureData() {
		return adventureData;
	}

	public void setCurrentChapter(EAdChapter chapter, Chapter c) {
		this.currentChapter = chapter;
		this.oldChapter = c;
		flagFields.clear();
		variableFields.clear();
	}

	/**
	 * Loads the global states in the current chapter, and removes any global
	 * state in the cache
	 */
	public void loadGlobalStates() {
		globalStates.clear();
		// Add global states
		globalStatesToLoad.addAll(oldChapter.getGlobalStates());
		int iterations = 0;
		boolean toWait = false;
		while (iterations <= globalStatesToLoad.size()
				&& !globalStatesToLoad.isEmpty()) {
			// We check for references to other global states, if any and the
			// global state is still not loaded, we send this global state to
			// the end of the queue
			GlobalState g = globalStatesToLoad.remove(0);
			for (List<Condition> l : g.getConditionsList()) {
				for (Condition c : l) {
					if (c.getType() == Condition.GLOBAL_STATE_CONDITION) {
						GlobalStateCondition gs = (GlobalStateCondition) c;
						if (!globalStates.containsKey(gs.getId())) {
							toWait = true;
							break;
						}
					}
				}
				if (toWait) {
					break;
				}
			}

			if (toWait) {
				globalStatesToLoad.add(g);
				iterations++;

			} else {
				iterations = 0;
				EAdCondition cond = conditionConverter.convert(g);
				if (cond == null) {
					logger.warn("Global state returned a {} after conversion",
							g.getId());
				}
				globalStates.put(g.getId(), cond);
			}
			toWait = false;
		}

		// We report errors
		if (globalStatesToLoad.size() > 0) {
			String globalStatesIds = "";
			for (GlobalState gs : globalStatesToLoad) {
				globalStatesIds += gs.getId() + ",";
			}
			logger
					.error("Cycle detected in global states: {}",
							globalStatesIds);
		}
	}

	/**
	 * Loads the macros of the current chapter, and removes any existing macro
	 * in the cache
	 */
	public void loadMacros() {
		macros.clear();
		conversations.clear();
		// Add macros
		macrosToLoad.addAll(oldChapter.getMacros());
		int iterations = 0;
		boolean toWait = false;
		while (iterations <= macrosToLoad.size() && !macrosToLoad.isEmpty()) {
			Macro m = macrosToLoad.remove(0);
			// We check for other macros. If there's any and is still not
			// defined, we send this macro to the end of the queue to wait
			for (AbstractEffect e : m.getEffects()) {
				if (e instanceof MacroReferenceEffect) {
					MacroReferenceEffect mr = (MacroReferenceEffect) e;
					if (!macros.containsKey(mr.getTargetId())) {
						toWait = true;
						break;
					}
				}
			}

			// If macro has a reference to a non-defined macro, we wait to load
			// it
			if (toWait) {
				macrosToLoad.add(m);
				iterations++;
				// If not, we load it
			} else {
				iterations = 0;
				EAdList<EAdEffect> macro = getMacro(m.getId());
				List<EAdEffect> effect = effectsConverter.convert(m);
				if (effect.size() > 0) {
					macro.add(effect.get(0));
				}
			}
			toWait = false;

		}

		if (macrosToLoad.size() > 0) {
			String macrosIds = "";
			for (Macro m : macrosToLoad) {
				macrosIds += m.getId() + ",";
			}
			logger.error("Cycle detected in macros: {}", macrosIds);
		}
	}

	/**
	 * Loads the conversations of the current chapter, and clear any existing
	 * conversation in the cache
	 */
	public void loadConversations() {
		npcTexts.clear();
		npcBubbles.clear();
		// Load text and bubble colors
		for (NPC npc : oldChapter.getCharacters()) {
			EAdPaint textPaint = utilsConverter.getPaint(npc
					.getTextFrontColor(), npc.getTextBorderColor());

			npcTexts.put(npc.getId(), textPaint);
			if (npc.getShowsSpeechBubbles()) {
				EAdPaint bubblePaint = utilsConverter.getPaint(npc
						.getBubbleBkgColor(), npc.getBubbleBorderColor());
				npcBubbles.put(npc.getId(), bubblePaint);
			} else {
				npcBubbles.put(npc.getId(), Paint.BLACK_ON_WHITE);
			}
		}
		// Load conversations
		for (Conversation c : oldChapter.getConversations()) {
			EAdEffect conversation = conversationsConverter.convert(c);
			EAdEffect proxy = conversations.get(c.getId());
			if (proxy != null) {
				proxy.addNextEffect(conversation);
			} else {
				conversations.put(c.getId(), conversation);
			}
		}
	}

	public EAdField<Boolean> getFlag(String id) {
		EAdField<Boolean> field = flagFields.get(id);
		if (field == null) {
			field = new BasicField<Boolean>(currentChapter,
					new VarDef<Boolean>(id, Boolean.class, false));
			flagFields.put(id, field);
		}
		return field;
	}

	public EAdField<Integer> getVariable(String id) {
		EAdField<Integer> field = variableFields.get(id);
		if (field == null) {
			field = new BasicField<Integer>(currentChapter,
					new VarDef<Integer>(id, Integer.class, 0));
			variableFields.put(id, field);
		}
		return field;
	}

	public EAdCondition getGlobalState(String id) {
		EAdCondition globalState = globalStates.get(id);
		if (globalState == null) {
			logger.warn("Global state '{}' not found", id);
		}
		return globalState;
	}

	public EAdList<EAdEffect> getMacro(String id) {
		EAdList<EAdEffect> macro = macros.get(id);
		if (macro == null) {
			macro = new EAdList<EAdEffect>();
			macros.put(id, macro);
		}
		return macro;
	}

	public EAdGUIEvent getActionsInteraction() {
		switch (adventureData.getDefaultClickAction()) {
		case SHOW_ACTIONS:
			return MouseGEv.MOUSE_LEFT_PRESSED;
		case SHOW_DETAILS:
			return MouseGEv.MOUSE_RIGHT_PRESSED;
		}
		return MouseGEv.MOUSE_RIGHT_PRESSED;
	}

	/**
	 * Creates an speak effect for the given npc id and the given text
	 * 
	 * @param npc
	 * @param text
	 * @return
	 */
	public SpeakEf getSpeakFor(String npc, EAdString text) {
		EAdElement element = elementsCache.get(npc);
		SpeakEf effect = new SpeakSceneElementEf(element, text);
		effect.setColor(npcTexts.get(npc), npcBubbles.get(npc));
		EAdField<EAdSceneElement> fieldElement = elementsCache.getField(
				element, SceneElementDef.VAR_SCENE_ELEMENT);

		effect.setX(elementsCache.getField(fieldElement,
				SceneElement.VAR_CENTER_X));
		effect.setY(elementsCache.getField(fieldElement, SceneElement.VAR_TOP));
		return effect;
	}

	/**
	 * Returns the conversation for the given id
	 * 
	 * @param id
	 * @return
	 */
	public EAdEffect getConversation(String id) {
		EAdEffect conversation = conversations.get(id);
		if (conversation == null) {
			conversation = new EmptyEffect();
			conversations.put(id, conversation);
		}
		return conversation;
	}
}
