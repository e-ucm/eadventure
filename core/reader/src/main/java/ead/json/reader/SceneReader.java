package ead.json.reader;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.multimedia.EAdSound;
import ead.common.model.elements.effects.PlaySoundEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.util.Position;
import ead.json.reader.model.JsonScene;
import ead.json.reader.model.JsonScene.JsonSceneElement;
import ead.reader.model.ObjectsFactory;

public class SceneReader {

	private ObjectsFactory objectsFactory;

	public SceneReader(ObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}

	public EAdScene parseScene(JsonScene jsonScene) {
		EAdDrawable background = (EAdDrawable) objectsFactory
				.getAsset(jsonScene.background);
		BasicScene scene = new BasicScene(background);
		if (jsonScene.id != null)
			scene.setId(jsonScene.id);

		objectsFactory.putEAdElement(scene.getId(), scene);

		EAdSound bgMusic = (EAdSound) objectsFactory.getAsset(jsonScene.music);
		PlaySoundEf playBg = new PlaySoundEf(bgMusic, true);
		scene.addInitEffect(playBg);

		if (jsonScene.sceneElements != null) {
			for (JsonSceneElement e : jsonScene.sceneElements) {
				EAdSceneElement s = parseSceneElement(e);
				if (e.id != null)
					s.setId(e.id);
				objectsFactory.putEAdElement(s.getId(), s);
				scene.add(s);
			}
		}

		return scene;
	}

	public EAdSceneElement parseSceneElement(JsonSceneElement jsonSceneElement) {
		EAdDrawable d = (EAdDrawable) objectsFactory
				.getAsset(jsonSceneElement.appearance);
		SceneElement e = new SceneElement(d);
		e.setPosition(new Position(jsonSceneElement.x, jsonSceneElement.y,
				jsonSceneElement.disp_x, jsonSceneElement.disp_y));
		e.setInitialVisible(jsonSceneElement.visible);
		e.setInitialEnable(jsonSceneElement.enable);
		e.setInitialRotation(jsonSceneElement.rotation);
		e.setInitialScale(jsonSceneElement.scale);
		e.setInitialScale(jsonSceneElement.scale_x, jsonSceneElement.scale_y);
		e.setInitialAlpha(jsonSceneElement.alpha);
		return e;
	}

}
