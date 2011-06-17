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

package es.eucm.eadventure.common.impl.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.impl.importer.auxiliar.TemporalInputStreamCreator;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.impl.writer.EAdAdventureModelWriter;
import es.eucm.eadventure.common.loader.Loader;
import es.eucm.eadventure.common.loader.incidences.Incidence;
import es.eucm.eadventure.common.model.EAdAdventureModel;

/**
 * An importer for old games from 1.x version
 * 
 */
public class EAdventure1XImporter {

	private EAdElementImporter<AdventureData, EAdAdventureModel> adventureImporter;

	private ResourceImporter resourceImporter;

	@Inject
	public EAdventure1XImporter(
			EAdElementImporter<AdventureData, EAdAdventureModel> adventureImp,
			ResourceImporter resourceImporter) {
		this.adventureImporter = adventureImp;
		this.resourceImporter = resourceImporter;
	}

	/**
	 * Imports and old game form 1.x version
	 * 
	 * @param file
	 *            Could be the project folder, an *.eap file or and *.ead file
	 * @param destiny
	 *            Folder path where the import project will be stored
	 * @return An {@link EAdventureModel} complete with all game information
	 */
	public EAdAdventureModel importGame(String file, String destiny) {
		String folder = file;
		if ( file.endsWith(".eap") ){
			folder = file.substring(0, file.length() - 4 );
		}
		
		AdventureData adventureData = this.loadGame(folder);
		
		resourceImporter.setPaths(folder, destiny);

		EAdAdventureModel model = adventureImporter.init(adventureData);
		model = adventureImporter.convert(adventureData, model);
		
		EAdAdventureModelWriter writer = new EAdAdventureModelWriter();
		
		try {
			OutputStream os = new FileOutputStream( new File( destiny, "data.xml"));
			writer.write(model, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return model;

	}

	/**
	 * Loads an old model AdventureData from a file
	 * 
	 * @param file
	 *            Could be the project folder, an *.eap file or and *.ead file
	 * @return An old data model game
	 */
	public AdventureData loadGame(String file) {
		ArrayList<Incidence> incidences = new ArrayList<Incidence>();
		return Loader.loadAdventureData(new TemporalInputStreamCreator(file),
				incidences, true);
	}

}
