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

package ead.engine.core.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.game.Game;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.gdx.platform.GdxInputHandler;
import ead.engine.core.gdx.utils.InvOrtographicCamera;
import ead.engine.core.input.InputHandler;

@Singleton
public class GdxEngineImpl implements GdxEngine {

	private SpriteBatch spriteBatch;

	private InvOrtographicCamera c;

	private Game game;

	private GdxCanvas canvas;

	private InputHandler inputHandler;

	@Inject
	public GdxEngineImpl(GdxCanvas canvas, Game game, InputHandler inputHandler) {
		ShaderProgram.pedantic = false;
		this.canvas = canvas;
		this.game = game;
		this.inputHandler = inputHandler;
	}

	@Override
	public void create() {
		game.initialize();
		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,
				GL20.GL_ONE_MINUS_SRC_ALPHA);

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		c = new InvOrtographicCamera();
		float centerX = width / 2;
		float centerY = height / 2;

		c.position.set(centerX, centerY, 0);
		c.viewportWidth = width;
		c.viewportHeight = height;

		canvas.setGraphicContext(spriteBatch);

		Gdx.input.setInputProcessor(new GdxInputHandler(inputHandler, c));
	}

	@Override
	public void dispose() {
		game.dispose();
	}

	@Override
	public void render() {
		c.update();
		spriteBatch.setProjectionMatrix(c.combined);
		game.update();
		spriteBatch.begin();
		game.render();
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
