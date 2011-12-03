package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;

public class SoundScene extends EmptyScene {
	
	public SoundScene( ){
		CaptionImpl caption = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Play", EAdPaintImpl.WHITE_ON_BLACK, EAdPaintImpl.BLACK_ON_WHITE, new EAdFontImpl( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect = EAdElementsFactory.getInstance().getEffectFactory().getPlaySound("@binary/sound.mp3" );
		getComponents().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption, 10, 10, effect));
	}
	
	
	@Override
	public String getSceneDescription() {
		return "A scene where a sound is played";
	}
	
	public String getDemoName(){
		return "Sound Scene";
	}
}
