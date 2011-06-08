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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.editor.Launcher;
import es.eucm.eadventure.editor.control.ViewController;
import es.eucm.eadventure.editor.view.SplashScreen;
import es.eucm.eadventure.editor.view.impl.SplashScreenImpl;

/**
 * eAdventure editor launcher.
 * This class has a main method.
 */
public class EAdventureEditor implements Launcher {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(EAdventureEditor.class);
	
	/**
	 * Controller for the view
	 */
	private ViewController viewController;
	
	public static void main(String[] args) {
		// The following line is used by MacOS X to set the application name correctly
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "eAdventure");
		
		SplashScreen splashScreen = new SplashScreenImpl();
		splashScreen.show();
		
		Injector injector = Guice.createInjector(new EditorGuiceModule());

		Launcher launcher = injector.getInstance(Launcher.class);

		
		launcher.configure();
		launcher.initialize();
		splashScreen.hide();
		launcher.start();
	}
	
	@Inject
	public EAdventureEditor(ViewController viewController) {
		this.viewController = viewController;
	}
	
	@Override
	public void configure() {
		logger.info("Configuring...");
	}
	
	@Override
	public void initialize() {
		logger.info("Initializing...");
		viewController.initialize();
	}
	
	@Override
	public void start() {
		logger.info("Starting...");
		viewController.showWindow();
	}
	
}
