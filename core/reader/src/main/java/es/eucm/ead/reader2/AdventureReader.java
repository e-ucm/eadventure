package es.eucm.ead.reader2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.model.elements.EAdChapter;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.reader2.model.Manifest;
import es.eucm.ead.reader2.model.ReaderVisitor;
import es.eucm.ead.reader2.model.XMLFileNames;
import es.eucm.ead.tools.TextFileReader;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.tools.xml.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AdventureReader {

	private static final Logger logger = LoggerFactory
			.getLogger("AdventureReader");

	private TextFileReader reader;

	private XMLParser parser;

	private ReaderVisitor readerVisitor;

	private Object result;

	@Inject
	public AdventureReader(ReflectionProvider reflectionProvider,
			XMLParser parser, TextFileReader reader) {
		this.reader = reader;
		this.parser = parser;
		this.readerVisitor = new ReaderVisitor(reflectionProvider);
	}

	public Manifest getManifest() {
		return (Manifest) read(XMLFileNames.MANIFEST);
	}

	public Object read(String file) {
		XMLNode node = parser.parse(reader.read("@" + file));
		readerVisitor.loadElement(node, new ReaderVisitor.VisitorListener() {
			@Override
			public boolean loaded(XMLNode node, Object object,
					boolean isNullInOrigin) {
				result = object;
				return true;
			}
		});
		readerVisitor.finish();
		return result;

	}

	public EAdChapter readChapter(String chapterId) {
		return (EAdChapter) read(chapterId + "." + XMLFileNames.CHAPTER);
	}

	public EAdScene readScene(String sceneId) {
		return (EAdScene) read(sceneId + "." + XMLFileNames.SCENE);
	}
}
