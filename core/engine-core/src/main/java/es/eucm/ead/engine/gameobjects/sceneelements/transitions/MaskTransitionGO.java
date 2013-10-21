/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.engine.gameobjects.sceneelements.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.drawables.RuntimeDrawable;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.transitions.MaskTransition;

public class MaskTransitionGO extends TransitionGO<MaskTransition> {

	private int remainingTime;

	private Texture texture;

	private static ShaderProgram maskShader;

	private static FrameBuffer nextSceneBuffer;

	private Vector3 offset;

	@Inject
	public MaskTransitionGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
	}

	public void setElement(SceneElement e) {
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
	public void transition(SceneGO previousScene, SceneGO nextScene,
			TransitionListener transitionListener) {
		super.transition(previousScene, nextScene, transitionListener);
		this.remainingTime = transition.getTime();
		offset.set(800, 0, 0);
		this.addSceneElement(nextScene);
	}

	public void act(float delta) {
		super.act(delta);
		offset.x -= (delta / transition.getTime()) * 1600;
		if (nextScene != null) {
			nextScene.setY(0);
			nextScene.setX(0);
			remainingTime -= game.getSkippedMilliseconds();
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
