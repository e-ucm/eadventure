package ead.reader.elements.readers;

import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.reader.adventure.DOMTags;
import ead.reader.elements.ElementsFactory;
import ead.reader.elements.XMLVisitor;
import ead.reader.elements.XMLVisitor.VisitorListener;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

@SuppressWarnings("rawtypes")
public class MapReader extends AbstractReader<EAdMap> {

	public MapReader(ElementsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAdMap read(XMLNode node) {
		Class<?> keyClass = super.getNodeClass(node.getAttributes().getValue(
				DOMTags.KEY_CLASS_AT));
		Class<?> valueClass = super.getNodeClass(node.getAttributes().getValue(
				DOMTags.VALUE_CLASS_AT));
		EAdMap map = new EAdMapImpl(keyClass, valueClass);
		MapVisitorListener listener = new MapVisitorListener(map);
		XMLNodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			xmlVisitor.loadElement(childNodes.item(i), listener);
		}
		return map;
	}

	public class MapVisitorListener implements VisitorListener {
		private EAdMap map;

		private Object key;

		public MapVisitorListener(EAdMap map) {
			this.map = map;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void loaded(Object object) {
			if (key == null) {
				key = object;
			} else {
				map.put(key, object);
				key = null;
			}

		}

	}

}
