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

package es.eucm.ead.editor.control;

import java.io.File;
import java.util.Collection;

import javax.swing.Action;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.engine.desktop.platform.DesktopGUI;
import es.eucm.ead.engine.desktop.utils.assetviewer.AssetViewer;
import java.util.LinkedHashMap;

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
	private ScriptController scriptController;

	private DesktopGame game;

	private AssetHandler assetHandler;
	private final Provider<AssetViewer> assetViewerProvider;

	/**
	 * Action map. Contains all actions, generally bound to menu items or
	 * the like.
	 */
	private LinkedHashMap<String, Action> actionMap = new LinkedHashMap<String, Action>();

	@Inject
	public ControllerImpl(EditorConfig editorConfig, EditorModel editorModel,
			ProjectController projectController,
			NavigationController navigationController,
			ViewController viewControler, CommandManager commandManager,
			ScriptController scriptController, DesktopGUI gdxGui,
			AssetHandler assetHandler, Provider<AssetViewer> assetViewerProvider) {

		this.editorConfig = editorConfig;
		this.editorModel = editorModel;
		this.projectController = projectController;
		this.navigationController = navigationController;
		this.viewController = viewControler;
		this.commandManager = commandManager;
		this.scriptController = scriptController;

		this.assetHandler = assetHandler;
		this.assetViewerProvider = assetViewerProvider;
	}

	@Override
	public void initialize() {
		projectController.setController(this);
		navigationController.setController(this);
		viewController.setController(this);
		commandManager.setController(this);
		scriptController.setController(this);
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

	/**
	 * Provides AssetViewers on request
	 */
	@Override
	public AssetViewer createAssetViewer() {
		AssetViewer viewer = assetViewerProvider.get();
		getAssetHandler();
		return viewer;
	}

	@Override
	public AssetHandler getAssetHandler() {
		assetHandler.setResourcesLocation(editorModel.getLoader().getSaveDir()
				.getAbsolutePath());
		assetHandler.setCacheEnabled(false);
		return assetHandler;
	}

	@Override
	public ScriptController getScriptController() {
		return scriptController;
	}
}
