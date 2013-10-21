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

package es.eucm.ead.importer.subconverters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.importer.ModelQuerier;
import es.eucm.ead.importer.StringsConverter;
import es.eucm.ead.importer.subconverters.conditions.ConditionsConverter;
import es.eucm.ead.importer.subconverters.effects.EffectsConverter;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.EmptyEffect;
import es.eucm.ead.model.elements.effects.text.QuestionEf;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.params.text.EAdString;
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

	static private Logger logger = LoggerFactory
			.getLogger(ConversationsConverter.class);

	public static final String IN_CONVERSATION = "in_conversation";

	private ModelQuerier modelQuerier;

	private StringsConverter stringsConverter;

	private ConditionsConverter conditionsConverter;

	private EffectsConverter effectsConverter;

	private Map<ConversationNode, List<Effect>> nodes;

	@Inject
	public ConversationsConverter(StringsConverter stringConverter,
			ConditionsConverter conditionsConverter,
			EffectsConverter effectsConverter, ModelQuerier modelQuerier) {
		this.conditionsConverter = conditionsConverter;
		this.stringsConverter = stringConverter;
		this.nodes = new HashMap<ConversationNode, List<Effect>>();
		this.effectsConverter = effectsConverter;
		this.modelQuerier = modelQuerier;
	}

	/**
	 * @param c the old conversation
	 * @return Converts and old conversation into a list of speak effects
	 */
	public Effect convert(Conversation c) {
		nodes.clear();
		// Create the nodes
		for (ConversationNode n : c.getAllNodes()) {
			nodes.put(n, convert(n));
		}

		// We need a mechanism to encapsulate the whole conversation and to hold
		// subsequent effects until the conversation is finished. We used a wait
		// until effect, with a condition that checks if the conversation is
		// over
		Effect empty = modelQuerier.getConversation(c.getId());
		ElementField inConversation = new ElementField(empty, IN_CONVERSATION);

		ChangeFieldEf endConversation = new ChangeFieldEf(inConversation,
				EmptyCond.FALSE);
		// Connect the nodes
		for (ConversationNode n : c.getAllNodes()) {
			if (n.getType() == ConversationNode.DIALOGUE) {
				DialogueConversationNode dn = (DialogueConversationNode) n;
				if (dn.getChild(0) != null) {
					List<Effect> effects = nodes.get(dn);
					Effect nextEffect = nodes.get(dn.getChild(0)).get(0);
					effects.get(effects.size() - 1).addNextEffect(nextEffect);
				}
			} else {
				addAnswers((OptionConversationNode) n);
			}
			// End condition
			if (n.isTerminal()) {
				List<Effect> effects = nodes.get(n);
				effects.get(effects.size() - 1).addNextEffect(endConversation);
			}
		}
		return nodes.get(c.getRootNode()).get(0);
	}

	/**
	 * @param n a node
	 * @return Converts a conversation node into an effect
	 */
	public List<Effect> convert(ConversationNode n) {
		List<Effect> node = null;
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
			List<Effect> nextEffects = effectsConverter.convert(n.getEffects());
			if (node == null) {
				node = nextEffects;
			} else if (nextEffects.size() > 0) {
				node.get(node.size() - 1).addNextEffect(nextEffects.get(0));
				node.add(nextEffects.get(nextEffects.size() - 1));
			}
		}

		return node;
	}

	/**
	 * @param n a node
	 * @return Converts a conversation node into an effect
	 */
	private List<Effect> convertDialog(DialogueConversationNode n) {
		ArrayList<Effect> nodes = new ArrayList<Effect>();
		// If it has no lines, we return an empty effect
		if (n.getLineCount() == 0) {
			nodes.add(new EmptyEffect());
			return nodes;
		}

		Effect lastEffect = null;
		for (int i = 0; i < n.getLineCount(); i++) {
			// XXX n.getAudioPath(i);
			// XXX n.getSynthesizerVoice(line)
			// XXX n.isKeepShowing()

			EAdString text = stringsConverter.convert(n.getLineText(i), true);
			List<Operation> ops = stringsConverter.getOperations(n
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
	 * @param n a node
	 * @return Returns a list with only one node, with a question effect
	 */
	private List<Effect> convertOption(OptionConversationNode n) {
		ArrayList<Effect> nodes = new ArrayList<Effect>();
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
	 * @param n a node
	 */
	private void addAnswers(OptionConversationNode n) {
		QuestionEf question = (QuestionEf) nodes.get(n).get(0);
		for (int i = 0; i < n.getLineCount(); i++) {
			// In eAd1, expressions are not evaluated in answers
			EAdString answer = stringsConverter
					.convert(n.getLineText(i), false);
			List<Effect> nextEffects = nodes.get(n.getChild(i));
			if (nextEffects.size() > 0) {
				Effect nextEffect = nextEffects.get(0);
				question.addAnswer(answer, nextEffect);
			} else {
				logger.debug("Weird. Answer with no next node.");
			}
		}
	}

}
