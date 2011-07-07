package es.eucm.eadventure.engine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.graphics.Canvas;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.platform.impl.AbstractGUI;
import es.eucm.eadventure.engine.extra.AndroidCanvas;

@Singleton
public class AndroidGUI extends AbstractGUI<Canvas> {

	private AndroidCanvas canvas;
	
	@SuppressWarnings("rawtypes")
	@Inject
	public AndroidGUI(PlatformConfiguration platformConfiguration,
			GraphicRendererFactory<?> assetRendererFactory,
			GameObjectManager gameObjectManager,
			MouseState mouseState,
			KeyboardState keyboardState, BasicHUD basicHUD, ValueMap valueMap) {
		super(platformConfiguration, assetRendererFactory, gameObjectManager,
				mouseState, keyboardState, valueMap);
		this.gameObjects.addHUD(basicHUD);
	}
	
	@Override
	public RuntimeAsset<Image> commitToImage() {
		//FIXME does not commit to image
		return null;
	}

	@Override
	public void initilize() {
		
		
	}

	@Override
	public void commit(float interpolation) {
		processInput();
		
		if (canvas != null) {
			render(canvas, interpolation);
		}
	}

	@Override
	public void showSpecialResource(Object object, int x, int y,
			boolean fullscreen) {
		// TODO Auto-generated method stub
		
	}

	public void setCanvas(AndroidCanvas aCanvas) {
		this.canvas = aCanvas;
	}

}
