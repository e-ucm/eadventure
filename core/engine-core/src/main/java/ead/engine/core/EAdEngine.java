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

package ead.engine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.operations.SystemFields;
import ead.engine.core.canvas.GdxCanvas;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.utils.InvOrtographicCamera;

@Singleton
public class EAdEngine implements ApplicationListener {

	private Game game;

	private GameState gameState;

	private GUI gui;

	private Stage stage;

	private InvOrtographicCamera c;

	private SpriteBatch spriteBatch;

	private Vector2 sceneMouseCoordinates;

	private float scaleX;

	private float scaleY;

	@Inject
	public EAdEngine(Game game, GameState gameState, GUI gui) {
		ShaderProgram.pedantic = false;
		this.game = game;
		game.setEAdEngine(this);
		this.gameState = gameState;
		this.gui = gui;
		this.sceneMouseCoordinates = new Vector2();
	}

	@Override
	public void create() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		game.initialize();

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		gameState.setValue(SystemFields.GAME_WIDTH, width);
		gameState.setValue(SystemFields.GAME_HEIGHT, height);

		spriteBatch = new GdxCanvas();
		stage = new Stage(width, height, true, spriteBatch);
		spriteBatch.enableBlending();

		c = new InvOrtographicCamera();
		float centerX = width / 2;
		float centerY = height / 2;

		c.position.set(centerX, centerY, 0);
		c.viewportWidth = width;
		c.viewportHeight = height;

		stage.setCamera(c);

		Gdx.input.setInputProcessor(stage);

		stage.addActor(gui.getRoot());
		stage.setKeyboardFocus(gui.getRoot());

		game.restart(true);
	}

	@Override
	public void dispose() {
		game.dispose();
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.act(gui.getSkippedMilliseconds());
		stage.act(gui.getSkippedMilliseconds());
		sceneMouseCoordinates.set(Gdx.input.getX(), Gdx.input.getY());
		stage.screenToStageCoordinates(sceneMouseCoordinates);
		gameState.setValue(SystemFields.MOUSE_SCENE_X, Float
				.valueOf(sceneMouseCoordinates.x));
		gameState.setValue(SystemFields.MOUSE_SCENE_Y, Float
				.valueOf(sceneMouseCoordinates.y));
		gameState.setValue(SystemFields.MOUSE_X, Float.valueOf(Gdx.input.getX()
				/ scaleX));
		gameState.setValue(SystemFields.MOUSE_Y, Float.valueOf(Gdx.input.getY()
				/ scaleY));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		scaleX = (float) width / 800.0f;
		scaleY = (float) height / 600.0f;
		gui.getRoot().setScaleX(scaleX);
		gui.getRoot().setScaleY(scaleY);
	}

	public Stage getStage() {
		return stage;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
