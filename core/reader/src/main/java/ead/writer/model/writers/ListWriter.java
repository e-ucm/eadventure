package ead.writer.model.writers;

import ead.common.model.elements.extra.EAdList;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;
import ead.writer.model.ObjectsCache;

public class ListWriter extends AbstractWriter<EAdList<?>> {

	public ListWriter(ModelVisitor modelVisitor, XMLParser xmlParser,
			ObjectsCache objectCache) {
		super(modelVisitor, xmlParser, objectCache);
	}

	@Override
	public XMLNode write(EAdList<?> object) {
		XMLNode node = xmlParser.newNode(DOMTags.LIST_TAG);
		ListWriterVisitor listVisitor = new ListWriterVisitor(node);
		for (Object o : object) {
			modelVisitor.writeElement(o, listVisitor);
		}
		return node;
	}

	public static class ListWriterVisitor implements VisitorListener {

		private XMLNode list;

		public ListWriterVisitor(XMLNode list) {
			this.list = list;
		}

		@Override
		public void load(XMLNode node, Object object) {
			list.append(node);
		}

	}

}
