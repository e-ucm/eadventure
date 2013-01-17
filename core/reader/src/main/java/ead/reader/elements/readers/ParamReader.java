package ead.reader.elements.readers;

import ead.reader.elements.ElementsFactory;
import ead.reader.elements.XMLVisitor;
import ead.tools.xml.XMLNode;

/**
 * Parameters can be any type of object. Nodes with parameters have no children.
 * 
 */
public class ParamReader extends AbstractReader<Object> {

	public ParamReader(ElementsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	@Override
	public Object read(XMLNode node) {
		Class<?> clazz = getNodeClass(node);
		if (clazz == null) {
			return elementsFactory.getParam(node.getNodeText());
		}
		return elementsFactory.getParam(node.getNodeText(), clazz);
	}

}
