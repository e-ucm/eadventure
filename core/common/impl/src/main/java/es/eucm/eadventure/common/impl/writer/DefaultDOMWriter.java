package es.eucm.eadventure.common.impl.writer;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.impl.DOMTags;

/**
 * 
 * Writer for those elements that don't have a specific writer
 * 
 */
public class DefaultDOMWriter extends DOMWriter<Object> {
	
	public static final String TAG = "param";

	@Override
	public Element buildNode(Object data) {
		Element node = doc.createElement(TAG);
		node.setAttribute(DOMTags.CLASS_AT, shortClass(data.getClass().getName()));

		if (data instanceof Class) {
			node.setTextContent(((Class<?>) data).getName());
		} else
			node.setTextContent(data.toString());
		return node;
	}

}
