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

package es.eucm.eadventure.engine.core.platform.test;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.impl.importer.EAdventure1XImporter;
import es.eucm.eadventure.common.impl.importer.ImporterConfigurationModule;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.debuggers.impl.EAdMainDebugger;
import es.eucm.eadventure.engine.core.debuggers.impl.TrajectoryDebugger;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.PlatformControl;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopAssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.DesktopPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

/**
 * Tester for import old <e-Adventure> games
 * 
 */
public class ImportTestDesktopPlatformLauncher {

	/**
	 * The platform control implementation
	 */
	private PlatformControl platformControl;

	private AssetHandler assetHandler;

	private static EAdScene screen;

	@Inject
	public ImportTestDesktopPlatformLauncher(PlatformControl platformControl,
			AssetHandler assetHandler) {
		this.platformControl = platformControl;
		this.assetHandler = assetHandler;
	}

	public ImportTestDesktopPlatformLauncher() {

	}

	public void launch(File file) {
		((DesktopAssetHandler) assetHandler).setResourceLocation(file);
		platformControl.start();
	}

	public static void main(String[] args) {
		EAdMainDebugger.addDebugger(TrajectoryDebugger.class);
		// try {
		// UIManager.setLookAndFeel(EAdGUILookAndFeel.getInstance());
		// } catch (UnsupportedLookAndFeelException e) {
		// e.printStackTrace();
		// }

		// Default directory
		File directory = new File("src/test/resources/EAdventure1Project/");

		JFileChooser fileChooser = new JFileChooser(directory);

		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".eap")
						|| f.getName().endsWith(".ead")
						|| f.getName().endsWith(".zip");
			}

			@Override
			public String getDescription() {
				return "eAdventure projects";
			}

		};
		fileChooser.setFileFilter(filter);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			File f = fileChooser.getSelectedFile();

			String folder = f.getParentFile().getAbsolutePath();

			String projectName = f.getName();

			Injector injector = Guice
					.createInjector(new ImporterConfigurationModule(folder
							+ "/" + projectName),
							new DesktopAssetHandlerModule(),
							new DesktopAssetRendererModule(null),
							new DesktopModule(), new BasicGameModule());
			PlatformConfiguration conf = injector.getInstance(PlatformConfiguration.class);
			conf.setWidth(800);
			conf.setHeight(600);
			EAdventure1XImporter importer = injector
					.getInstance(EAdventure1XImporter.class);

			projectName = projectName.substring(0, projectName.length() - 4);
			JDialog dialog = new JDialog();
			dialog.setTitle("eAdventure");
			dialog.setModal(false);
			dialog.setSize(50, 50);
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(null);
			JLabel label = new JLabel();
			label.setText("Importing...");
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(label, BorderLayout.CENTER);
			dialog.setVisible(true);
			EAdAdventureModel model = importer.importGame(folder + "/"
					+ projectName + "Imported");
			dialog.setVisible(false);

			if (model != null) {

				screen = model.getChapters().get(0).getInitialScene();
				injector.getInstance(StringHandler.class).setString(
						new EAdString("Loading"), "loading");
				LoadingScreen loadingScreen = injector
						.getInstance(LoadingScreen.class);
				loadingScreen.setInitialScreen(screen);

				Game game = injector.getInstance(Game.class);
				game.setGame(model, model.getChapters().get(0));

				System.setProperty(
						"com.apple.mrj.application.apple.menu.about.name",
						"eAdventure");

				PlatformLauncher launcher = injector
						.getInstance(PlatformLauncher.class);

				// TODO extract file from args or use default?
				File file = new File(folder, projectName + "Imported");
				// File file = new File("/ProyectoJuegoFINAL.ead");
				((DesktopPlatformLauncher) launcher).launch(new EAdURIImpl(file
						.toString()));
			}
			else {
				JOptionPane
						.showMessageDialog(
								null,
								"Error importing the game. Check the console for more information.",
								"Import error",
								JOptionPane.ERROR_MESSAGE);
			}
		} 

	}
}
