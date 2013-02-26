package ead.engine.core.gameobjects.sceneelements.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.Inject;

import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.transitions.MaskTransition;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.drawables.RuntimeDrawable;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneGO;

public class MaskTransitionGO extends TransitionGO<MaskTransition> {

	private int remainingTime;

	private Texture texture;

	private static ShaderProgram maskShader;

	private static FrameBuffer nextSceneBuffer;

	private Vector3 offset;

	@Inject
	public MaskTransitionGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
	}

	public void setElement(EAdSceneElement e) {
		super.setElement(e);
		RuntimeDrawable<?> r = (RuntimeDrawable<?>) assetHandler
				.getRuntimeAsset(transition.getMask());
		texture = r.getTextureHandle();
		offset = new Vector3();
		if (maskShader == null) {
			maskShader = new ShaderProgram(assetHandler
					.getTextFile("@binary/shaders/mask.vert"), assetHandler
					.getTextFile("@binary/shaders/mask.frag"));
			if (maskShader.getLog().length() != 0)
				logger.warn(maskShader.getLog());
			nextSceneBuffer = new FrameBuffer(Format.RGBA8888, 800, 600, false);
		}
		maskShader.begin();
		maskShader.setUniformi("mask_texture", 1);
		maskShader.setAttributef("a_sizes", 800, 600, r.getWidth(), r
				.getHeight());
		maskShader.end();
	}

	@Override
	public void transition(SceneGO nextScene,
			TransitionListener transitionListener) {
		super.transition(nextScene, transitionListener);
		this.remainingTime = transition.getTime();
		offset.set(800, 0, 0);
		this.addSceneElement(nextScene);
	}

	public void act(float delta) {
		super.act(delta);
		offset.x -= (delta / transition.getTime()) * 1600;
		nextScene.setY(0);
		nextScene.setX(0);
		if (nextScene != null) {
			remainingTime -= gui.getSkippedMilliseconds();
			if (remainingTime <= 0) {
				nextScene.setVisible(true);
				super.finish();
			}
		}
	}

	@Override
	public void drawChildren(SpriteBatch batch, float parentAlpha) {
		previousScene.setVisible(true);
		nextScene.setVisible(false);
		super.drawChildren(batch, parentAlpha);
		batch.end();
		nextSceneBuffer.begin();
		batch.begin();

		previousScene.setVisible(false);
		nextScene.setVisible(true);

		super.drawChildren(batch, parentAlpha);
		nextSceneBuffer.end();

		batch.flush();
		batch.setShader(maskShader);
		maskShader.setAttributef("a_offset", offset.x, offset.y, offset.z, 0);
		texture.bind(1);
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
		Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		batch.draw(nextSceneBuffer.getColorBufferTexture(), 0, 0);
		batch.setShader(null);
	}

}
