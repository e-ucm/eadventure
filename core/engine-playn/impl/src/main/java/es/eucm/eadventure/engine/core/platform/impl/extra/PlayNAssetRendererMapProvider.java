package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.HashMap;
import java.util.Map;

import playn.core.Canvas;
import playn.core.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.EffectHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.MenuHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.FillFactory;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.PlayNEngineSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNBezierShapeRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNCaptionRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNComposedDrawableRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNDisplacedDrawableRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNImageAssetRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.PlayNSpriteImageRenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.ActionsHudGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.BasicHudGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.BasicInventoryGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.BasicSceneElementRenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.EffectHudGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.EffectsGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.MenuHUDGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.SceneGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.TransitionGORenderer;
import es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers.VideoSceneGORenderer;

@Singleton
public class PlayNAssetRendererMapProvider implements MapProvider<Class<?>, GraphicRenderer<?, ?>>{

	private Map<Class<?>, GraphicRenderer<?, ?>> factoryMap;
	
	private FillFactory<Canvas, Path> fillFactory;
	
	private AssetHandler assetHandler;
	
	private GraphicRendererFactory<?> graphicRendererFactory;
	
	private PlatformConfiguration platformConfiguration;
	
	private MouseState mouseState;
	
	private GameObjectFactory gameObjectFactory;
	

	@Inject
	public PlayNAssetRendererMapProvider(FillFactory<Canvas, Path> fillFactory,
			AssetHandler assetHandler,
			GraphicRendererFactory<?> graphicRendererFactory,
			PlatformConfiguration platformConfiguration,
			MouseState mouseState,
			GameObjectFactory gameObjectFactory) {
		this.fillFactory = fillFactory;
		this.assetHandler = assetHandler;
		this.graphicRendererFactory = graphicRendererFactory;
		this.platformConfiguration = platformConfiguration;
		this.mouseState = mouseState;
		this.gameObjectFactory = gameObjectFactory;
	}

	public void addToGameObjectFactory(
			Class<?> key_value,
			GameObjectRenderer<?, ?> value_value) {
		factoryMap.put(key_value, value_value);
	}

	@Override
	public Map<Class<?>, GraphicRenderer<?, ?>> getMap() {
		if (factoryMap == null) {
			factoryMap = new HashMap<Class<?>, GraphicRenderer<?, ?>>();		

			factoryMap.put(PlayNEngineImage.class, new PlayNImageAssetRenderer());
			factoryMap.put(PlayNEngineCaption.class, new PlayNCaptionRenderer());
			factoryMap.put(PlayNBezierShape.class, new PlayNBezierShapeRenderer(fillFactory));
			factoryMap.put(RuntimeComposedDrawable.class, new PlayNComposedDrawableRenderer(assetHandler, graphicRendererFactory));
			factoryMap.put(RuntimeDisplacedDrawable.class, new PlayNDisplacedDrawableRenderer(assetHandler, graphicRendererFactory));
			factoryMap.put(PlayNEngineSpriteImage.class, new PlayNSpriteImageRenderer());
			factoryMap.put(ActorReferenceGO.class, new BasicSceneElementRenderer(graphicRendererFactory));
			factoryMap.put(BasicSceneElementGO.class, new BasicSceneElementRenderer(graphicRendererFactory));
			factoryMap.put(ComplexSceneElementGO.class, new BasicSceneElementRenderer(graphicRendererFactory));
			factoryMap.put(BasicHUD.class, new BasicHudGORenderer(graphicRendererFactory, mouseState, gameObjectFactory, assetHandler));
			factoryMap.put(BasicHUDImpl.class, new BasicHudGORenderer(graphicRendererFactory, mouseState, gameObjectFactory, assetHandler));
			factoryMap.put(MenuHUD.class, new MenuHUDGORenderer());
			factoryMap.put(MenuHUDImpl.class, new MenuHUDGORenderer());
			factoryMap.put(PlayNMenuHUDImpl.class, new MenuHUDGORenderer());
			factoryMap.put(ActionsHUD.class, new ActionsHudGORenderer(platformConfiguration));
			factoryMap.put(PlayNActionsHUDImpl.class, new ActionsHudGORenderer(platformConfiguration));
			factoryMap.put(EffectHUD.class, new EffectHudGORenderer());
			factoryMap.put(EffectHUDImpl.class, new EffectHudGORenderer());
				
			factoryMap.put(ComplexBlockingEffectGO.class, new EffectsGORenderer());
			factoryMap.put(SceneGO.class, new SceneGORenderer());
			factoryMap.put(SceneGOImpl.class, new SceneGORenderer());
			factoryMap.put(TransitionGO.class, new TransitionGORenderer(graphicRendererFactory));
			factoryMap.put(BasicInventoryGO.class, new BasicInventoryGORenderer());
			factoryMap.put(PlayNBasicInventoryGO.class, new BasicInventoryGORenderer());
			factoryMap.put(VideoSceneGO.class, new VideoSceneGORenderer());
				
			//FIXME these shouldn't be necessary if Abstract factory supports "getInterfaces" method, removed for GWT compatibility
			factoryMap.put(ComposedSceneGOImpl.class, new SceneGORenderer());
			factoryMap.put(SceneElementGO.class, new BasicSceneElementRenderer(graphicRendererFactory));
			factoryMap.put(ActorReferenceGOImpl.class, new BasicSceneElementRenderer(graphicRendererFactory));

		}
		return factoryMap;
	}


}
