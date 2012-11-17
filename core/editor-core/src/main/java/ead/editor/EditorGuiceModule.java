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

package ead.editor;

import com.google.inject.AbstractModule;
import ead.editor.control.CommandManager;
import ead.editor.control.CommandManagerImpl;
import ead.editor.control.Controller;
import ead.editor.control.ControllerImpl;
import ead.editor.control.EditorConfig;
import ead.editor.control.EditorConfigImpl;
import ead.editor.control.NavigationController;
import ead.editor.control.NavigationControllerImpl;
import ead.editor.control.ProjectController;
import ead.editor.control.ProjectControllerImpl;
import ead.editor.control.ViewController;
import ead.editor.model.EditorAnnotator;
import ead.editor.model.EditorModel;
import ead.editor.model.EditorModelImpl;
import ead.editor.view.EditorWindow;
import ead.importer.annotation.ImportAnnotator;

/**
 * Google Guice ({@link http://code.google.com/p/google-guice/}) module to
 * configure the eAdventure editor.
 */
public class EditorGuiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(EditorModel.class).to(EditorModelImpl.class);
		
		bind(EditorConfig.class).to(EditorConfigImpl.class);
		bind(Controller.class).to(ControllerImpl.class);
		bind(CommandManager.class).to(CommandManagerImpl.class);
		bind(ProjectController.class).to(ProjectControllerImpl.class);
		bind(NavigationController.class).to(NavigationControllerImpl.class);
		bind(ViewController.class).to(EditorWindow.class);

		bind(Launcher.class).to(EAdventureEditor.class);
        bind(ImportAnnotator.class).to(EditorAnnotator.class);
	}
}
