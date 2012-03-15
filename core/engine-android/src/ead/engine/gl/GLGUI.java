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
