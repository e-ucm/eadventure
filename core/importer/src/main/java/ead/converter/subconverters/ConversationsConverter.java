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

package ead.converter.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.EmptyEffect;
import ead.common.model.elements.effects.text.QuestionEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.params.text.EAdString;
import ead.common.model.params.variables.VarDef;
import ead.converter.ModelQuerier;
import ead.converter.StringsConverter;
import ead.converter.subconverters.conditions.ConditionsConverter;
import ead.converter.subconverters.effects.EffectsConverter;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ConversationsConverter {

	private static final Logger logger = LoggerFactory
			.getLogger("ConversationConverter");

	public static final VarDef<Boolean> IN_CONVERSATION = new VarDef<Boolean>(
			"in_conversation", Boolean.class, true);

	private ModelQuerier modelQuerier;

	private StringsConverter stringsConverter;

	private ConditionsConverter conditionsConverter;

	private EffectsConverter effectsConverter;

	private Map<ConversationNode, List<EAdEffect>> nodes;

	@Inject
	public ConversationsConverter(StringsConverter stringConverter,
			ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter, ModelQuerier modelQuerier) {
		this.conditionsConverter = conditionsConverter;
		this.stringsConverter = stringConverter;
		this.nodes = new HashMap<ConversationNode, List<EAdEffect>>();
		this.effectsConverter = effectsConverter;
		this.modelQuerier = modelQuerier;
	}

	/**
	 * Converts and old conversation into a list of speak effects
	 * 
	 * @param c
	 * @return
	 */
	public EAdEffect convert(Conversation c) {
		nodes.clear();
		// Create the nodes
		for (ConversationNode n : c.getAllNodes()) {
			nodes.put(n, convert(n));
		}

		// We need a mechanism to encapsulate the whole conversation and to hold
		// subsequent effects until the conversation is finished. We used a wait
		// until effect, with a condition that checks if the conversation is
		// over
		EAdEffect empty = modelQuerier.getConversation(c.getId());
		EAdField<Boolean> inConversation = new BasicField<Boolean>(empty,
				IN_CONVERSATION);

		ChangeFieldEf endConversation = new ChangeFieldEf(inConversation,
				EmptyCond.FALSE);
		// Connect the nodes
		for (ConversationNode n : c.getAllNodes()) {
			for (int i = 0; i < n.getChildCount(); i++) {
				if (n.getType() == ConversationNode.DIALOGUE) {
					if (n.getChildCount() > 1) {
						logger
								.warn("Weird. A dialogue node with more than one child");
					}
					List<EAdEffect> effects = nodes.get(n);
					EAdEffect nextEffect = nodes.get(n.getChild(0)).get(0);
					effects.get(effects.size() - 1).addNextEffect(nextEffect);
				} else {
					addAnswers((OptionConversationNode) n);
				}
			}
			// End condition
			if (n.isTerminal()) {
				List<EAdEffect> effects = nodes.get(n);
				effects.get(effects.size() - 1).addNextEffect(endConversation);
			}
		}
		EAdEffect root = nodes.get(c.getRootNode()).get(0);
		return root;
	}

	/**
	 * Converts a conversation node into an effect
	 * 
	 * @param n
	 * @return
	 */
	public List<EAdEffect> convert(ConversationNode n) {
		List<EAdEffect> node = null;
		switch (n.getType()) {
		case ConversationNode.DIALOGUE:
			node = convertDialog((DialogueConversationNode) n);
			break;
		case ConversationNode.OPTION:
			node = convertOption((OptionConversationNode) n);
			break;
		}

		// Add node effects
		if (n.hasEffects()) {
			List<EAdEffect> nextEffects = effectsConverter.convert(n
					.getEffects());
			node.addAll(nextEffects);
		}

		return node;
	}

	/**
	 * Converts a dialogue node into a list of effects
	 * 
	 * @param n
	 * @return
	 */
	private List<EAdEffect> convertDialog(DialogueConversationNode n) {
		ArrayList<EAdEffect> nodes = new ArrayList<EAdEffect>();
		// If it has no lines, we return an empty effect
		if (n.getLineCount() == 0) {
			nodes.add(new EmptyEffect());
			return nodes;
		}

		EAdEffect lastEffect = null;
		for (int i = 0; i < n.getLineCount(); i++) {
			// XXX n.getAudioPath(i);
			// XXX n.getSynthesizerVoice(line)
			// XXX n.isKeepShowing()

			EAdString text = stringsConverter.convert(n.getLineText(i), true);
			List<EAdOperation> ops = stringsConverter.getOperations(n
					.getLineText(i));

			SpeakEf nextEffect = modelQuerier.getSpeakFor(n.getLineName(i),
					text);
			nextEffect.getCaption().getOperations().addAll(ops);

			// Set conditions
			nextEffect.setNextEffectsAlways(true);
			nextEffect.setCondition(conditionsConverter.convert(n
					.getLineConditions(i)));

			if (lastEffect != null) {
				lastEffect.addNextEffect(nextEffect);
			}
			nodes.add(nextEffect);
			lastEffect = nextEffect;
		}
		return nodes;
	}

	/**
	 * Returns a list with only one node, with a question effect
	 * 
	 * @param n
	 * @return
	 */
	private List<EAdEffect> convertOption(OptionConversationNode n) {
		ArrayList<EAdEffect> nodes = new ArrayList<EAdEffect>();
		QuestionEf node = new QuestionEf();

		// XXX n.isTopPosition() n.isBottomPosition()
		// XXX n.isPreListening();
		// XXX n.isKeepShowing()

		nodes.add(node);
		return nodes;
	}

	/**
	 * Adds answers nodes to an option node
	 * 
	 * @param n
	 */
	private void addAnswers(OptionConversationNode n) {
		QuestionEf question = (QuestionEf) nodes.get(n).get(0);
		for (int i = 0; i < n.getLineCount(); i++) {
			// In eAd1, expressions are not evaluated in answers
			EAdString answer = stringsConverter
					.convert(n.getLineText(i), false);
			List<EAdEffect> nextEffects = nodes.get(n.getChild(i));
			if (nextEffects.size() > 0) {
				EAdEffect nextEffect = nextEffects.get(0);
				question.addAnswer(answer, nextEffect);
			} else {
				logger.debug("Weird. Answer with no next node.");
			}
		}
	}

}
