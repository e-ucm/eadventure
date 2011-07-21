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
import es.eucm.eadventure.common.data.chapter.effects.SpeakPlayerEffect;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BallonShape.BalloonType;

public class SpeakPlayerEffectImporter extends
		TextEffectImporter<SpeakPlayerEffect> {

	@Inject
	public SpeakPlayerEffectImporter(StringHandler stringHandler,
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(stringHandler, conditionImporter, factory);
	}

	@Override
	public EAdSpeakEffect convert(SpeakPlayerEffect oldObject, Object newElement) {
		EAdSpeakEffect effect = super.convert(oldObject, newElement);

		EAdString text = stringHandler.addNewString(oldObject.getLine());
		effect.setText(text);
		effect.setBalloonType(BalloonType.ROUNDED_RECTANGLE);

		Player p = factory.getCurrentOldChapterModel().getPlayer();

		EAdColor center = EAdColor.valueOf("0x" + p.getTextFrontColor().substring(1) + "ff");
		EAdColor border = EAdColor.valueOf("0x" + p.getTextBorderColor().substring(1) + "ff");

		EAdColor bubbleCenter = EAdColor.valueOf("0x" + p.getBubbleBkgColor().substring(1) + "ff");
		EAdColor bubbleBorder = EAdColor.valueOf("0x" + p.getBubbleBorderColor().substring(1)
				+ "ff");
		
		effect.setColor(new EAdBorderedColor(center, border), new EAdBorderedColor(bubbleCenter, bubbleBorder));
		
		// FIXME Wrong, element holds an actor, and we need the reference
		EAdSceneElement element = (EAdSceneElement) factory.getElementById(Player.IDENTIFIER);
		
		effect.setPosition(element.getVars().getVar(EAdSceneElementVars.VAR_X), element.getVars().getVar(EAdSceneElementVars.VAR_Y));

		return effect;
	}

}
