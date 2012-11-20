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

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.subimporters.effects.texts.SpeakCharEffectImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;

public class LineImporterToShowText implements
		EAdElementImporter<ConversationLine, SpeakEf> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EAdElementFactory factory;

	private StringHandler stringHandler;

	protected ImportAnnotator annotator;

	@Inject
	public LineImporterToShowText(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, StringHandler stringHandler,
			ImportAnnotator annotator) {
		this.conditionsImporter = conditionsImporter;
		this.factory = factory;
		this.stringHandler = stringHandler;
		this.annotator = annotator;
	}

	public SpeakEf init(ConversationLine line) {
		EAdElement element = factory.getElementById(line.getName());
		if (line.isPlayerLine() && factory.isFirstPerson()) {
			return new SpeakEf();
		} else
			return new SpeakSceneElementEf(element);

	}

	@Override
	public SpeakEf convert(ConversationLine line, Object object) {
		SpeakEf effect = (SpeakEf) object;

		NPC npc = line.getName().equals(Player.IDENTIFIER) ? factory
				.getCurrentOldChapterModel().getPlayer() : factory
				.getCurrentOldChapterModel().getCharacter(line.getName());

		String text = SpeakCharEffectImporter.setBallonType(effect, line
				.getText());
		stringHandler.setString(effect.getString(), text);
		SpeakCharEffectImporter.setColor(effect, npc);

		// Set conditions
		if (line.getConditions() != null) {
			EAdCondition condition = conditionsImporter.init(line
					.getConditions());
			condition = conditionsImporter.convert(line.getConditions(),
					condition);
			if (condition != null) {
				effect.setCondition(condition);
			}
		}
		effect.setNextEffectsAlways(true);

		return effect;
	}

}
