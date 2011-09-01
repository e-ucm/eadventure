package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;

public class SoundScene extends EmptyScene {
	
	public SoundScene( ){
		CaptionImpl caption = EAdElementsFactory.getInstance().getCaptionFactory().createCaption("Play", EAdBorderedColor.WHITE_ON_BLACK, EAdBorderedColor.BLACK_ON_WHITE, new EAdFontImpl( new EAdURIImpl( "@binary/DroidSans-Bold.ttf"), 20));
		EAdEffect effect = EAdElementsFactory.getInstance().getEffectFactory().getPlaySound("@binary/sound.mp3" );
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(caption, 10, 10, effect));
	}
	
	
	@Override
	public String getDescription() {
		return "A scene where a sound is played";
	}
	
	public String getDemoName(){
		return "Sound Scene";
	}
}
