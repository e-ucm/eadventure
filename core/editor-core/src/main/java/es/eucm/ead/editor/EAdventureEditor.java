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

package es.eucm.ead.editor;

import es.eucm.ead.editor.view.SplashScreen;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.EditorModelLoader;
import es.eucm.ead.editor.model.visitor.ModelVisitorDriver;
import es.eucm.ead.editor.view.SplashScreenImpl;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.editor.util.Log4jConfig.Slf4jLevel;
import es.eucm.ead.editor.view.generic.AbstractOption;
import es.eucm.ead.editor.view.panel.asset.ImageAssetPanel;
import es.eucm.ead.reader2.AdventureReader;
import es.eucm.ead.reader2.model.readers.ObjectReader;

/**
 * eAdventure editor launcher. This class has a main method.
 *
 * IMPORTANT: to re-generate resources, use
 * java -cp engine/utils/target/utils-2.0.1-SNAPSHOT.jar
 *      ResourceCreator engine/editor-engine ead.editor
 *      etc/LICENSE.txt engine/editor-engine/src/main/java/ead/editor/R.java
 */
public class EAdventureEditor implements Launcher {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger(EAdventureEditor.class);
	/**
	 * UI Controller
	 */
	private Controller controller;

	/**
	 * Main entry point into the editor.
	 * @param args the first argument, if set, is understood to be a game file
	 */
	public static void main(String[] args) {

		// The following line is used by MacOS X to set the application name correctly
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");

		// Locale.setDefault(new Locale("es", "ES"));

		// Initialize logging
		Log4jConfig.configForConsole(Slf4jLevel.Info, new Object[] {

				// AdventureConverter
				ObjectReader.class, Slf4jLevel.Debug, AdventureReader.class,
				Slf4jLevel.Debug,
				// recursion into bits of model when importing
				EditorModelLoader.class, Slf4jLevel.Debug,
				// access to the model
				EditorModel.class, Slf4jLevel.Debug, ModelVisitorDriver.class,
				Slf4jLevel.Debug,

				// image previews
				ImageAssetPanel.class, Slf4jLevel.Debug,
				// image previews
				AbstractOption.class, Slf4jLevel.Debug

		//"EditorModelLoader", Log4jConfig.Slf4jLevel.Debug,

				// "ModelIndex", Slf4jLevel.Debug,
				// "EditorModelLoader", Slf4jLevel.Debug,
				// "ModelVisitorDriver", Slf4jLevel.Debug,

				// "EditorAnnotator", Slf4jLevel.Debug,
				// "EAdventureImporter", Slf4jLevel.Debug,
				// "EWindowImpl", Slf4jLevel.Info,
				// "ActorFactory", Slf4jLevel.Debug,

				//            "QueryNode", Log4jConfig.Slf4jLevel.Debug,
				//			Writer
				//            "DOMWriter", Log4jConfig.Slf4jLevel.Debug,
				//			Reader
				//			  "NodeVisitor", Log4jConfig.Slf4jLevel.Debug,
				//	  		  "ElementNodeVisitor", Log4jConfig.Slf4jLevel.Debug,
				//	  		  "MapNodeVisitor", Log4jConfig.Slf4jLevel.Debug,
				//	  		  "ParamNodeVisitor", Log4jConfig.Slf4jLevel.Debug,
				//            "ObjectFactory", Log4jConfig.Slf4jLevel.Debug
				//			Internacionalizacion (i18n)
				//            "I18N", Log4jConfig.Slf4jLevel.Debug,
				});

		// show splash
		SplashScreen splashScreen = new SplashScreenImpl();
		splashScreen.show();

		// initialize launcher
		Injector injector = Guice.createInjector(new DesktopModule(),
				new EditorGuiceModule(), new JavaToolsModule());

		// init reflection
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));

		// launch
		Launcher launcher = injector.getInstance(Launcher.class);
		launcher.configure();
		launcher.initialize();

		// hide splash & launch app
		splashScreen.hide();
		launcher.start();
	}

	@Inject
	public EAdventureEditor(Controller controller) {
		logger.info("Controller set to {}", controller);
		this.controller = controller;
	}

	@Override
	public void configure() {
		logger.info("Configuring...");
	}

	@Override
	public void initialize() {
		logger.info("Initializing...");
		controller.initialize();
	}

	@Override
	public void start() {
		logger.info("Starting...");
		controller.getViewController().showWindow();
	}
}
