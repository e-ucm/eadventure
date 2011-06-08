package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class LineImporterToCaption implements
		Importer<ConversationLine, Caption> {

	@Inject
	private EAdElementFactory factory;

	@Inject
	private StringHandler stringHandler;

	@Override
	public Caption convert(ConversationLine line) {
		// Set caption attributes
		EAdString string = stringHandler.addNewString(line.getText());
		CaptionImpl caption = new CaptionImpl(string);

		NPC character = null;
		if (line.getName().equals(Player.IDENTIFIER))
			character = factory.getCurrentOldChapterModel().getPlayer();
		else
			character = factory.getCurrentOldChapterModel().getCharacter(
					line.getName());

		if (character != null) {
			String bg = getColor( character.getBubbleBkgColor() );
			String borderBg = getColor( character.getBubbleBorderColor() );
			String textColor = getColor( character.getTextFrontColor() );
			String textBorderColor = getColor( character.getTextBorderColor() );

			if (bg != null) {
				EAdBorderedColor bubble = new EAdBorderedColor(
						EAdColor.valueOf(bg), EAdColor.valueOf(borderBg));
				caption.setBubbleColor(bubble);
			}

			EAdBorderedColor text = new EAdBorderedColor(
					EAdColor.valueOf(textColor),
					EAdColor.valueOf(textBorderColor));

			caption.setTextColor(text);
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

	@Override
	public boolean equals(ConversationLine oldObject, Caption newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
