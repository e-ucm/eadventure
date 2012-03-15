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

package ead.engine.gl;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.AbstractGUI;
import ead.engine.core.platform.EngineConfiguration;

@Singleton
public class GLGUI extends AbstractGUI<GL10> {
	
	private GLSurfaceView glSurfaceView;

	@Inject
	public GLGUI(EngineConfiguration platformConfiguration,
			GameObjectManager gameObjectManager, InputHandler inputHandler,
			GameState gameState, SceneElementGOFactory gameObjectFactory,
			GLCanvas canvas, GameLoop gameLoop) {
		super(platformConfiguration, gameObjectManager, inputHandler,
				gameState, gameObjectFactory, canvas, gameLoop);
	}

	@Override
	public void showSpecialResource(Object object, int x, int y,
			boolean fullscreen) {
		
	}
	
	public void setGLSurfaceView(GLSurfaceView glSurfaceView){
		this.glSurfaceView = glSurfaceView;
	}

	@Override
	public void commit(float interpolation) {
		processInput();
		glSurfaceView.requestRender();
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void finish() {
		
	}
	
	public GLSurfaceView getGLSurfaceView( ){
		return glSurfaceView;
	}

}
