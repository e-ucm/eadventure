package es.eucm.ead.tests.techdemo.demos;

import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.reader.AdventureReader;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.InitScene;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.tools.java.utils.Log4jConfig;
import es.eucm.ead.tools.java.utils.Log4jConfig.Slf4jLevel;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import es.eucm.ead.writer.AdventureWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class DemosToXMLTest {

	static {
		Log4jConfig.configForConsole(Slf4jLevel.Debug, null);
	}

	private static final Logger logger = LoggerFactory.getLogger("DemosToXml");

	@Test
	public void testWriteDemo() throws IOException {

		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);

		logger.debug("Writing demo model to src/main/resources/data.xml");
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, "src/main/resources/data.xml");

		AdventureReader reader = new AdventureReader(new SaxXMLParser(),
				new JavaReflectionProvider());
		String xml = "";
		String s = null;
		BufferedReader r = new BufferedReader(new FileReader(new File(
				"src/main/resources/data.xml")));
		while ((s = r.readLine()) != null) {
			xml += s;
		}
		EAdAdventureModel model2 = reader.readXML(xml);
		writer.write(model2, "src/main/resources/data2.xml");

		File f = new File("src/main/resources/data2.xml");
		assertTrue(FileUtils.isFileBinaryEqual(new File(
				"src/main/resources/data.xml"), f));
		f.delete();

		//		AdventureReader reader = new AdventureReader(new DomXMLParser());
		//
		//		ObjectFactory.init(new JavaReflectionProvider());
		//		ReflectionClassLoader.init(new JavaReflectionClassLoader());
		//		ObjectFactory.initialize();
		//		EAdAdventureModel readModel = reader.readXML(FileUtils
		//				.getText(new File("src/main/resources/data.xml")));
		//		assertTrue(readModel != null);
		logger.debug("Done.");
	}
}
