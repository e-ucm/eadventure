package es.eucm.eadventure.engine.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.engine.AndroidGraphicRendererFactory;
import es.eucm.eadventure.engine.assetrenderers.AndroidCaptionRenderer;
import es.eucm.eadventure.engine.assetrenderers.AndroidComposedDrawableRenderer;
import es.eucm.eadventure.engine.assetrenderers.AndroidDisplacedDrawableRenderer;
import es.eucm.eadventure.engine.assetrenderers.AndroidImageAssetRenderer;
import es.eucm.eadventure.engine.assets.AndroidEngineCaption;
import es.eucm.eadventure.engine.assets.AndroidEngineImage;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.gameobjectrenderers.ActionsHudGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.BasicHudGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.BasicInventoryGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.BasicSceneElementRenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.EffectHudGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.EffectsGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.SceneGORenderer;
import es.eucm.eadventure.engine.gameobjectrenderers.TransitionGORenderer;

@Singleton
public class AndroidAssetRendererModule extends AbstractModule implements MapProvider<Class<?>, GraphicRenderer<?, ?>> {

	private Map<Class<?>, GraphicRenderer<?, ?>> factoryMap;
	
	@Inject
	public AndroidAssetRendererModule(Injector injector) {
		if (injector != null) {
		factoryMap = new HashMap<Class<?>, GraphicRenderer<?, ?>>();		
		
		factoryMap.put(AndroidEngineImage.class, injector.getInstance(AndroidImageAssetRenderer.class));
		factoryMap.put(AndroidEngineCaption.class, injector.getInstance(AndroidCaptionRenderer.class));
		factoryMap.put(RuntimeComposedDrawable.class, injector.getInstance(AndroidComposedDrawableRenderer.class));
		factoryMap.put(RuntimeDisplacedDrawable.class, injector.getInstance(AndroidDisplacedDrawableRenderer.class));
		factoryMap.put(ActorReferenceGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(BasicSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(ComplexSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
		factoryMap.put(BasicHUD.class, injector.getInstance(BasicHudGORenderer.class));
		factoryMap.put(BasicHUDImpl.class, injector.getInstance(BasicHudGORenderer.class));
		//factoryMap.put(MenuHUD.class, injector.getInstance(MenuHUDGORenderer.class));
		//factoryMap.put(MenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
		//factoryMap.put(DesktopMenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
		factoryMap.put(ActionsHUD.class, injector.getInstance(ActionsHudGORenderer.class));
		factoryMap.put(EffectHUD.class, injector.getInstance(EffectHudGORenderer.class));
		factoryMap.put(AndroidActionsHUDImpl.class, injector.getInstance(ActionsHudGORenderer.class));
		
		factoryMap.put(ComplexBlockingEffectGO.class, injector.getInstance(EffectsGORenderer.class));
		factoryMap.put(SceneGO.class, injector.getInstance(SceneGORenderer.class));
		factoryMap.put(SceneGOImpl.class, injector.getInstance(SceneGORenderer.class));
		factoryMap.put(TransitionGO.class, injector.getInstance(TransitionGORenderer.class));
		factoryMap.put(BasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
		//factoryMap.put(DesktopBasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
		//factoryMap.put(VideoSceneGO.class, injector.getInstance(VideoSceneGORenderer.class));
		}
	}
	
	@Override
	protected void configure() {
		bind(new TypeLiteral<GraphicRendererFactory<?>> () {}).to(AndroidGraphicRendererFactory.class);
		bind(new TypeLiteral<MapProvider<Class<?>, GraphicRenderer<?, ?>>>() {}).to(AndroidAssetRendererModule.class);
		//bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>() {}).to(AndroidVideoRenderer.class);
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
