package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.VideoImpl;

public class VideoScene extends EAdVideoScene implements SceneDemo {

	public VideoScene() {
		super("videoScene");
		Video video = new VideoImpl("@binary/bbb_trailer_400p.ogv");
		getResources().addAsset(EAdVideoScene.video, video);
		setUpForEngine();
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing a video";
	}

	public String getDemoName() {
		return "Video Scene";
	}

}
