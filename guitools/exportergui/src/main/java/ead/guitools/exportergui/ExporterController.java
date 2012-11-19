package ead.guitools.exportergui;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.exporter.GeneralExporter;
import ead.importer.EAdventureImporter;
import ead.importer.ImporterModule;
import ead.tools.java.JavaToolsModule;
import ead.utils.FileUtils;

public class ExporterController {

	private static final Logger logger = LoggerFactory
			.getLogger("ExporterController");
	private GeneralExporter exporter;
	private EAdventureImporter importer;
	private boolean deleteTempFile;

	public ExporterController() {
		Injector i = Guice.createInjector(
				new ImporterModule(),
				new JavaToolsModule());

		exporter = new GeneralExporter();
		importer = i.getInstance(EAdventureImporter.class);
		deleteTempFile = false;
	}

	/**
	 * Exports the game with the chosen formats
	 * 
	 * @param name
	 *            name for the game
	 * @param gameFolder
	 *            folder with game assets
	 * @param outputFolder
	 *            output folder for the export
	 * @param jar
	 *            whether a JAR file with the game must be generated
	 * @param war
	 *            whether a WAR file with the game must be generated
	 * @param apk
	 *            whether a APK file with the game must be generated
	 * @param installApk
	 *            if the APK must be installed in connected Android devices
	 */
	public void export(String name, String gameFolder, String outputFolder,
			boolean jar, boolean war, boolean apk, boolean installApk) {
		exporter.setName(name);
		exporter.setInstallApk(installApk);
		String finalGameFolder = getFolder(gameFolder);
		exporter.export(finalGameFolder, outputFolder, jar, war, apk);
		if (deleteTempFile) {
			File f = new File(finalGameFolder);
			try {
				FileUtils.deleteRecursive(f);
			} catch (IOException e) {
				logger.error("While exporting", e);
			}
		}
	}

	private String getFolder(String gameBase) {
		deleteTempFile = true;
		try {
			if (gameBase.endsWith(".ead")) {
				File tempFile = FileUtils.createTempDir("eAdventureImport", null);
				ZipFile zipFile = new ZipFile(gameBase);
				// If project is 2.0 or greater
				if (zipFile.getEntry("data.xml") != null) {
					zipFile.close();
					extractFolder(gameBase, tempFile.getAbsolutePath());
				} else {
					importer.importGame(gameBase, tempFile.getAbsolutePath());
				}
				return tempFile.getAbsolutePath();
			} else {
				File f = new File(gameBase, "data.xml");
				if (f.exists()) {
					deleteTempFile = false;
					return gameBase;
				} else {
					File tempFile = FileUtils.createTempDir("eAdventureImport", null);
					importer.importGame(gameBase, tempFile.getAbsolutePath());
					return tempFile.getAbsolutePath();
				}
			}
		} catch (IOException e) {
			logger.error("While importing {}", 
					gameBase, e);
		}
		return null;
	}

	public void extractFolder(String zipFile, String newPath)
			throws ZipException, IOException {
		File dest = new File(newPath);
		dest.mkdir();
		FileUtils.expand(new File(zipFile), dest);
	}
}
