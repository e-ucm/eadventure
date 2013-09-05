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

package es.eucm.ead.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.canvas.GdxCanvas;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.utils.InvOrtographicCamera;
import es.eucm.ead.model.elements.operations.SystemFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EAdEngine implements ApplicationListener {

	static private Logger logger = LoggerFactory.getLogger(EAdEngine.class);

	private Game game;

	private GameState gameState;

	private GUI gui;

	private Stage stage;

	private Vector2 sceneMouseCoordinates;

	/**
	 * Reference width for the game. This width is independent from the window size. It's in stage relative units
	 */
	private float gameWidth;

	/**
	 * Reference height for the game. This height is independent from the window size. It's in stage relative units
	 */
	private float gameHeight;

	@Inject
	public EAdEngine(Game game, GameState gameState, GUI gui) {
		ShaderProgram.pedantic = false;
		this.game = game;
		this.gameState = gameState;
		this.gui = gui;
		this.sceneMouseCoordinates = new Vector2();
		this.gameWidth = gameHeight = -1;
	}

	public void setGameWidth(float gameWidth) {
		this.gameWidth = gameWidth;
		gameState.setValue(SystemFields.GAME_WIDTH, (int) gameWidth);
	}

	public void setGameHeight(float gameHeight) {
		this.gameHeight = gameHeight;
		gameState.setValue(SystemFields.GAME_HEIGHT, (int) gameHeight);
	}

	@Override
	public void create() {
		logger.debug("Creating graphic context");
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		int width = 800;
		int height = 600;

		GdxCanvas spriteBatch = new GdxCanvas();
		stage = new Stage(width, height, true, spriteBatch);
		spriteBatch.enableBlending();

		InvOrtographicCamera c = new InvOrtographicCamera();
		float centerX = width / 2;
		float centerY = height / 2;

		c.position.set(centerX, centerY, 0);
		c.viewportWidth = width;
		c.viewportHeight = height;

		stage.setCamera(c);
		gui.setStage(stage);
		Gdx.input.setInputProcessor(stage);

		stage.addActor(new Logo());

	}

	@Override
	public void dispose() {
		game.dispose();
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.act(game.getSkippedMilliseconds());
		stage.act(game.getSkippedMilliseconds());
		sceneMouseCoordinates.set(Gdx.input.getX(), Gdx.input.getY());
		stage.getRoot().parentToLocalCoordinates(sceneMouseCoordinates);
		gameState.setValue(SystemFields.MOUSE_X, sceneMouseCoordinates.x);
		gameState.setValue(SystemFields.MOUSE_Y, sceneMouseCoordinates.y);
		gui.getScene().parentToLocalCoordinates(sceneMouseCoordinates);
		gameState.setValue(SystemFields.MOUSE_SCENE_X, sceneMouseCoordinates.x);
		gameState.setValue(SystemFields.MOUSE_SCENE_Y, sceneMouseCoordinates.y);
		stage.draw();
		game.doHook(Game.HOOK_AFTER_RENDER);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		if (gameWidth > 0) {
			float scaleX = (float) width / gameWidth;
			float scaleY = (float) height / gameHeight;
			stage.getRoot().setScale(scaleX, scaleY);
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public class Logo extends Actor {

		private TextureRegion textureRegion = null;

		private float alpha = 0.0f;

		private float initialTime = 1000f;

		public Logo() {
			Texture texture = new Texture(Gdx.files
					.internal("eadventurelogo.png"));
			textureRegion = new TextureRegion(texture);
			textureRegion.flip(false, true);
		}

		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			batch.draw(textureRegion, stage.getWidth() / 2
					- textureRegion.getRegionWidth() / 2, stage.getHeight() / 2
					- textureRegion.getRegionHeight() / 2);
		}

		public void act(float delta) {
			if (initialTime > 0.0f) {
				initialTime -= delta;
			} else if (initialTime <= 0.0f) {
				alpha += delta / 2000.0f;
				alpha = Math.min(1.0f, alpha);
			}
		}
	}

}
