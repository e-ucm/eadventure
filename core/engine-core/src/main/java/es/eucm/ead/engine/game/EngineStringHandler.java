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

	private static final Logger logger = LoggerFactory
			.getLogger("EngineStringHandler");

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
