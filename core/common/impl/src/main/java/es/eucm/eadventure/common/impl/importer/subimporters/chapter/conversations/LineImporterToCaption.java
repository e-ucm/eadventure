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

import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;

public class LineImporterToCaption implements
		GenericImporter<ConversationLine, Caption> {

	@Inject
	private EAdElementFactory factory;

	@Inject
	private StringHandler stringHandler;

	public Caption init(ConversationLine line) {
		return null;
	}

	@Override
	public Caption convert(ConversationLine line, Object object) {
		// Set caption attributes
		EAdString string = EAdString.newEAdString("line");
		stringHandler.setString(string, line.getText());
		CaptionImpl caption = new CaptionImpl(string);

		NPC character = null;
		if (line.getName().equals(Player.IDENTIFIER))
			character = factory.getCurrentOldChapterModel().getPlayer();
		else
			character = factory.getCurrentOldChapterModel().getCharacter(
					line.getName());

		if (character != null) {
			String bg = getColor(character.getBubbleBkgColor());
			String borderBg = getColor(character.getBubbleBorderColor());
			String textColor = getColor(character.getTextFrontColor());
			String textBorderColor = getColor(character.getTextBorderColor());

			if (bg != null) {
				EAdPaintImpl bubble = new EAdPaintImpl(
						new EAdColor(bg), new EAdColor(borderBg));
				caption.setBubblePaint(bubble);
			}

			EAdPaintImpl text = new EAdPaintImpl(
					new EAdColor(textColor), new EAdColor(textBorderColor));

			caption.setTextPaint(text);
		}
		return caption;
	}

	private String getColor(String oldColor) {
		if (oldColor != null) {
			String color = oldColor;
			if (oldColor.startsWith("#")) {
				color = color.substring(1);
			}
			while (color.length() < 6) {
				color = "0" + color;
			}

			return "0x" + color + "FF";
		} else
			return "0x00000000";
	}

}
