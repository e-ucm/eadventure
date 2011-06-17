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

package es.eucm.eadventure.engine.core.platform;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.BundledDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.BundledDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.common.resources.impl.DefaultStringHandler;
import es.eucm.eadventure.engine.core.KeyboardState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.BasicHUDImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.GameObjectManagerImpl;
import es.eucm.eadventure.engine.core.impl.KeyboardStateImpl;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeBundledAnimation;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeFramesAnimation;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;

public class TestModule extends AbstractModule {
	
	@Override
	protected void configure() {
		configureAssetRenderer();
		configureAssetHandler();
		bind(Boolean.class).annotatedWith(Names.named("threaded")).toInstance(Boolean.FALSE);
		bind(GUI.class).to(TestGUI.class);
		bind(PlatformConfiguration.class).to(TestPlatformConfiguration.class);
		bind(PlatformControl.class).to(TestPlatformControl.class);
		bind(PlatformLauncher.class).to(TestPlatformLauncher.class);
		bind(MouseState.class).to(MouseStateTest.class);
		bind(KeyboardState.class).to(KeyboardStateImpl.class);
		bind(GameObjectManager.class).to(GameObjectManagerImpl.class);
		bind(BasicHUD.class).to(BasicHUDImpl.class);
		bind(FontCacheImpl.class).to(TestFontCache.class);
	}
	
	private void configureAssetHandler() {
		bind(StringHandler.class).to(DefaultStringHandler.class);
		bind(AssetHandler.class).to(TestAssetHandler.class);
		
		Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> map = new HashMap<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>( );

		bind( new TypeLiteral<Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>>>( ) {} ).toInstance( map );

		map.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		map.put(BundledDrawableImpl.class, RuntimeBundledAnimation.class);
		map.put(BundledDrawable.class, RuntimeBundledAnimation.class);
	}

	protected void configureAssetRenderer() {
		bind(new TypeLiteral<GraphicRendererFactory<?>> () {}).to(TestGraphicRenderer.class);
	}


}