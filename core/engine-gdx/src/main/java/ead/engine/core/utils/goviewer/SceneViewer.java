package ead.engine.core.utils.goviewer;

import com.badlogic.gdx.ApplicationListener;

import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public class SceneViewer implements ApplicationListener {

	private EAdTransformation initialTransformation;

	private GUI gui;

	private EngineConfiguration configuration;

	private SceneGO<?> currentScene;

	private int sceneWidth;
	private int sceneHeight;
	private int containerWidth;
	private int containerHeight;

	@Override
	public void create() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		updateInitialTransformation();
		currentScene.update();
		gui.addElement(currentScene, initialTransformation);
		gui.commit(0);
		gui.prepareGUI();
	}

	public void updateInitialTransformation() {
		if (initialTransformation != null) {
			initialTransformation.setValidated(true);
		}

		if (containerWidth != configuration.getWidth()
				|| containerHeight != configuration.getHeight()) {

			containerWidth = configuration.getWidth();
			containerHeight = configuration.getHeight();

			float scaleX = configuration.getWidth() / (float) sceneWidth;
			float scaleY = configuration.getHeight() / (float) sceneHeight;

			float scale = scaleX < scaleY ? scaleX : scaleY;
			float dispX = Math.abs(sceneWidth * scaleX - sceneWidth * scale) / 2;
			float dispY = Math.abs(sceneHeight * scaleY - sceneHeight * scale) / 2;

			initialTransformation = new EAdTransformationImpl();
			initialTransformation.getMatrix().translate(dispX, dispY, true);
			initialTransformation.getMatrix().scale(scale, scale, true);
			initialTransformation.setValidated(false);
			gui.setInitialTransformation(initialTransformation);
		}
	}

	@Override
	public void resize(int arg0, int arg1) {

	}

	@Override
	public void resume() {

	}

}
