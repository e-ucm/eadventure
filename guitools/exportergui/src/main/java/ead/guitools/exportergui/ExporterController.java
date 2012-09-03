package ead.guitools.exportergui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.codehaus.plexus.util.FileUtils;

import ead.exporter.GeneralExporter;
import ead.importer.EAdventureImporter;
import ead.tools.java.JavaFileUtils;

public class ExporterController {

	private GeneralExporter exporter;
	private EAdventureImporter importer;
	private boolean deleteTempFile;

	public ExporterController() {
		exporter = new GeneralExporter();
		importer = new EAdventureImporter();
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
		if ( deleteTempFile ){
			File f = new File( finalGameFolder );
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				
			}
		}
	}

	private String getFolder(String gameBase) {
		deleteTempFile = true;
		if (gameBase.endsWith(".ead")) {
			try {
				File tempFile = JavaFileUtils.getTempFolder("eAdventureImport");
				ZipFile zipFile = new ZipFile(gameBase);
				// If project is 2.0 or greater
				if ( zipFile.getEntry("data.xml") != null ){
					zipFile.close();
					extractFolder(gameBase, tempFile.getAbsolutePath());
				}
				else {
					importer.importGame(gameBase, tempFile.getAbsolutePath());
				}
				return tempFile.getAbsolutePath();
		
			} catch (IOException e) {

			}
		}
		else {
			File f = new File( gameBase, "data.xml");
			if ( f.exists() ){
				deleteTempFile = false;
				return gameBase;
			}
			else {
				File tempFile = JavaFileUtils.getTempFolder("eAdventureImport");
				importer.importGame(gameBase, tempFile.getAbsolutePath());
				return tempFile.getAbsolutePath();
			}
		}
		return null;
	}

	public void extractFolder(String zipFile, String newPath)
			throws ZipException, IOException {
		int BUFFER = 2048;
		File file = new File(zipFile);
		ZipFile zip = new ZipFile(file);

		new File(newPath).mkdir();
		Enumeration<?> zipFileEntries = zip.entries();

		while (zipFileEntries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			File destinationParent = destFile.getParentFile();
			destinationParent.mkdirs();
			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(
						zip.getInputStream(entry));
				int currentByte;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
		zip.close();
	}

}
