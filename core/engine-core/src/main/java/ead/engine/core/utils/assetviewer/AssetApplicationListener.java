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

package ead.engine.core.utils.assetviewer;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.google.inject.Inject;

import ead.engine.core.assets.drawables.RuntimeDrawable;
import ead.engine.core.assets.fonts.FontHandler;
import ead.engine.core.utils.InvOrtographicCamera;
import ead.tools.reflection.ReflectionProvider;

@SuppressWarnings( { "unchecked", "rawtypes" })
public class AssetApplicationListener implements ApplicationListener {

	public static final int DEFAULT_PATTERN_SIZE = 20;
	public int patternSize = DEFAULT_PATTERN_SIZE;

	private InvOrtographicCamera camera;
	private SpriteBatch spriteBatch;
	private Graphics graphics;
	private Texture squareTexture;

	private int canvasWidth;
	private int canvasHeight;
	private int drawableWidth;
	private int drawableHeight;

	private Matrix4 transformation;
	private Matrix4 idt = new Matrix4();

	private RuntimeDrawable currentDrawable;
	private int time;
	private List<String> states;

	@Inject
	public AssetApplicationListener(FontHandler fontHandler,
			ReflectionProvider reflectionProvider) {
		//XXX	canvas = new GenericCanvas();
		transformation = new Matrix4();
		transformation.idt();
		idt.idt();
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	/**
	 * Sets the size of the side of the background pattern squares
	 * 
	 * @param size
	 *            size in pixels
	 */
	public void setPatternSize(int size) {
		this.patternSize = size;
	}

	public void setDrawable(RuntimeDrawable drawable) {
		if (currentDrawable != null) {
			currentDrawable.freeMemory();
		}
		this.currentDrawable = drawable;
		drawableWidth = currentDrawable.getDrawable(time, states, 0).getWidth();
		drawableHeight = currentDrawable.getDrawable(time, states, 0)
				.getHeight();
		updateZoom();
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,
				GL20.GL_ONE_MINUS_SRC_ALPHA);
		camera = new InvOrtographicCamera();
		// XXX canvas.setGraphicContext(spriteBatch);
		time = 0;
		createBackgroundPattern();
	}

	private void createBackgroundPattern() {
		Pixmap square = new Pixmap(patternSize * 2, patternSize,
				Pixmap.Format.RGB888);
		square.setColor(Color.GRAY);
		square.fillRectangle(0, 0, patternSize, patternSize);
		square.setColor(Color.LIGHT_GRAY);
		square.fillRectangle(patternSize, 0, patternSize, patternSize);
		squareTexture = new Texture(square);
		square.dispose();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		if (graphics != null) {
			time += graphics.getDeltaTime() * 1000;
			graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		spriteBatch.begin();

		// Background pattern
		camera.zoom = 1;
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.setTransformMatrix(idt);
		for (int i = 0; i <= canvasWidth + patternSize * 2; i += patternSize * 2) {
			for (int j = 0; j <= canvasHeight; j += patternSize) {
				spriteBatch.draw(squareTexture, i
						- ((j / patternSize) % 2 == 0 ? patternSize : 0), j);
			}
		}

		// Asset
		camera.zoom = zoom;
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.setTransformMatrix(transformation);

		if (currentDrawable != null) {
			//currentDrawable.getDrawable(time, states, 0).render(canvas);
		}

		spriteBatch.end();
	}

	private float zoom = 1;

	@Override
	public void resize(int w, int h) {
		canvasWidth = w;
		canvasHeight = h;

		float centerX = canvasWidth / 2;
		float centerY = canvasHeight / 2;

		camera.position.set(centerX, centerY, 0);
		camera.viewportWidth = canvasWidth;
		camera.viewportHeight = canvasHeight;

		updateZoom();
	}

	@Override
	public void resume() {

	}

	private void updateZoom() {
		transformation.idt();
		transformation.translate((canvasWidth - drawableWidth) / 2,
				(canvasHeight - drawableHeight) / 2, 0);

		float zoomX = (float) drawableWidth / (float) this.canvasWidth;
		float zoomY = (float) drawableHeight / (float) this.canvasHeight;
		zoom = zoomX > zoomY ? zoomX : zoomY;
	}

}
