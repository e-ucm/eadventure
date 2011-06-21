package es.eucm.eadventure.engine.core.platform.test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.impl.importer.EAdventure1XImporter;
import es.eucm.eadventure.common.impl.importer.ImporterConfigurationModule;
import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.impl.EAdURIImpl;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
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

		
		// Default directory
		File directory = new File("src/test/resources/EAdventure1Project/");
		
		JFileChooser fileChooser = new JFileChooser(directory);

		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".eap");
			}

			@Override
			public String getDescription() {
				return "eAdventure projects";
			}

		};
		fileChooser.setFileFilter(filter);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
			
			File f = fileChooser.getSelectedFile();

			String folder = f.getParentFile().getAbsolutePath();
			
			String projectName = f.getName();
			projectName = projectName.substring(0, projectName.length() - 4 );

			Injector injector = Guice.createInjector(
					new ImporterConfigurationModule(folder + "/" + projectName ),
					new DesktopAssetHandlerModule(),
					new DesktopAssetRendererModule(null), new DesktopModule(), new BasicGameModule());
			EAdventure1XImporter importer = injector
					.getInstance(EAdventure1XImporter.class);

			EAdAdventureModel model = importer.importGame(f.getAbsolutePath(),
					folder + "/" + projectName + "Imported");

			screen = model.getChapters().get(0).getInitialScreen();
			injector.getInstance(StringHandler.class).addString(
					new EAdString("Loading"), "loading");
			LoadingScreen loadingScreen = injector
					.getInstance(LoadingScreen.class);
			loadingScreen.setInitialScreen(screen);

			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name",
					"eAdventure");

			PlatformLauncher launcher = injector
					.getInstance(PlatformLauncher.class);
			// TODO extract file from args or use default?
			File file = new File(folder, "ProjectImported");
			// File file = new File("/ProyectoJuegoFINAL.ead");
			((DesktopPlatformLauncher) launcher).launch(new EAdURIImpl( file.toString() ));
		}

	}
}
