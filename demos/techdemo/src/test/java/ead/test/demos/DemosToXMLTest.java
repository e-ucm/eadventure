package ead.test.demos;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.demos.elementfactories.scenes.scenes.InitScene;
import ead.tools.java.reflection.JavaReflectionProvider;
import ead.tools.java.xml.JavaXMLParser;
import ead.utils.Log4jConfig;
import ead.utils.Log4jConfig.Slf4jLevel;
import ead.writer.AdventureWriter;

public class DemosToXMLTest {

	static {
		Log4jConfig.configForConsole(Slf4jLevel.Debug, null);
	}

	private static final Logger logger = LoggerFactory.getLogger("DemosToXml");

	/**
	 * Writes the demos into an XML file
	 * @param args
	 */
	@Test
	public void testWriteDemo() {

		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);

		logger.debug("Writing demo model to src/main/resources/data.xml");
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider(), new JavaXMLParser());
		writer.write(model, "src/main/resources/data.xml");

		//		AdventureReader reader = new AdventureReader(new JavaXMLParser());
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
