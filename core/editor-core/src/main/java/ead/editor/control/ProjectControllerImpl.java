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

import com.google.inject.Singleton;
import ead.editor.control.change.ChangeListener;
import ead.utils.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.editor.model.EditorModel;
import ead.engine.core.game.GameLoader;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.tools.GenericInjector;
import ead.tools.java.JavaToolsModule;

/**
 * Default implementation for the {@link ProjectController}.
 */
@Singleton
public class ProjectControllerImpl implements ProjectController {

    private static final Logger logger = LoggerFactory.getLogger("FileMenu");

	private Controller controller;
    private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

//	private GameLoader desktopLoader;
//	
//	@Inject
//	public ProjectControllerImpl(GameLoader desktopLoader) {
//		this.desktopLoader = desktopLoader;
//	}
	
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void load(String projectURL) {
		controller.getCommandManager().clearCommands();
		try {
			controller.getModel().load(new File(projectURL));
            notifyListeners("Loaded ok");
			controller.getViewController().clearViews();
			controller.getViewController().restoreViews();
		} catch (IOException ex) {
			logger.warn("Error loading {}", projectURL, ex);
		    SwingUtilities.showExceptionDialog(ex);
		}
	}

	@Override
	public void doImport(String sourceURL, String projectURL) {
		controller.getCommandManager().clearCommands();
		try {
			controller.getModel().loadFromImportFile(
                    new File(sourceURL), new File(projectURL));
            notifyListeners("Imported ok");
			controller.getViewController().clearViews();
			controller.getViewController().restoreViews();
		} catch (IOException ex) {
			logger.warn("Error importing from {} to {}",
                    new Object[]{sourceURL, projectURL}, ex);
		    SwingUtilities.showExceptionDialog(ex);
		}
	}

    @Override
	public void save() {
		try {
			controller.getViewController().saveViews();
			controller.getModel().save(null);
		} catch (IOException ex) {
			logger.warn("Error saving to previous dir.", ex);
		    SwingUtilities.showExceptionDialog(ex);
		}
	}

	@Override
	public void saveAs(String projectURL) {
		try {
			controller.getViewController().saveViews();
			controller.getModel().save(new File(projectURL));
		} catch (IOException ex) {
			logger.warn("Error saving {}", projectURL, ex);
		    SwingUtilities.showExceptionDialog(ex);
		}
	}

	@Override
	public void newProject() {
		// FIXME - Not yet implemented
		if (true) {
            throw new IllegalArgumentException("Not yet implemented!");
        }
        notifyListeners("Created ok");
		controller.getViewController().clearViews();
	}

	/**
	 * Launches a game
	 */
	@Override
	public void doRun() {
		Injector i = Guice.createInjector(new GdxDesktopModule(), new JavaToolsModule());
		GenericInjector injector = i.getInstance(GenericInjector.class);
		GameLoader g = injector.getInstance(GameLoader.class);
		EditorModel em = controller.getModel();
		g.loadGame(em.getEngineModel(), 
				em.getStringHandler().getStrings(), 
				em.getEngineProperties());
	}	
	
    @Override
    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        listeners.remove(changeListener);
    }

    @Override
    public void notifyListeners(Object event) {
        for (ChangeListener l : listeners) {
            l.processChange(event);
        }
    }
}
