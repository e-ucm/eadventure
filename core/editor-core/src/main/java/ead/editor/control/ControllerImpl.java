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
import java.util.Locale;

/**
 * Default implementation for the {@link Controller}.
 */
@Singleton
public class ControllerImpl implements Controller {

	private EditorModel editorModel = null;
    private ProjectController projectController;
    private NavigationController navigationController;
    private ViewController viewController;
    private CommandManager commandManager;

	@Inject
	public ControllerImpl(EditorModel editorModel, 
		ProjectController projectController, 
		NavigationController navigationController,
		ViewController viewControler,
		CommandManager commandManager)	{
		this.editorModel = editorModel;
		this.projectController = projectController;
		this.navigationController = navigationController;
		this.viewController = viewControler;
		this.commandManager = commandManager;

		this.projectController.setController(this);
		this.navigationController.setController(this);
		this.viewController.setController(this);
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

	/**
	 * Changes current locale. But this will not alter already-loaded strings...
	 * @param locale
	 */
	@Override
	public void setLocale(Locale locale) {
		Locale.setDefault(locale);

		// FIXME: should redo static initialization of all Messages classes

		// FIXME: should reload all the UI right around here, via massive repaints
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
}
