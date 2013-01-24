package ead.writer.model.writers;

import ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ObjectsCache;

public abstract class AbstractWriter<T> implements Writer<T> {

	protected XMLParser xmlParser;

	protected ObjectsCache objectCache;

	protected ModelVisitor modelVisitor;

	public AbstractWriter(ModelVisitor modelVisitor, XMLParser xmlParser,
			ObjectsCache objectCache) {
		this.xmlParser = xmlParser;
		this.objectCache = objectCache;
		this.modelVisitor = modelVisitor;
	}

	protected String translateClass(Class<? extends Object> clazz) {
		return objectCache.translateClass(clazz);
	}

}
