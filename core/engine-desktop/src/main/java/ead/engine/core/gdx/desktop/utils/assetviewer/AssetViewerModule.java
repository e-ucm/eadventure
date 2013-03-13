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

package ead.engine.core.gdx.desktop.utils.assetviewer;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.EAdOperation;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.fonts.FontHandler;
import ead.engine.core.assets.fonts.FontHandlerImpl;
import ead.engine.core.factories.EffectGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameStateImpl;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.debuggers.DebuggersHandler;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.gdx.desktop.platform.assets.GdxDesktopAssetHandler;
import ead.engine.core.tracking.GameTracker;
import ead.tools.BasicSceneGraph;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;
import ead.tools.StringHandler;
import ead.tools.StringHandlerImpl;
import ead.tools.java.JavaInjector;
import ead.tools.java.reflection.JavaReflectionProvider;
import ead.tools.reflection.ReflectionProvider;

public class AssetViewerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FontHandler.class).to(FontHandlerImpl.class).in(Singleton.class);
		bind(AssetHandler.class).to(GdxDesktopAssetHandler.class).in(
				Singleton.class);
		bind(StringHandler.class).to(StringHandlerImpl.class).in(
				Singleton.class);
		bind(GUI.class).to(AssetViewerGUI.class).in(Singleton.class);
		bind(GameState.class).to(AssetVariableMap.class).in(Singleton.class);
		bind(GenericInjector.class).to(JavaInjector.class).in(Singleton.class);
		bind(SceneGraph.class).to(BasicSceneGraph.class).in(Singleton.class);
		bind(ReflectionProvider.class).to(JavaReflectionProvider.class).in(
				Singleton.class);

		bind(AssetViewer.class);
	}

	public static class AssetViewerGUI implements GUI {

		@Override
		public void finish() {
			// TODO Auto-generated method stub

		}

		@Override
		public int getSkippedMilliseconds() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getTicksPerSecond() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void addHud(SceneElementGO hud) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeHUD(SceneElementGO hud) {
			// TODO Auto-generated method stub

		}

		@Override
		public void showSpecialResource(Object object, int x, int y,
				boolean fullscreen) {
			// TODO Auto-generated method stub

		}

		@Override
		public SceneElementGO getGameObjectIn(int x, int y) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void initialize(Game game, GameState gameState,
				SceneElementGOFactory sceneElementFactory,
				DebuggersHandler debuggerHandler) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setScene(SceneGO scene) {
			// TODO Auto-generated method stub

		}

		@Override
		public SceneGO getScene() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EAdScene getPreviousScene() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SceneElementGO getSceneElement(EAdSceneElement element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SceneElementGO getHUD(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SceneElementGO getRoot() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SceneElementGO getGameObjectUnderPointer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addDragSource(SceneElementGO dragSource) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addDragTarget(SceneElementGO dragTarget) {
			// TODO Auto-generated method stub

		}

		@Override
		public void resetDrag() {
			// TODO Auto-generated method stub

		}

	}

	public static class AssetVariableMap extends GameStateImpl {

		public AssetVariableMap(StringHandler stringHandler,
				SceneElementGOFactory sceneElementFactory,
				EffectGOFactory effectFactory,
				ReflectionProvider reflectionProvider, GameTracker tracker) {
			super(stringHandler, sceneElementFactory, effectFactory,
					reflectionProvider, tracker);
		}

		@Override
		public String processTextVars(String text,
				EAdList<EAdOperation> operations) {
			return text;
		}

	}

}