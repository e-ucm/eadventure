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
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.texts.SpeakCharEffectImporter;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.predef.model.effects.EAdSpeakSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;

public class LineImporterToShowText implements
		EAdElementImporter<ConversationLine, EAdSpeakEffect> {

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	private EAdElementFactory factory;

	private StringHandler stringHandler;

	@Inject
	public LineImporterToShowText(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory, StringHandler stringHandler) {
		this.conditionsImporter = conditionsImporter;
		this.factory = factory;
		this.stringHandler = stringHandler;
	}

	public EAdSpeakEffect init(ConversationLine line) {
		EAdElement element = factory.getElementById(line.getName());
		if (line.isPlayerLine() && factory.isFirstPerson()) {
			return new EAdSpeakEffect();
		} else
			return new EAdSpeakSceneElement(element);

	}

	@Override
	public EAdSpeakEffect convert(ConversationLine line, Object object) {
		EAdSpeakEffect effect = (EAdSpeakEffect) object;

		NPC npc = line.getName().equals(Player.IDENTIFIER) ? factory
				.getCurrentOldChapterModel().getPlayer() : factory
				.getCurrentOldChapterModel().getCharacter(line.getName());

		stringHandler.setString(effect.getString(), line.getText());
		SpeakCharEffectImporter.setColor(effect, line.getText(), npc);

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

		return effect;
	}

}
