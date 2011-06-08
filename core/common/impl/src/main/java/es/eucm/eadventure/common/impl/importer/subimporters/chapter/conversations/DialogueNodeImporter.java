package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;

public class DialogueNodeImporter implements
		Importer<DialogueConversationNode, EAdTriggerMacro> {

	private static int ID_GENERATOR = 0;

	private Importer<ConversationLine, EAdShowText> conversationLineImporter;

	private EffectsImporterFactory effectsImporter;

	@Inject
	public DialogueNodeImporter(EffectsImporterFactory effectsImporter,
			Importer<ConversationLine, EAdShowText> conversationLineImporter) {

		this.effectsImporter = effectsImporter;
		this.conversationLineImporter = conversationLineImporter;
	}

	@Override
	public EAdTriggerMacro convert(DialogueConversationNode oldObject) {
		EAdMacroImpl macro = new EAdMacroImpl("DialogueNode" + ID_GENERATOR++);

		for (int i = 0; i < oldObject.getLineCount(); i++) {
			EAdShowText effect = conversationLineImporter.convert(oldObject
					.getLine(i));
			effect.setBlocking(true);
			effect.setOpaque(true);
			if (effect != null)
				macro.getEffects().add(effect);
		}

		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect effect = effectsImporter.getEffect(e);
			if (effect != null)
				macro.getEffects().add(effect);
		}

		EAdTriggerMacro triggerMacro = new EAdTriggerMacro("triggerMacro_"
				+ macro.getId());
		triggerMacro.setMacro(macro);
		return triggerMacro;
	}

	@Override
	public boolean equals(DialogueConversationNode oldObject,
			EAdTriggerMacro newObject) {
		return false;
	}

}
