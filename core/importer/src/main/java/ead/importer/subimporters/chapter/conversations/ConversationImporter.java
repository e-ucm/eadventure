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

package ead.importer.subimporters.chapter.conversations;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.text.ShowQuestionEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.params.text.EAdString;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EffectsImporterFactory;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;

public class ConversationImporter implements
		EAdElementImporter<Conversation, EAdEffect> {

	private Map<ConversationNode, EAdEffect> nodes;

	private StringHandler stringHandler;

	private EAdElementImporter<DialogueConversationNode, EAdEffect> dialogueImporter;

	protected ImportAnnotator annotator;

	@Inject
	public ConversationImporter(
			EAdElementImporter<DialogueConversationNode, EAdEffect> dialogueImporter,
			EffectsImporterFactory effectFactory, StringHandler stringHandler,
			ImportAnnotator annotator) {
		nodes = new LinkedHashMap<ConversationNode, EAdEffect>();
		this.dialogueImporter = dialogueImporter;
		this.stringHandler = stringHandler;
		this.annotator = annotator;
	}

	@Override
	public EAdEffect init(Conversation oldObject) {
		return null;
	}

	@Override
	public EAdEffect convert(Conversation oldObject, Object object) {
		nodes.clear();

		annotator.annotate(ImportAnnotator.Type.Entry, ImportAnnotator.Key.Role, "Conversation");
		annotator.annotate(ImportAnnotator.Type.Open, oldObject.getId());

		for (ConversationNode node : oldObject.getAllNodes()) {
			if (node.getType() == ConversationNode.DIALOGUE) {
				EAdEffect effect = dialogueImporter
						.init((DialogueConversationNode) node);
				effect = dialogueImporter.convert(
						(DialogueConversationNode) node, effect);
				if (effect != null) {
					nodes.put(node, effect);
				}
			} else if (node.getType() == ConversationNode.OPTION) {
				EAdEffect effect = new ShowQuestionEf();
				nodes.put(node, effect);
				annotator.annotate(effect, ImportAnnotator.Type.Comment,
						"choice");
			}
		}

		for (ConversationNode node : oldObject.getAllNodes()) {
			if (node.getType() == ConversationNode.DIALOGUE) {
				if (!node.isTerminal()) {
					EAdEffect currentNodeEffect = nodes.get(node);
					EAdEffect nextNodeEffect = nodes.get(node.getChild(0));
					while (currentNodeEffect.getNextEffects().size() > 0) {
						currentNodeEffect = currentNodeEffect.getNextEffects()
								.get(0);
					}
					currentNodeEffect.getNextEffects().add(nextNodeEffect);
				}
			} else if (node.getType() == ConversationNode.OPTION) {
				ShowQuestionEf currentNodeEffect = (ShowQuestionEf) nodes
						.get(node);
				for (int i = 0; i < node.getChildCount(); i++) {
					EAdString string = EAdString.newRandomEAdString("line");
					stringHandler.setString(string, node.getLineText(i));
					currentNodeEffect.addAnswer(string, nodes.get(node
							.getChild(i)));
				}
				currentNodeEffect.setUpNewInstance();
			}
		}
		EAdEffect initialEffect = nodes.get(oldObject.getRootNode());
		ChangeFieldEf changeField = new ChangeFieldEf(
				SystemFields.BASIC_HUD_OPAQUE, BooleanOp.TRUE_OP);
		changeField.getNextEffects().add(initialEffect);

		annotator.annotate(ImportAnnotator.Type.Close, oldObject.getId());

		return changeField;
	}
}
