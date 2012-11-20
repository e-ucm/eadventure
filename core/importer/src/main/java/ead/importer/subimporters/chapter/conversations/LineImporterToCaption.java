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
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.importer.GenericImporter;
import ead.importer.interfaces.EAdElementFactory;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;

public class LineImporterToCaption implements
		GenericImporter<ConversationLine, EAdCaption> {

	@Inject
	private EAdElementFactory factory;

	@Inject
	private StringHandler stringHandler;

	@Override
	public EAdCaption init(ConversationLine line) {
		return null;
	}

	@Override
	public EAdCaption convert(ConversationLine line, Object object) {
		// Set caption attributes
		EAdString string = EAdString.newRandomEAdString("line");
		stringHandler.setString(string, line.getText());
		Caption caption = new Caption(string);

		NPC character = null;
		if (line.getName().equals(Player.IDENTIFIER)) {
			character = factory.getCurrentOldChapterModel().getPlayer();
		} else {
			character = factory.getCurrentOldChapterModel().getCharacter(
					line.getName());
		}

		if (character != null) {
			String bg = getColor(character.getBubbleBkgColor());
			String borderBg = getColor(character.getBubbleBorderColor());
			String textColor = getColor(character.getTextFrontColor());
			String textBorderColor = getColor(character.getTextBorderColor());

			if (bg != null) {
				Paint bubble = new Paint(new ColorFill(bg), new ColorFill(
						borderBg));
				caption.setBubblePaint(bubble);
			}

			Paint text = new Paint(new ColorFill(textColor), new ColorFill(
					textBorderColor));

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
		} else {
			return "0x00000000";
		}
	}
}
