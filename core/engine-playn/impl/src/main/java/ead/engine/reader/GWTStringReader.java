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

package ead.engine.reader;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import ead.common.params.text.EAdString;
import ead.common.util.StringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTStringReader {

	private static Logger logger = LoggerFactory.getLogger("GWTStringReader");

	public void readXML(String fileName, final StringHandler stringHandler) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileName);

		logger.info("Read strings: {}", fileName);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
				public void onError(Request request, Throwable exception) {
					//throw exception;
				}

                @Override
				public void onResponseReceived(Request request,
						Response response) {
					String xml = response.getText();
					Document doc = XMLParser.parse(xml);


					NodeList nl = doc.getFirstChild().getChildNodes();
					for (int i = 0; i < nl.getLength(); i++) {
						String name = nl.item(i).getAttributes().getNamedItem("name").getNodeValue();
						String value = GWTReader.getNodeText(nl.item(i));
						stringHandler.setString(new EAdString(name), value);
					}
				}
			});
		} catch (RequestException ex) {
			// requestFailed(ex);
		}
	}


}
