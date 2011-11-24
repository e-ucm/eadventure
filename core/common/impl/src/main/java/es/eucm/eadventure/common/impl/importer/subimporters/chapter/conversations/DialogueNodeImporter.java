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
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;

public class DialogueNodeImporter implements
		EAdElementImporter<DialogueConversationNode, EAdTriggerMacro> {

	private static int ID_GENERATOR = 0;

	private EAdElementImporter<ConversationLine, EAdShowSceneElement> conversationLineImporter;

	private EffectsImporterFactory effectsImporter;

	@Inject
	public DialogueNodeImporter(EffectsImporterFactory effectsImporter,
			EAdElementImporter<ConversationLine, EAdShowSceneElement> conversationLineImporter) {

		this.effectsImporter = effectsImporter;
		this.conversationLineImporter = conversationLineImporter;
	}

	@Override
	public EAdTriggerMacro init(DialogueConversationNode oldObject) {
		EAdMacroImpl macro = new EAdMacroImpl();
		macro.setId("DialogueNode" + ID_GENERATOR++);

		EAdTriggerMacro triggerMacro = new EAdTriggerMacro();
		triggerMacro.setId("triggerMacro_"
				+ macro.getId());
		triggerMacro.setMacro(macro);
		return triggerMacro;
	}
	
	@Override
	public EAdTriggerMacro convert(DialogueConversationNode oldObject, Object object) {
		EAdTriggerMacro triggerMacro = (EAdTriggerMacro) object;
		EAdMacro macro = triggerMacro.getMacro();
		
		for (int i = 0; i < oldObject.getLineCount(); i++) {
			EAdShowSceneElement effect = conversationLineImporter.init(oldObject
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
