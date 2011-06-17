package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;

public class DialogueNodeImporter implements
		EAdElementImporter<DialogueConversationNode, EAdTriggerMacro> {

	private static int ID_GENERATOR = 0;

	private EAdElementImporter<ConversationLine, EAdShowText> conversationLineImporter;

	private EffectsImporterFactory effectsImporter;

	@Inject
	public DialogueNodeImporter(EffectsImporterFactory effectsImporter,
			EAdElementImporter<ConversationLine, EAdShowText> conversationLineImporter) {

		this.effectsImporter = effectsImporter;
		this.conversationLineImporter = conversationLineImporter;
	}

	@Override
	public EAdTriggerMacro init(DialogueConversationNode oldObject) {
		EAdMacroImpl macro = new EAdMacroImpl("DialogueNode" + ID_GENERATOR++);

		EAdTriggerMacro triggerMacro = new EAdTriggerMacro("triggerMacro_"
				+ macro.getId());
		triggerMacro.setMacro(macro);
		return triggerMacro;
	}
	
	@Override
	public EAdTriggerMacro convert(DialogueConversationNode oldObject, Object object) {
		EAdTriggerMacro triggerMacro = (EAdTriggerMacro) object;
		EAdMacro macro = triggerMacro.getMacro();
		
		for (int i = 0; i < oldObject.getLineCount(); i++) {
			EAdShowText effect = conversationLineImporter.init(oldObject
					.getLine(i));
			effect = conversationLineImporter.convert(oldObject
					.getLine(i), effect);
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

		return triggerMacro;
	}
}
