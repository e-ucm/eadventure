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

package ead.converter;

import java.io.InputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.converter.inputstreamcreators.ImporterInputStreamCreator;
import ead.importer.auxiliar.ImporterImageLoaderFactory;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.loader.Loader;
import es.eucm.eadventure.common.loader.incidences.Incidence;

@Singleton
public class OldReader {

	private static final Logger logger = LoggerFactory.getLogger("OldReader");

	private ImporterInputStreamCreator inputStreamCreator;

	private ImageLoaderFactory imageLoader = new ImporterImageLoaderFactory();

	@Inject
	public OldReader() {
		inputStreamCreator = new ImporterInputStreamCreator();
	}

	/**
	 * Loads an old model AdventureData
	 * 
	 */
	public AdventureData loadGame(String file) {
		inputStreamCreator.setFile(file);
		ArrayList<Incidence> incidences = new ArrayList<Incidence>();
		AdventureData data = null;
		try {
			data = Loader.loadAdventureData(inputStreamCreator, incidences);
		} catch (Exception e) {
			logger.error("Exception while reading old <e-Adventure> game", e);
		}
		if (data == null) {
			logger.warn("Invalid <e-Adventure> game");
		}

		if (incidences.size() > 0) {
			logger.info("There were the following incidences during loading:");
			for (Incidence i : incidences) {
				logger.info(i.getMessage());
			}
		}
		return data;
	}

	public InputStream getInputStream(String file) {
		return inputStreamCreator.buildInputStream(file);
	}

	public Animation getAnimation(String file) {
		return Loader.loadAnimation(inputStreamCreator, file, imageLoader);
	}

	public InputStreamCreator getInputStreamCreator() {
		return inputStreamCreator;
	}

}
