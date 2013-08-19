package es.eucm.ead.tools.xml;

import java.util.Map;

public class XMLWriter {

	public String generateString(XMLNode document) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
		xml += generateNode(document);
		return xml;
	}

	private String generateNode(XMLNode node) {
		String xml = "";
		xml += "<" + node.getNodeName();
		// write attributes
		for (Map.Entry<String, String> entry : node.getAttributes().entrySet()) {
			xml += " " + entry.getKey() + "=\"" + entry.getValue() + "\"";
		}
		if (node.hasChildNodes() || !"".equals(node.getNodeText())) {
			xml += ">";
			xml += node.getNodeText();
			for (XMLNode n : node.getChildren()) {
				xml += generateNode(n);
			}
			xml += "</" + node.getNodeName() + ">";
		} else {
			xml += "/>";
		}
		return xml;
	}
}
