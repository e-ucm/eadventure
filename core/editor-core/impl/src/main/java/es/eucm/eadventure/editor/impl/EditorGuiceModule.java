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

package es.eucm.eadventure.editor.impl;

import com.google.inject.AbstractModule;

import es.eucm.eadventure.editor.Launcher;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.NavigationController;
import es.eucm.eadventure.editor.control.ProjectController;
import es.eucm.eadventure.editor.control.ViewController;
import es.eucm.eadventure.editor.control.impl.CommandManagerImpl;
import es.eucm.eadventure.editor.control.impl.ControllerImpl;
import es.eucm.eadventure.editor.control.impl.NavigationControllerImpl;
import es.eucm.eadventure.editor.control.impl.ProjectControllerImpl;
import es.eucm.eadventure.editor.control.impl.ViewControllerImpl;
import es.eucm.eadventure.editor.view.EditorWindow;
import es.eucm.eadventure.editor.view.ToolPanel;
import es.eucm.eadventure.editor.view.impl.EditorWindowImpl;
import es.eucm.eadventure.editor.view.impl.ToolPanelImpl;
import es.eucm.eadventure.editor.view.menu.EditMenu;
import es.eucm.eadventure.editor.view.menu.EditorMenuBar;
import es.eucm.eadventure.editor.view.menu.FileMenu;
import es.eucm.eadventure.editor.view.menu.impl.EditMenuImpl;
import es.eucm.eadventure.editor.view.menu.impl.EditorMenuBarImpl;
import es.eucm.eadventure.editor.view.menu.impl.FileMenuImpl;

/**
 * Google Guice ({@link http://code.google.com/p/google-guice/}) module to
 * configure the eAdventure editor.
 */
public class EditorGuiceModule extends AbstractModule {

	@Override
	protected void configure() {

		configureMainViewElements();
		
		configureController();

		configureMenu();
		
		bind(Launcher.class).to(EAdventureEditor.class);

	}
	
	/**
	 * Configure the main elements of the editor view
	 */
	private void configureMainViewElements() {
		bind(EditorWindow.class).to(EditorWindowImpl.class);
		bind(ToolPanel.class).to(ToolPanelImpl.class);
	}
	
	/**
	 * Configure controller for the application, the project and the navigation
	 */
	private void configureController() {
		bind(Controller.class).to(ControllerImpl.class);
		bind(CommandManager.class).to(CommandManagerImpl.class);
		bind(ProjectController.class).to(ProjectControllerImpl.class);
		bind(NavigationController.class).to(NavigationControllerImpl.class);
		bind(ViewController.class).to(ViewControllerImpl.class);
	}

	/**
	 * Configure the menu of the application
	 */
	private void configureMenu() {
		bind(EditorMenuBar.class).to(EditorMenuBarImpl.class);
		bind(FileMenu.class).to(FileMenuImpl.class);
		bind(EditMenu.class).to(EditMenuImpl.class);
	}
	
}
