package es.eucm.eadventure.common.impl.writer;

import org.w3c.dom.Element;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.params.EAdParam;

/**
 * Writer for {@link EAdParam}
 * 
 */
public class ParamDOMWriter extends DOMWriter<Object> {

	@Override
	public Element buildNode(Object data, Class<?> listClass) {
		Element node = doc.createElement(DOMTags.PARAM_AT);
		
		String value = null;
		if (data instanceof EAdParam)
			value = ((EAdParam) data).toStringData();
		else if (data instanceof Class)
			value = ((Class<?>) data).getName();
		else
			value = data.toString();

		String compressedValue = paramsMap.get(data);
		if (compressedValue == null) {
			String key = DOMTags.PARAM_AT + paramsMap.keySet().size();
			paramsMap.put(data, key);
		} else {
			value = compressedValue;
		}
		
		if ( listClass == null || listClass != data.getClass() )
			node.setAttribute(DOMTags.CLASS_AT, shortClass(data.getClass().getName()));
		node.setTextContent(value);
		return node;
	}

}
