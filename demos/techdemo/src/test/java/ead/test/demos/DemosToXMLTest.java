package ead.test.demos;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.demos.elementfactories.scenes.scenes.InitScene;
import ead.writer.EAdAdventureModelWriter;

public class DemosToXMLTest {

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
		EAdAdventureModelWriter writer = new EAdAdventureModelWriter();
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
