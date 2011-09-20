package es.eucm.eadventure.engine.core.platform.impl.extra;

import playn.core.Canvas;
import playn.core.Path;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.FillFactory;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class AssetRendererMapProviderFactory {
	
	private FillFactory<Canvas, Path> fillFactory;
	
	private AssetHandler assetHandler;
	
	private PlatformConfiguration platformConfiguration;
	
	private MouseState mouseState;
	
	private GameObjectFactory gameObjectFactory;
	
	@Inject
	public AssetRendererMapProviderFactory(FillFactory<Canvas, Path> fillFactory,
			AssetHandler assetHandler,
			PlatformConfiguration platformConfiguration,
			MouseState mouseState,
			GameObjectFactory gameObjectFactory) {
		this.fillFactory = fillFactory;
		this.assetHandler = assetHandler;
		this.platformConfiguration = platformConfiguration;
		this.mouseState = mouseState;
		this.gameObjectFactory = gameObjectFactory;
	}

	public MapProvider<Class<?>, GraphicRenderer<?, ?>> create(GraphicRendererFactory<?> graphicRendererFactory) {
		return new PlayNAssetRendererMapProvider(fillFactory, assetHandler, graphicRendererFactory, platformConfiguration, mouseState, gameObjectFactory);
	}
	
}
