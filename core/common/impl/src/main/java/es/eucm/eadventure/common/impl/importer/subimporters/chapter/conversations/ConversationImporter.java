package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;

public class ConversationImporter implements Importer<Conversation, EAdEffect> {

	private Map<ConversationNode, EAdEffect> nodes;

	private Importer<OptionConversationNode, EAdShowQuestion> optionNodeImporter;

	private Importer<DialogueConversationNode, EAdTriggerMacro> dialogueImporter;

	@Inject
	public ConversationImporter(
			Importer<DialogueConversationNode, EAdTriggerMacro> dialogueImporter,
			Importer<OptionConversationNode, EAdShowQuestion> optionNodeImporter,
			EffectsImporterFactory effectFactory) {
		nodes = new HashMap<ConversationNode, EAdEffect>();
		this.dialogueImporter = dialogueImporter;
		this.optionNodeImporter = optionNodeImporter;
	}

	@Override
	public EAdEffect convert(Conversation oldObject) {
		nodes.clear();

		for (ConversationNode node : oldObject.getAllNodes()) {
			if (node.getType() == ConversationNode.DIALOGUE) {
				EAdEffect effect = dialogueImporter
						.convert((DialogueConversationNode) node);
				if (effect != null)
					nodes.put(node, effect);
			} else {
				EAdEffect effect = optionNodeImporter
						.convert((OptionConversationNode) node);
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

	@Override
	public boolean equals(Conversation oldObject, EAdEffect newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
