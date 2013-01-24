package ead.writer.model.writers;

import ead.reader.DOMTags;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ObjectsCache;

public class ParamWriter extends AbstractWriter<Object> {

	public ParamWriter(ModelVisitor modelVisitor, XMLParser xmlParser,
			ObjectsCache objectCache) {
		super(modelVisitor, xmlParser, objectCache);
	}

	@Override
	public XMLNode write(Object o) {
		XMLNode node = xmlParser.newNode(DOMTags.PARAM_AT);
		if (o != null) {
			String translatedClass = translateClass(o.getClass());
			node.setAttribute(DOMTags.CLASS_AT, translatedClass);
			node.setText(o.toString());
		}
		return node;
	}
}
