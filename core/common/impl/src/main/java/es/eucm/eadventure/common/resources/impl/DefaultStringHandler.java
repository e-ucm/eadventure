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

package es.eucm.eadventure.common.resources.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

/**
 * Default and generic string handler implementation
 */
@Singleton
public class DefaultStringHandler implements StringHandler {

	/**
	 * The EAdString key and string cache
	 */
	private Map<EAdString, String> cache;

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultStringHandler.class);

	@Inject
	public DefaultStringHandler() {
		this.cache = new HashMap<EAdString, String>();
	}

	@Override
	public String getString(EAdString eAdString) {
		String string = cache.get(eAdString);
		if (string == null) {
			logger.info("Missing engine string " + eAdString);
			return "Missing " + eAdString;
		}
		return string;
	}

	// TODO Locale?
	@Override
	public void addString(EAdString eAdString, String string) {
		cache.put(eAdString, string);
	}

	@Override
	public String getUniqueId() {
		return "id" + (new Random()).nextLong();
	}

	@Override
	public void loadStrings(InputStream inputStream) {
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			for (Object key : properties.keySet())
				cache.put(new EAdString((String) key),
						properties.getProperty((String) key));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public EAdString addNewString(String string) {
		EAdString eadString = new EAdString(getUniqueId());
		this.addString(eadString, string);
		return eadString;
	}

}
