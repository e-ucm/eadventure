package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.scenes.VideoScene;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import ead.elementfactories.demos.SceneDemo;

public class NgWindow extends VideoScene implements SceneDemo {
	public NgWindow() {
		super();
		setId("windowVideoScene");
		EAdVideo video = new Video("@binary/eAdventure.webm");
		getDefinition().getResources().addAsset(VideoScene.video, video);
	}

	@Override
	public String getSceneDescription() {
		return "A scene showing a video";
	}

	public String getDemoName() {
		return "Video Scene";
	}
}
