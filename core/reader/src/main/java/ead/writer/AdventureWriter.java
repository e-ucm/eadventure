package ead.writer;

import com.google.inject.Inject;

import ead.common.model.elements.EAdAdventureModel;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

public class AdventureWriter {

	private ModelVisitor visitor;

	private XMLParser xmlParser;

	@Inject
	public AdventureWriter(ReflectionProvider reflectionProvider,
			XMLParser parser) {
		this.visitor = new ModelVisitor(reflectionProvider, parser);
		this.xmlParser = parser;
	}

	public XMLDocument write(EAdAdventureModel model) {
		write(model, new VisitorListener() {

			@Override
			public void load(XMLNode node, Object object) {
				// Do nothing
			}

		});
		boolean done = false;
		while (!done) {
			done = visitor.step();
		}
		return visitor.getDocument();
	}

	public void write(EAdAdventureModel model, VisitorListener listener) {
		visitor.clear();
		visitor.writeElement(model, listener);
	}

	public void write(EAdAdventureModel model, String string) {
		XMLDocument doc = write(model);
		xmlParser.writeToFile(doc, string);
	}

}
