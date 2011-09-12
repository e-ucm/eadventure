package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
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
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
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

public class PlayNAssetRendererMapProvider  implements MapProvider<Class<?>, GraphicRenderer<?, ?>>{

	private Map<Class<?>, GraphicRenderer<?, ?>> factoryMap;

	@Inject
	public PlayNAssetRendererMapProvider(Injector injector) {
		factoryMap = new HashMap<Class<?>, GraphicRenderer<?, ?>>();		
			
		factoryMap.put(PlayNEngineImage.class, injector.getInstance(PlayNImageAssetRenderer.class));
		factoryMap.put(PlayNEngineCaption.class, injector.getInstance(PlayNCaptionRenderer.class));
		factoryMap.put(PlayNBezierShape.class, injector.getInstance(PlayNBezierShapeRenderer.class));
		factoryMap.put(RuntimeComposedDrawable.class, injector.getInstance(PlayNComposedDrawableRenderer.class));
		factoryMap.put(RuntimeDisplacedDrawable.class, injector.getInstance(PlayNDisplacedDrawableRenderer.class));
		factoryMap.put(PlayNEngineSpriteImage.class, injector.getInstance(PlayNSpriteImageRenderer.class));
		factoryMap.put(ActorReferenceGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(BasicSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(ComplexSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(BasicHUD.class, injector.getInstance(BasicHudGORenderer.class));
		factoryMap.put(BasicHUDImpl.class, injector.getInstance(BasicHudGORenderer.class));
		factoryMap.put(MenuHUD.class, injector.getInstance(MenuHUDGORenderer.class));
		factoryMap.put(MenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
		factoryMap.put(PlayNMenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
		factoryMap.put(ActionsHUD.class, injector.getInstance(ActionsHudGORenderer.class));
		factoryMap.put(EffectHUD.class, injector.getInstance(EffectHudGORenderer.class));
		factoryMap.put(EffectHUDImpl.class, injector.getInstance(EffectHudGORenderer.class));
		factoryMap.put(PlayNActionsHUDImpl.class, injector.getInstance(ActionsHudGORenderer.class));
			
		factoryMap.put(ComplexBlockingEffectGO.class, injector.getInstance(EffectsGORenderer.class));
		factoryMap.put(SceneGO.class, injector.getInstance(SceneGORenderer.class));
		factoryMap.put(SceneGOImpl.class, injector.getInstance(SceneGORenderer.class));
		factoryMap.put(TransitionGO.class, injector.getInstance(TransitionGORenderer.class));
		factoryMap.put(BasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
		factoryMap.put(PlayNBasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
		factoryMap.put(VideoSceneGO.class, injector.getInstance(VideoSceneGORenderer.class));
			
		//FIXME these shouldn't be necessary if Abstract factory supports "getInterfaces" method, removed for GWT compatibility
		factoryMap.put(ComposedSceneGOImpl.class, injector.getInstance(SceneGORenderer.class));
		factoryMap.put(SceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(ActorReferenceGOImpl.class, injector.getInstance(BasicSceneElementRenderer.class));
	}

	
	public void addToGameObjectFactory(
			Class<?> key_value,
			GameObjectRenderer<?, ?> value_value) {
		factoryMap.put(key_value, value_value);
	}

	@Override
	public Map<Class<?>, GraphicRenderer<?, ?>> getMap() {
		return factoryMap;
	}


}
