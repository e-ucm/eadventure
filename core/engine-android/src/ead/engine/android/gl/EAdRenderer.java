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

package ead.engine.android.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;

public class EAdRenderer implements Renderer {
	
	private float[] projectionMatrix = new float[16];
	
	private GenericCanvas eAdCanvas;
	
	private GameObjectManager gameObjects;
	
	public EAdRenderer(GLCanvas canvas, GameObjectManager gameObjects ){
		this.eAdCanvas = canvas;
		this.gameObjects = gameObjects;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
		synchronized (GameObjectManager.lock) {
			for (DrawableGO<?> go : gameObjects.getGameObjects()) {
				if (go != null && go.getRuntimeDrawable() != null) {
					EAdTransformation t = go.getTransformation();
					eAdCanvas.setTransformation(t);
					go.getRuntimeDrawable().render(eAdCanvas);
				}

			}
		}
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);	
		Matrix.orthoM(projectionMatrix, 0, 0, 800, 600, 0, 1, -1);
		((GLCanvas) eAdCanvas).init();
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}

}
