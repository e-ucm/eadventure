package ead.writer.model.writers;

import ead.common.model.elements.extra.EAdList;
import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

public class ListWriter extends AbstractWriter<EAdList<?>> {

	public ListWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
	}

	@Override
	public XMLNode write(EAdList<?> object) {
		// Don't store empty lists
		if (object.isEmpty()) {
			return null;
		}
		XMLNode node = modelVisitor.newNode(DOMTags.LIST_TAG);
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
