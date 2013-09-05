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

package es.eucm.ead.engine.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.reader.strings.StringsReader;
import es.eucm.ead.tools.StringHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Singleton
public class EngineStringHandler extends StringHandlerImpl {

	static private Logger logger = LoggerFactory
			.getLogger(EngineStringHandler.class);

	/**
	 * Strings reader
	 */
	private StringsReader stringsReader;

	private AssetHandler assetHandler;

	private String currentLanguage;

	@Inject
	public EngineStringHandler(StringsReader stringsReader,
			AssetHandler assetHandler) {
		this.stringsReader = stringsReader;
		this.assetHandler = assetHandler;
	}

	public void setLanguage(String language) {
		if (language != null && !language.equals(currentLanguage)) {
			currentLanguage = language;
			assetHandler.setLanguage(currentLanguage);
		}

		if (!isLanguageLoaded(language)) {
			loadStrings(language);
		}

		super.setLanguage(language);
	}

	private void loadStrings(String language) {

		logger.debug("Loading language {}", language);
		// Map containing all the files with strings (keys) and its associated
		// language (value)
		String strings = assetHandler.getTextFile("@strings" + language
				+ ".xml");
		if (strings == null || strings.equals("")) {
			logger
					.info(
							"{} language was not loaded. Maybe the strings.xml file associated is not present",
							language);
		} else {
			addLanguage(language);
			Map<EAdString, String> stringsMap = stringsReader
					.readStrings(strings);
			if (stringsMap != null) {
				addStrings(stringsMap);
				logger.info("{} language loaded", language);
			} else {
				logger.info("{} language not loaded. See previous erros.",
						language);
			}
		}
	}
}
