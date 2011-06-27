package es.eucm.eadventure.common.elmentfactories.assets;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;

public class CaptionFactory {

	public CaptionImpl createCaption(String text, EAdBorderedColor textColor,
			EAdBorderedColor bubbleColor, EAdFont font) {
		CaptionImpl caption = new CaptionImpl();
		caption.setText(EAdElementsFactory.getInstance().getStringFactory()
				.getString(text));
		caption.setTextColor(textColor);
		caption.setBubbleColor(bubbleColor);
		caption.setFont(font);
		return caption;

	}

	public CaptionImpl createCaption(String text, EAdBorderedColor textColor,
			EAdBorderedColor bubbleColor) {
		return createCaption(text, textColor, bubbleColor, EAdFont.REGULAR);
	}

	public CaptionImpl createCaption(String text) {
		return createCaption(text, EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE);
	}

}
