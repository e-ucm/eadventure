package ead.common.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ead.common.PropertiesReader;
import ead.common.model.elements.EAdAdventureModel;

public class PropertiesReaderImpl implements PropertiesReader {

	@Override
	public void setProperties(EAdAdventureModel model,
			InputStream eadPropertiesFile) {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				eadPropertiesFile));
		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				String[] strings = line.split("=");
				if (strings.length == 2) {
					String key = strings[0];
					String value = strings[1];
					model.setProperty(key, value);
				}
			}
		} catch (IOException e) {

		}
	}

}
