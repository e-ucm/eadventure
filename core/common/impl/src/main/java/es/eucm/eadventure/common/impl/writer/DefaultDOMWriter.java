package es.eucm.eadventure.common.impl.writer;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

public class DefaultDOMWriter extends DOMWriter<Object> {

	@Override
	public Element buildNode(Object data) {
		try {
			initilizeDOMWriter();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		node = doc.createElement("object");
		node.setAttribute("class", data.getClass().getName());

		if (data instanceof Class) {
			node.setTextContent(((Class<?>) data).getName());
		} else
			node.setTextContent(data.toString());
		return node;
	}

}
