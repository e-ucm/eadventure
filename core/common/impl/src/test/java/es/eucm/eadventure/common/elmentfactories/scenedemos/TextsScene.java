package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.StringFactory.StringType;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText.ShowTextAnimation;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;

public class TextsScene extends EmptyScene {
	
	public TextsScene( ){
		// Show text caption
		CaptionImpl caption = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Show text Á ÑÑÑ !!!! *Á", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFont( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect = EAdElementsFactory.getInstance().getEffectFactory().getShowText("This text is showing through an EAdShowText effect", 400, 200, ShowTextAnimation.FADE_IN );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption, 10, 10, effect));
		
		// Show question caption
		CaptionImpl caption2 = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Launch a question", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFont( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect question = EAdElementsFactory.getInstance().getEffectFactory().getShowQuestion("I have a question and you have to answer, I'm afraid", 5 );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption2, 10, 100, question));
		
		// Show text caption
		CaptionImpl caption3 = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Show very long text", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFont( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect2 = EAdElementsFactory.getInstance().getEffectFactory().getShowText(StringType.VERY_LONG_STRING.getString(), 400, 200, ShowTextAnimation.FADE_IN, 100 );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption3, 10, 200, effect2));
	}

}
