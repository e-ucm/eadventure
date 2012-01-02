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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.StringFileHandler;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.resources.StringHandler;
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

	private static final Logger logger = Logger.getLogger("EAdventureImporter");

	@Inject
	public EAdventure1XImporter(
			EAdElementImporter<AdventureData, EAdAdventureModel> adventureImp,
			ResourceImporter resourceImporter,
			InputStreamCreator inputStreamCreator, StringHandler stringsWriter,
			StringFileHandler stringFileHandler) {
		this.adventureImporter = adventureImp;
		this.resourceImporter = resourceImporter;
		this.inputStreamCreator = inputStreamCreator;
		this.stringsHandler = stringsWriter;
		this.stringFileHandler = stringFileHandler;
	}

	/**
	 * Imports and old game form 1.x version
	 * 
	 * @param destiny
	 *            Folder path where the import project will be stored
	 * @return An {@link EAdventureModel} complete with all game information
	 */
	public EAdAdventureModel importGame(String destiny) {
		AdventureData adventureData = loadGame();

		if (adventureData == null) {
			return null;
		}

		resourceImporter.setPath(destiny);

		EAdAdventureModel model = adventureImporter.init(adventureData);
		model = adventureImporter.convert(adventureData, model);

		EAdAdventureModelWriter writer = new EAdAdventureModelWriter();

		try {
			OutputStream os = new FileOutputStream(
					new File(destiny, "data.xml"));
			writer.write(model, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		File f = new File(destiny, "strings.xml");

		try {
			stringFileHandler.write(new FileOutputStream(f),
					stringsHandler.getStrings());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return model;

	}

	/**
	 * Loads an old model AdventureData
	 * 
	 */
	public AdventureData loadGame() {
		ArrayList<Incidence> incidences = new ArrayList<Incidence>();
		AdventureData data = Loader.loadAdventureData(inputStreamCreator,
				incidences, true);
		if (data == null) {
			logger.info("Invalid <e-Adventure> game");
		}

		logger.info("There was the following incidences during the file reading:");
		for (Incidence i : incidences) {
			logger.info(i.getMessage());
		}
		return data;
	}

}
