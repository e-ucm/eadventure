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

package ead.common.importer.subimporters.effects.texts;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.params.fills.EAdColor;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;

public class SpeakPlayerEffectImporter extends
		TextEffectImporter<SpeakPlayerEffect> {

	private NPC npc;

	@Inject
	public SpeakPlayerEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public SpeakEf init(SpeakPlayerEffect oldObject) {
		npc = factory.getCurrentOldChapterModel().getPlayer();
		if ( factory.isFirstPerson() ){
			return new SpeakEf();
		}
		else{
			SpeakSceneElementEf effect = new SpeakSceneElementEf();
			effect.setElement(factory.getElementById(npc.getId()));
			return effect;
		}
	}

	@Override
	public SpeakEf convert(SpeakPlayerEffect oldObject,
			Object object) {
		SpeakEf effect = super.convert(oldObject, object);

		String line = oldObject.getLine();

		BalloonType type = BalloonType.ROUNDED_RECTANGLE;
		if (line.startsWith(SpeakCharEffectImporter.WHISPER)) {
			// TODO Whisper balloon
			type = BalloonType.ROUNDED_RECTANGLE;
			line = line.substring(SpeakCharEffectImporter.WHISPER.length());
		} else if (line.startsWith(SpeakCharEffectImporter.THOUGHT)) {
			type = BalloonType.CLOUD;
			line = line.substring(SpeakCharEffectImporter.THOUGHT.length());
		} else if (line.startsWith(SpeakCharEffectImporter.YELL)) {
			type = BalloonType.ELECTRIC;
			line = line.substring(SpeakCharEffectImporter.YELL.length());
		}

		
		for ( EAdOperation op: TextEffectImporter.getOperations(oldObject.getLine(), factory)){
			effect.getCaption().getFields().add(op);
		}
		String finalLine = TextEffectImporter.translateLine(oldObject.getLine());
		stringHandler.setString(effect.getString(), finalLine);
		effect.setBalloonType(type);

		EAdColor center = new EAdColor("0x"
				+ npc.getTextFrontColor().substring(1) + "ff");
		EAdColor border = new EAdColor("0x"
				+ npc.getTextBorderColor().substring(1) + "ff");

		EAdColor bubbleCenter = new EAdColor("0x"
				+ npc.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = new EAdColor("0x"
				+ npc.getBubbleBorderColor().substring(1) + "ff");

		effect.setColor(new EAdPaintImpl(center, border), new EAdPaintImpl(
				bubbleCenter, bubbleBorder));
		
		return effect;
	}

}
