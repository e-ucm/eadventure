package es.eucm.eadventure.engine.reader;

import java.util.logging.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

public class GWTStringReader {

	private static Logger logger = Logger.getLogger("GWTStringReader");

	public void readXML(String fileName, final StringHandler stringHandler) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileName);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					//throw exception;
				}

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
