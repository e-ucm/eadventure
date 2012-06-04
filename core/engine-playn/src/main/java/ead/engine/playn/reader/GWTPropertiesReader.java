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

package ead.engine.playn.reader;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import ead.engine.playn.core.platform.EngineCallback;

public class GWTPropertiesReader {

	private static final Logger logger = LoggerFactory
			.getLogger("PropertiesReader");

	private Map<String, String> propertiesMap;

	private EngineCallback callback;

	public void readProperties(String fileName, Map<String, String> properties,
			EngineCallback cbck) {
		this.propertiesMap = properties;
		this.callback = cbck;
		logger.info("Reading game properties...");
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileName);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {

				@Override
				public void onError(Request arg0, Throwable arg1) {

				}

				@Override
				public void onResponseReceived(Request arg0, Response resp) {
					String properties = resp.getText();
					logger.info("Find {}. Processing...", properties);

					for (String line : properties.split("\\r?\\n")) {
						String[] strings = line.split("=");
						if (strings.length == 2) {
							String key = strings[0];
							String value = strings[1];
							logger.info("{}={} read.", key, value);
							propertiesMap.put(key, value);
						}
					}
					callback.done();

				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

}
