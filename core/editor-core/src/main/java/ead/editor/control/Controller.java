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

import ead.editor.model.EditorModel;
import java.util.Locale;


/**
 * Game editor controller. The main controller provides access to delegate
 * controllers in charge of project-wide operations, view and command management
 */
public interface Controller {

    /**
     * Retrieves the editor-wide configuration
     */
    EditorConfig getConfig();

	/**
	 * Retrieve the model for read-only purposes.
	 * ALERT: do not keep copies of the model, or do anything except READING it
	 */
	EditorModel getModel();

    /**
     * Returns the project controller. In charge of project loading / saving
     */
    ProjectController getProjectController();

    /**
     * Returns the navigation controller. In charge of forward / backward
     * navigation in views.
     */
    NavigationController getNavigationController();

    /**
     * Returns the view controller. In charge of creating, hiding and
     * displaying views.
     */
    ViewController getViewController();

    /**
     * Returns the command controller. In charge of model-changing command
     * execution, including undo / redo. All commands should be executed
     * through the manager.
     */
    CommandManager getCommandManager();
}
