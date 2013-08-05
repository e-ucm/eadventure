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

package ead.tools.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.tools.ConfigBackend;

public class ConfigBackendProperties extends ConfigBackend {

	public static Logger logger = LoggerFactory.getLogger("ConfigBackend");

	private Properties props = new Properties();
	private File saveFile;

	@Override
	public boolean load(String sourceURL) {
		try {
			props.loadFromXML(new FileInputStream(new File(sourceURL)));
			saveFile = new File(sourceURL);
			return true;
		} catch (FileNotFoundException ex) {
			logger
					.debug(
							"Could not load properties from {}. File doesn't exist. File will be created",
							sourceURL, ex);
		} catch (InvalidPropertiesFormatException e) {
			logger
					.debug(
							"Invalid properties format exception in {}. Content will be ignored.",
							sourceURL);
		} catch (IOException e) {
			logger.error(
					"Something went wrong while loading properties file {}",
					sourceURL, e);
		}
		return false;
	}

	@Override
	public boolean save(String targetURL) {
		if (saveFile == null && targetURL == null) {
			throw new IllegalArgumentException("Cannot save if never loaded");
		} else if (targetURL != null) {
			saveFile = new File(targetURL);
		}
		try {
			props.storeToXML(new FileOutputStream(saveFile), "Saved at "
					+ new Date().toString());
			return true;
		} catch (Exception ex) {
			logger.error("Could not save properties to {}", saveFile, ex);
		}
		return false;
	}

	@Override
	public String getValue(Object key) {
		return props.getProperty("" + key);
	}

	@Override
	public void put(Object key, String value) {
		props.put("" + key, value);
	}
}
