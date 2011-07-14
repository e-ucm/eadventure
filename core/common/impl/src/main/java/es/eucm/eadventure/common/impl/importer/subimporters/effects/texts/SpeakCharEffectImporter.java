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
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class SpeakCharEffectImporter extends
		TextEffectImporter<SpeakCharEffect> {

	@Inject
	public SpeakCharEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public EAdShowText convert(SpeakCharEffect oldObject, Object object) {
		EAdShowText effect = super.convert(oldObject, object);

		EAdString text = stringHandler.addNewString(oldObject.getLine());

		NPC p = factory.getCurrentOldChapterModel().getCharacter(
				oldObject.getTargetId());
		CaptionImpl c = new CaptionImpl(text);
		EAdColor center = EAdColor.valueOf("0x" + p.getTextFrontColor().substring(1) + "ff");
		EAdColor border = EAdColor.valueOf("0x" + p.getTextBorderColor().substring(1) + "ff");
		c.setTextColor(new EAdBorderedColor(center, border));

		EAdColor bubbleCenter = EAdColor.valueOf("0x" + p.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = EAdColor.valueOf("0x" + p.getBubbleBorderColor().substring(1) + "ff");
		c.setBubbleColor(new EAdBorderedColor(bubbleCenter, bubbleBorder));
	
		//FIXME fix position
		effect.setCaption(c, 10, 10);

		return effect;
	}

}
