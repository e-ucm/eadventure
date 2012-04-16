package ead.common;

import java.io.InputStream;

import ead.common.model.elements.EAdAdventureModel;

/**
 * Reads the properties defined in ead.properties for a model and set them
 * 
 * 
 */
public interface PropertiesReader {

	void setProperties( EAdAdventureModel model, InputStream eadPropertiesFile );
}
