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

package ead.editor.control;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.editor.model.EditorModel;
import java.io.File;
import java.util.HashMap;
import javax.swing.Action;

import ead.engine.core.game.GameLoader;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.engine.core.gdx.desktop.platform.GdxDesktopGUI;
import java.util.Collection;

import com.google.inject.Provider;
import ead.common.util.EAdURI;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import ead.engine.core.platform.assets.AssetHandler;

/**
 * Default implementation for the {@link Controller}.
 */
@Singleton
public class ControllerImpl implements Controller {

	private boolean configLoaded = false;

	private EditorConfig editorConfig;
	private EditorModel editorModel;
	private ProjectController projectController;
	private NavigationController navigationController;
	private ViewController viewController;
	private CommandManager commandManager;
	private AssetHandler assetHandler;
	private GdxDesktopGUI gui;

	private final Provider<AssetViewer> assetViewerProvider;
	private final Provider<GameLoader> gameLoaderProvider;

	/**
	 * Action map. Contains all actions, generally bound to menu items or
	 * the like.
	 */
	private HashMap<String, Action> actionMap = new HashMap<String, Action>();

	@Inject
	public ControllerImpl(EditorConfig editorConfig, EditorModel editorModel,
			ProjectController projectController,
			NavigationController navigationController,
			ViewController viewControler, CommandManager commandManager,
			GdxDesktopGUI gdxGui, AssetHandler assetHandler,
			Provider<AssetViewer> assetViewerProvider,
			Provider<GameLoader> gameLoaderProvider, GdxDesktopGUI gui) {

		this.editorConfig = editorConfig;
		this.editorModel = editorModel;
		this.projectController = projectController;
		this.navigationController = navigationController;
		this.viewController = viewControler;
		this.commandManager = commandManager;
		this.assetHandler = assetHandler;
		this.assetViewerProvider = assetViewerProvider;
		this.gameLoaderProvider = gameLoaderProvider;
		this.gui = gui;

		assetHandler.setCacheEnabled(true);
	}

	@Override
	public void initialize() {
		projectController.setController(this);
		navigationController.setController(this);
		viewController.setController(this);
	}

	@Override
	public EditorConfig getConfig() {
		if (!configLoaded) {
			File f = new File("ead-editor-config.xml");
			if (editorConfig.load(f.getAbsolutePath())) {
				configLoaded = true;
			} else if (editorConfig.save(f.getAbsolutePath())) {
				configLoaded = true;
			}
		}
		return editorConfig;
	}

	/**
	 * Access to the editor model. IMPORTANT: all non-control classes should
	 * consider the returned model to be read-only and transient. Violators
	 * WILL be punished.
	 * @return
	 */
	@Override
	public EditorModel getModel() {
		return editorModel;
	}

	@Override
	public ProjectController getProjectController() {
		return projectController;
	}

	@Override
	public NavigationController getNavigationController() {
		return navigationController;
	}

	@Override
	public ViewController getViewController() {
		return viewController;
	}

	@Override
	public CommandManager getCommandManager() {
		return commandManager;
	}

	@Override
	public Collection<Action> getActions() {
		return actionMap.values();
	}

	@Override
	public Action getAction(String name) {
		return actionMap.get(name);
	}

	@Override
	public void putAction(String name, Action action) {
		actionMap.put(name, action);
	}

	@Override
	public AssetHandler getAssetHandler() {
		return assetHandler;
	}

	/**
	 * Provides GameLoaders on request
	 */
	@Override
	public GameLoader createGameLoader() {
		gui.finish();
		assetHandler.setResourcesLocation(new EAdURI(editorModel
				.getLoader().getSaveDir().getPath()));
		return gameLoaderProvider.get();
	}

	/**
	 * Provides AssetViewers on request
	 */
	@Override
	public AssetViewer createAssetViewer() {
		assetHandler.setResourcesLocation(new EAdURI(editorModel
				.getLoader().getSaveDir().getPath()));
		return assetViewerProvider.get();
	}
}
