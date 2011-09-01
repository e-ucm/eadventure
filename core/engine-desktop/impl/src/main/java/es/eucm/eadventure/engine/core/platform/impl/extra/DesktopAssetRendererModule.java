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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.ActionsHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.EffectHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.MenuHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.EmptyTransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.transitions.DisplaceTransitionGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopBezierShape;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineCaption;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.DesktopEngineSpriteImage;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeComposedDrawable;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeDisplacedDrawable;
import es.eucm.eadventure.engine.core.platform.impl.DesktopGraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopBezierShapeRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopCaptionRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopComposedDrawableRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopDisplacedDrawableRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopImageAssetRenderer;
import es.eucm.eadventure.engine.core.platform.impl.assetrenderers.DesktopSpriteImageRenderer;
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
import es.eucm.eadventure.engine.core.platform.impl.specialassetrenderers.VLCDesktopVideoRenderer;

@Singleton
public class DesktopAssetRendererModule extends AbstractModule implements MapProvider<Class<?>, GraphicRenderer<?, ?>> {

	private Map<Class<?>, GraphicRenderer<?, ?>> factoryMap;
	
	@Inject
	public DesktopAssetRendererModule(Injector injector) {
		if (injector != null) {
			factoryMap = new HashMap<Class<?>, GraphicRenderer<?, ?>>();		
			
			factoryMap.put(DesktopEngineImage.class, injector.getInstance(DesktopImageAssetRenderer.class));
			factoryMap.put(DesktopEngineCaption.class, injector.getInstance(DesktopCaptionRenderer.class));
			factoryMap.put(DesktopBezierShape.class, injector.getInstance(DesktopBezierShapeRenderer.class));
			factoryMap.put(RuntimeComposedDrawable.class, injector.getInstance(DesktopComposedDrawableRenderer.class));
			factoryMap.put(RuntimeDisplacedDrawable.class, injector.getInstance(DesktopDisplacedDrawableRenderer.class));
			factoryMap.put(DesktopEngineSpriteImage.class, injector.getInstance(DesktopSpriteImageRenderer.class));
			factoryMap.put(ActorReferenceGO.class, injector.getInstance(BasicSceneElementRenderer.class));
			factoryMap.put(BasicSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
			factoryMap.put(ComplexSceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
			factoryMap.put(BasicHUD.class, injector.getInstance(BasicHudGORenderer.class));
			factoryMap.put(BasicHUDImpl.class, injector.getInstance(BasicHudGORenderer.class));
			factoryMap.put(MenuHUD.class, injector.getInstance(MenuHUDGORenderer.class));
			factoryMap.put(MenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
			factoryMap.put(DesktopMenuHUDImpl.class, injector.getInstance(MenuHUDGORenderer.class));
			factoryMap.put(ActionsHUD.class, injector.getInstance(ActionsHudGORenderer.class));
			factoryMap.put(EffectHUD.class, injector.getInstance(EffectHudGORenderer.class));
			factoryMap.put(DesktopActionsHUDImpl.class, injector.getInstance(ActionsHudGORenderer.class));
			
			factoryMap.put(ComplexBlockingEffectGO.class, injector.getInstance(EffectsGORenderer.class));
			factoryMap.put(SceneGO.class, injector.getInstance(SceneGORenderer.class));
			factoryMap.put(SceneGOImpl.class, injector.getInstance(SceneGORenderer.class));
			factoryMap.put(TransitionGO.class, injector.getInstance(TransitionGORenderer.class));
			factoryMap.put(BasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
			factoryMap.put(DesktopBasicInventoryGO.class, injector.getInstance(BasicInventoryGORenderer.class));
			factoryMap.put(VideoSceneGO.class, injector.getInstance(VideoSceneGORenderer.class));
			
			//FIXME these shouldn't be necessary if Abstract factory supports "getInterfaces" method, removed for GWT compatibility
			factoryMap.put(EmptyTransitionGO.class, injector.getInstance(TransitionGORenderer.class));
			factoryMap.put(DisplaceTransitionGO.class, injector.getInstance(TransitionGORenderer.class));
			factoryMap.put(ComposedSceneGOImpl.class, injector.getInstance(SceneGORenderer.class));
			factoryMap.put(SceneElementGO.class, injector.getInstance(BasicSceneElementRenderer.class));
			factoryMap.put(ActorReferenceGOImpl.class, injector.getInstance(BasicSceneElementRenderer.class));
			
		}
	}
	
	@Override
	protected void configure() {
		bind(new TypeLiteral<GraphicRendererFactory<?>> () {}).to(DesktopGraphicRendererFactory.class);
		bind(new TypeLiteral<MapProvider<Class<?>, GraphicRenderer<?, ?>>>() {}).to(DesktopAssetRendererModule.class);
		bind(new TypeLiteral<SpecialAssetRenderer<Video, ?>>() {}).to(VLCDesktopVideoRenderer.class);
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
