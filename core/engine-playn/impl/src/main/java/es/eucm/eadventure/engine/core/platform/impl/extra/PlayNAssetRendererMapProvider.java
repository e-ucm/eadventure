package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

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

public class PlayNAssetRendererMapProvider implements MapProvider<Class<?>, GraphicRenderer<?, ?>>{

	private Map<Class<?>, GraphicRenderer<?, ?>> factoryMap;

	@Inject
	public PlayNAssetRendererMapProvider(PlayNImageAssetRenderer playNImageAssetRenderer,
			PlayNCaptionRenderer playNCaptionRenderer,
			PlayNBezierShapeRenderer playNBezierShapeRenderer,
			PlayNComposedDrawableRenderer playNComposedDrawableRenderer,
			PlayNDisplacedDrawableRenderer playNDisplacedDrawableRenderer,
			PlayNSpriteImageRenderer playNSpriteImageRenderer,
			BasicSceneElementRenderer basicSceneElementRenderer,
			BasicHudGORenderer basicHudGORenderer,
			MenuHUDGORenderer menuHUDGORenderer,
			ActionsHudGORenderer actionsHudGORenderer,
			EffectHudGORenderer effectHudGORenderer,
			EffectsGORenderer effectsGORenderer,
			SceneGORenderer sceneGORenderer,
			TransitionGORenderer transitionGORenderer,
			BasicInventoryGORenderer basicInventoryGORenderer,
			VideoSceneGORenderer videoSceneGORenderer) {
		factoryMap = new HashMap<Class<?>, GraphicRenderer<?, ?>>();		
			
		factoryMap.put(PlayNEngineImage.class, playNImageAssetRenderer);
		factoryMap.put(PlayNEngineCaption.class, playNCaptionRenderer);
		factoryMap.put(PlayNBezierShape.class, playNBezierShapeRenderer);
		factoryMap.put(RuntimeComposedDrawable.class, playNComposedDrawableRenderer);
		factoryMap.put(RuntimeDisplacedDrawable.class, playNDisplacedDrawableRenderer);
		factoryMap.put(PlayNEngineSpriteImage.class, playNSpriteImageRenderer);
		factoryMap.put(ActorReferenceGO.class, basicSceneElementRenderer);
		factoryMap.put(BasicSceneElementGO.class, basicSceneElementRenderer);
		factoryMap.put(ComplexSceneElementGO.class, basicSceneElementRenderer);
		factoryMap.put(BasicHUD.class, basicHudGORenderer);
		factoryMap.put(BasicHUDImpl.class, basicHudGORenderer);
		factoryMap.put(MenuHUD.class, menuHUDGORenderer);
		factoryMap.put(MenuHUDImpl.class,menuHUDGORenderer);
		factoryMap.put(PlayNMenuHUDImpl.class, menuHUDGORenderer);
		factoryMap.put(ActionsHUD.class, actionsHudGORenderer);
		factoryMap.put(PlayNActionsHUDImpl.class, actionsHudGORenderer);
		factoryMap.put(EffectHUD.class, effectHudGORenderer);
		factoryMap.put(EffectHUDImpl.class, effectHudGORenderer);
			
		factoryMap.put(ComplexBlockingEffectGO.class, effectsGORenderer);
		factoryMap.put(SceneGO.class, sceneGORenderer);
		factoryMap.put(SceneGOImpl.class, sceneGORenderer);
		factoryMap.put(TransitionGO.class, transitionGORenderer);
		factoryMap.put(BasicInventoryGO.class, basicInventoryGORenderer);
		factoryMap.put(PlayNBasicInventoryGO.class, basicInventoryGORenderer);
		factoryMap.put(VideoSceneGO.class, videoSceneGORenderer);
			
		//FIXME these shouldn't be necessary if Abstract factory supports "getInterfaces" method, removed for GWT compatibility
		factoryMap.put(ComposedSceneGOImpl.class, sceneGORenderer);
		factoryMap.put(SceneElementGO.class, basicSceneElementRenderer);
		factoryMap.put(ActorReferenceGOImpl.class, basicSceneElementRenderer);
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
