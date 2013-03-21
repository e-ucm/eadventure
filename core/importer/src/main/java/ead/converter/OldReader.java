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
