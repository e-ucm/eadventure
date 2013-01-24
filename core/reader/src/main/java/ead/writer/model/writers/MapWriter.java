package ead.writer.model.writers;

import ead.common.model.elements.extra.EAdMap;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ObjectsCache;

public class MapWriter extends AbstractWriter<EAdMap<?, ?>> {

	public MapWriter(ModelVisitor modelVisitor, XMLParser xmlParser,
			ObjectsCache objectCache) {
		super(modelVisitor, xmlParser, objectCache);
	}

	@Override
	public XMLNode write(EAdMap<?, ?> object) {
		// TODO Auto-generated method stub
		return null;
	}

}
