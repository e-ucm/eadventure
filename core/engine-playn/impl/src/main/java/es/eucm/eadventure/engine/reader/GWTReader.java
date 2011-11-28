package es.eucm.eadventure.engine.reader;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.engine.core.Game;

public class GWTReader {
	
	private String xml;

	public void readXML(String fileName, final Game game) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileName);

		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					//throw exception;
				}

				public void onResponseReceived(Request request,
						Response response) {
					xml = response.getText();
					Document doc = XMLParser.parse(xml);

					ElementNodeVisitor env = new ElementNodeVisitor();
					NodeVisitor.init(doc.getFirstChild().getAttributes().getNamedItem(DOMTags.PACKAGE_AT).getNodeValue());
					EAdAdventureModel data = (EAdAdventureModel) env.visit(doc.getFirstChild().getFirstChild(), null, null, null);
					data.getId();
					
					game.setGame(data, data.getChapters().get(0));


				}
			});
		} catch (RequestException ex) {
			// requestFailed(ex);
		}
	}
	
	static String getNodeText(Node xmlNode) {
        if(xmlNode == null)
                return "";
        NodeList nodes = xmlNode.getChildNodes();
        String result = "";
        for (int i = 0; i < nodes.getLength(); i++) {
        	String value = nodes.item(i).getNodeValue();
            if (value != null)
            	result += (value.equals("null") ? "" : value);
        }
        return result;
	}
	
}
