package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;

public class ConversationImporter implements EAdElementImporter<Conversation, EAdEffect> {

	private Map<ConversationNode, EAdEffect> nodes;

	private EAdElementImporter<OptionConversationNode, EAdShowQuestion> optionNodeImporter;

	private EAdElementImporter<DialogueConversationNode, EAdTriggerMacro> dialogueImporter;

	@Inject
	public ConversationImporter(
			EAdElementImporter<DialogueConversationNode, EAdTriggerMacro> dialogueImporter,
			EAdElementImporter<OptionConversationNode, EAdShowQuestion> optionNodeImporter,
			EffectsImporterFactory effectFactory) {
		nodes = new HashMap<ConversationNode, EAdEffect>();
		this.dialogueImporter = dialogueImporter;
		this.optionNodeImporter = optionNodeImporter;
	}

	@Override
	public EAdEffect init(Conversation oldObject) {
		return null;
	}

	public EAdEffect convert(Conversation oldObject, Object object) {
		nodes.clear();

		for (ConversationNode node : oldObject.getAllNodes()) {
			if (node.getType() == ConversationNode.DIALOGUE) {
				EAdEffect effect = dialogueImporter
				.init((DialogueConversationNode) node);
				 effect = dialogueImporter
				.convert((DialogueConversationNode) node, effect);
				if (effect != null)
					nodes.put(node, effect);
			} else {
				EAdEffect effect = optionNodeImporter
				.init((OptionConversationNode) node);
				effect = optionNodeImporter
				.convert((OptionConversationNode) node, effect);
				if (effect != null)
					nodes.put(node, effect);
			}
		}

		for (ConversationNode node : oldObject.getAllNodes()) {
			if (node.getType() == ConversationNode.DIALOGUE) {
				if (!node.isTerminal()) {
					EAdTriggerMacro currentNodeEffect = (EAdTriggerMacro) nodes
							.get(node);
					EAdEffect nextNodeEffect = nodes.get(node.getChild(0));
					currentNodeEffect.getMacro().getEffects()
							.add(nextNodeEffect);
				}
			} else {
				EAdShowQuestion currentNodeEffect = (EAdShowQuestion) nodes
						.get(node);
				for (int i = 0; i < node.getChildCount(); i++) {
					currentNodeEffect.getAnswers().get(i).getMacro()
							.getEffects().add(nodes.get(node.getChild(i)));
				}
			}
		}
		EAdEffect initialEffect = nodes.get(oldObject.getRootNode()); 
		return initialEffect;
	}

}
