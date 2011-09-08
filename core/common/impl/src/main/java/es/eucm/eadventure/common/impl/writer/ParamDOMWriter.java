package es.eucm.eadventure.common.impl.writer;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.params.EAdParam;

public class ParamDOMWriter extends DOMWriter<EAdParam>{
	
	public static final String PREFIX = "param";

	@Override
	public Element buildNode(EAdParam data) {
		try {
			initilizeDOMWriter();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		node = doc.createElement("param");
		String value = paramsMap.get(data.toStringData());
		if ( value == null ){
			value = data.toStringData();
			paramsMap.put(value, "" + paramsMap.keySet().size());
		}
		node.setTextContent(value);
		return node;
	}

}
