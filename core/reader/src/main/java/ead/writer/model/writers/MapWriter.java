package ead.writer.model.writers;

import ead.common.model.elements.extra.EAdMap;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

@SuppressWarnings("rawtypes")
public class MapWriter extends AbstractWriter<EAdMap> {

	public MapWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
	}

	@Override
	public XMLNode write(EAdMap object) {
		// Don't store empty maps
		if (object.isEmpty()) {
			return null;
		}
		XMLNode node = modelVisitor.newNode(DOMTags.MAP_TAG);

		MapWriterListener listener = new MapWriterListener(node);
		for (Object key : object.keySet()) {
			Object value = object.get(key);
			modelVisitor.writeElement(key, listener);
			modelVisitor.writeElement(value, listener);
		}
		return node;
	}

	public static class MapWriterListener implements VisitorListener {

		private XMLNode map;

		private XMLNode key;

		public MapWriterListener(XMLNode map) {
			this.map = map;
			this.key = null;
		}

		@Override
		public void load(XMLNode node, Object object) {
			if (key == null) {
				key = node;
			} else {
				map.append(key);
				map.append(node);
				key = null;
			}
		}

	}

}
