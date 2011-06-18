package es.eucm.eadventure.common.model.elements.test;

import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.VideoImpl;

public class VideoSceneFactoryTest {
	
	public static EAdVideoScene getVideoScene() {
		EAdVideoScene videoScene = new EAdVideoScene("videoscene");
		videoScene.getResources().addAsset(EAdVideoScene.video, new VideoImpl("@binary/flame.mpg"));
		videoScene.setUpForEngine();
		
		return videoScene;
	}

}
