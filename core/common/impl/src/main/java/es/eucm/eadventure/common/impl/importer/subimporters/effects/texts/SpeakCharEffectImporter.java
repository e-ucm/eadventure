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

package es.eucm.eadventure.common.impl.importer.subimporters.effects.texts;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape.BalloonType;

public class SpeakCharEffectImporter extends
		TextEffectImporter<SpeakCharEffect> {

	public static final String WHISPER = "#:*";
	public static final String THOUGHT = "#O";
	public static final String YELL = "#!";

	private NPC npc;

	@Inject
	public SpeakCharEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public EAdSpeakEffect init(SpeakCharEffect oldObject) {
		npc = factory.getCurrentOldChapterModel().getCharacter(
				oldObject.getTargetId());
		return super.init(oldObject);
	}

	@Override
	public EAdSpeakEffect convert(SpeakCharEffect oldObject, Object object) {
		EAdSpeakEffect effect = super.convert(oldObject, object);

		String line = oldObject.getLine();

		BalloonType type = BalloonType.ROUNDED_RECTANGLE;
		if (line.startsWith(WHISPER)) {
			// TODO Whisper balloon
			type = BalloonType.ROUNDED_RECTANGLE;
			line = line.substring(WHISPER.length());
		} else if (line.startsWith(THOUGHT)) {
			type = BalloonType.CLOUD;
			line = line.substring(THOUGHT.length());
		} else if (line.startsWith(YELL)) {
			type = BalloonType.ELECTRIC;
			line = line.substring(YELL.length());
		}

		EAdString text = stringHandler.addString(line);
		effect.setText(text);
		effect.setBalloonType(type);

		EAdColor center = new EAdColor("0x"
				+ npc.getTextFrontColor().substring(1) + "ff");
		EAdColor border = new EAdColor("0x"
				+ npc.getTextBorderColor().substring(1) + "ff");

		EAdColor bubbleCenter = new EAdColor("0x"
				+ npc.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = new EAdColor("0x"
				+ npc.getBubbleBorderColor().substring(1) + "ff");

		effect.setColor(new EAdBorderedColor(center, border),
				new EAdBorderedColor(bubbleCenter, bubbleBorder));

		// FIXME Wrong, element holds an actor, and we need the reference
		EAdSceneElement element = (EAdSceneElement) factory.getElementById(npc
				.getId());

		effect.setPosition(element.getVars().getVar(
				EAdSceneElementVars.VAR_POSITION));
		effect.setStateVar(element.getVars().getVar(
				EAdSceneElementVars.VAR_STATE));
		effect.setDimensions(
				element.getVars().getVar(EAdSceneElementVars.VAR_WIDTH),
				element.getVars().getVar(EAdSceneElementVars.VAR_HEIGHT),
				element.getVars().getVar(EAdSceneElementVars.VAR_SCALE));

		return effect;
	}

}
