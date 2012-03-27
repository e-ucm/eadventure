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
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.SpeakCharEffect;
import es.eucm.eadventure.common.data.chapter.elements.NPC;

public class SpeakCharEffectImporter extends
		TextEffectImporter<SpeakCharEffect> {

	private NPC npc;

	@Inject
	public SpeakCharEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public SpeakEf init(SpeakCharEffect oldObject) {
		npc = factory.getCurrentOldChapterModel().getCharacter(
				oldObject.getTargetId());
		SpeakSceneElementEf effect = new SpeakSceneElementEf();
		effect.setElement(factory.getElementById(npc.getId()));
		return effect;
	}

	@Override
	public SpeakEf convert(SpeakCharEffect oldObject, Object object) {
		SpeakEf effect = super.convert(oldObject, object);

		for (EAdOperation op : TextEffectImporter.getOperations(
				oldObject.getLine(), factory)) {
			effect.getCaption().getFields().add(op);
		}

		String line = setBallonType(effect, oldObject.getLine());


		line = TextEffectImporter.translateLine(line);
		stringHandler.setString(effect.getString(), line);
		setColor(effect, npc);

		return effect;
	}

	public static void setColor(SpeakEf effect, NPC npc) {

		ColorFill center = new ColorFill("0x"
				+ npc.getTextFrontColor().substring(1) + "ff");
		ColorFill border = new ColorFill("0x"
				+ npc.getTextBorderColor().substring(1) + "ff");

		ColorFill bubbleCenter = new ColorFill("0x"
				+ npc.getBubbleBkgColor().substring(1) + "ff");
		ColorFill bubbleBorder = new ColorFill("0x"
				+ npc.getBubbleBorderColor().substring(1) + "ff");

		effect.setColor(new Paint(center, border), new Paint(
				bubbleCenter, bubbleBorder));
	}

}
