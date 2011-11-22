package es.eucm.eadventure.common.impl.writer;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.params.EAdParam;

/**
 * Writer for {@link EAdParam}
 * 
 */
public class ParamDOMWriter extends DOMWriter<EAdParam> {

	public static final String TAG = "param";

	@Override
	public Element buildNode(EAdParam data) {
		Element node = doc.createElement(TAG);
		String value = paramsMap.get(data.toStringData());
		if (value == null) {
			value = data.toStringData();
			paramsMap.put(value, "" + paramsMap.keySet().size());
		}
		node.setAttribute(DOMTags.CLASS_AT, shortClass(data.getClass().getName()));
		node.setTextContent(value);
		return node;
	}

}
