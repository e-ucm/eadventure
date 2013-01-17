package ead.reader.elements.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.reader.adventure.DOMTags;
import ead.reader.elements.ElementsFactory;
import ead.reader.elements.XMLVisitor;
import ead.tools.xml.XMLNode;

public abstract class AbstractReader<T> implements ElementReader<T> {
	
	protected static final Logger logger = LoggerFactory.getLogger("ElementReader");
	
	protected ElementsFactory elementsFactory;
	
	protected XMLVisitor xmlVisitor;
	
	public AbstractReader(ElementsFactory elementsFactory, XMLVisitor xmlVisitor){
		this.elementsFactory = elementsFactory;
		this.xmlVisitor = xmlVisitor;
	}

	/**
	 * Returns the class for the element contained in the given node
	 * @param node
	 * @return
	 */
	public Class<?> getNodeClass(XMLNode node){
		String clazz = node.getAttributes().getValue(DOMTags.CLASS_AT);
		return getNodeClass(clazz);
	}
	
	public Class<?> getNodeClass(String clazz){
		clazz = translateClass(clazz);
		Class<?> c = Object.class;
		try {
			c = elementsFactory.getClassFromName(clazz);
		} catch (NullPointerException e) {
			logger.error("Error resolving class {}", clazz, e);
		}
		return c;
	}
	
	/**
	 * Translate the class into its complete name
	 * @param clazz
	 * @return
	 */
	public String translateClass( String clazz ){
		return xmlVisitor.translate(clazz);
	}

}
