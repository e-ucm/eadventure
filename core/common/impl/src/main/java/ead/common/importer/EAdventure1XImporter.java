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

package ead.common.importer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.ProjectFiles;
import ead.common.StringFileHandler;
import ead.common.importer.auxiliar.inputstreamcreators.ImporterInputStreamCreator;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.common.util.StringHandler;
import ead.common.writer.EAdAdventureModelWriter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.loader.Loader;
import es.eucm.eadventure.common.loader.incidences.Incidence;

/**
 * An importer for old games from 1.x version
 *
 */
public class EAdventure1XImporter {

	private EAdElementImporter<AdventureData, EAdAdventureModel> adventureImporter;

	private ResourceImporter resourceImporter;

	private InputStreamCreator inputStreamCreator;

	private StringHandler stringsHandler;

	private StringFileHandler stringFileHandler;
	
	private EAdElementFactory elementFactory;

	private String destinyFile;

    private Random rand = new Random();

	private int progress;

	private String progressText;

	private static final Logger logger = LoggerFactory.getLogger("EAdventureImporter");

	@Inject
	public EAdventure1XImporter(
			EAdElementImporter<AdventureData, EAdAdventureModel> adventureImp,
			ResourceImporter resourceImporter,
			InputStreamCreator inputStreamCreator, StringHandler stringsWriter,
			StringFileHandler stringFileHandler, EAdElementFactory elementFactory) {
		this.adventureImporter = adventureImp;
		this.resourceImporter = resourceImporter;
		this.inputStreamCreator = inputStreamCreator;
		this.stringsHandler = stringsWriter;
		this.stringFileHandler = stringFileHandler;
		this.elementFactory = elementFactory;
	}

	/**
	 * Imports and old game form 1.x version
	 *
	 * @param eadFile
	 *            original ead file
	 * @param destiny
	 *            File where the import project will be stored. If {@code null},
	 *            import project won't be saved
	 * @return An {@link EAdventureModel} complete with all game information
	 */
	public EAdAdventureModel importGame(String eadFile, String destiny) {
		progress = 0;
		progressText = "Starting importer...";
		stringsHandler.getStrings().clear();
		((ImporterInputStreamCreator) inputStreamCreator).setFile(eadFile);
		elementFactory.init();
		progress = 10;
		progressText = "Loading old game...";
		AdventureData adventureData = loadGame();

		if (adventureData == null) {
			return null;
		}

		progress = 40;
		progressText = "Creating temporary files...";
		// Temp folder to use during importation
		String tempFolder = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tempFolder + File.separator + "eAdventureTemp"
				+ rand.nextInt());
		if (tmpDir.exists() == false) {
			tmpDir.mkdir();
		}
		tmpDir.deleteOnExit();

		progress = 50;
		progressText = "Creating temporary files...";
		resourceImporter.setPath(tmpDir.getAbsolutePath());
		destinyFile = tmpDir.getAbsolutePath();

		EAdAdventureModel model = adventureImporter.init(adventureData);
		model = adventureImporter.convert(adventureData, model);

		if (destiny != null) {
			progress = 90;
			progressText = "Creating " + destiny;
			createGameFile(model, tmpDir.getAbsolutePath(), destiny);
		}

		progress = 100;
		progressText = "Done";

		return model;

	}

	private void createGameFile(EAdAdventureModel model, String path,
			String destination) {

		// FIXME file names should be somewhere else as constants

		// Create data.xml
		EAdAdventureModelWriter writer = new EAdAdventureModelWriter();

        OutputStream os = null;
		try {
		    os = new FileOutputStream(new File(path, ProjectFiles.DATA_FILE));
			writer.write(model, os);
		} catch (Exception e) {
            logger.error("Cannot write data.xml "
                    + "while importing to '{}'", destination, e);
        } finally {
            if (os != null) try {
                os.close();
            } catch (Exception e) {
                logger.error("Error closing data.xml "
                        + "while importing '{}", destination, e);
            }
        }

		// Create strings.xml
		File f = new File(path, ProjectFiles.STRINGS_FILE);

		try {
			stringFileHandler.write(new FileOutputStream(f),
					stringsHandler.getStrings());
		} catch (Exception e) {
			logger.error("Cannot handle strings.xml "
                    + "while importing '{}'", destination, e);
		}

		// ead.properties
		File propertiesFile = new File(path, ProjectFiles.PROPERTIES_FILE);
		Properties properties = new Properties();
		properties.setProperty("targetEngine", "ead-200");

		try {
			FileOutputStream output = new FileOutputStream(propertiesFile);
			properties.store(output, "Imported version");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create zip file
		try {
			String fileName = destination.endsWith(".ead") ?
                    destination : destination + ".ead";
			File outFolder = new File(fileName);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(outFolder)));

			addFolderToZip(out, new File(path), false);
			out.flush();
			out.close();
		} catch (Exception e) {
            logger.error("Error outputting zip to {}", destination, e);
		}

	}

	public void addFolderToZip(ZipOutputStream zip, File folder, boolean addPath) {
		byte[] data = new byte[1000];
		File files[] = folder.listFiles();
		for (File f : files) {
			try {
				if (f.isDirectory()) {
					addFolderToZip(zip, f, true);
				} else {
					String entryName = (addPath ? f.getParentFile().getName()
							+ "/" : "")
							+ f.getName();

					zip.putNextEntry(new ZipEntry(entryName));

					BufferedInputStream in =
                            new BufferedInputStream(new FileInputStream(f), 1000);
					int count;
					while ((count = in.read(data, 0, 1000)) != -1) {
						zip.write(data, 0, count);
					}
					zip.closeEntry();
				}
			} catch (IOException e) {
                logger.error("Error adding folder {} to zip", folder, e);
			}
		}
	}

	/**
	 * Loads an old model AdventureData
	 *
	 */
	public AdventureData loadGame() {
		ArrayList<Incidence> incidences = new ArrayList<Incidence>();
		AdventureData data = null;
		try {
			data = Loader.loadAdventureData(inputStreamCreator, incidences, true);
		} catch(Exception e) {
			logger.error("Exception while reading old <e-Adventure> game", e);
		}
		if (data == null) {
			logger.warn("Invalid <e-Adventure> game");
		}

		logger.info("There were the following incidences during loading:");
		for (Incidence i : incidences) {
			logger.info(i.getMessage());
		}
		return data;
	}

	public String getDestinyFile() {
		return destinyFile;
	}

	public Map<EAdString, String> getStrings() {
		return stringsHandler.getStrings();
	}

	/**
	 * Return the progress of the importer (between 0 and 100)
	 * @return
	 */
	public int getProgress( ){
		return progress;
	}

	public String getProgressText( ){
		return progressText;
	}
}
