package es.eucm.ead.tests.writer;

import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.reader2.AdventureReader;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import es.eucm.ead.writer2.AdventureWriter;
import org.junit.Test;

public class ReaderWriterTest {

	@Test
	public void testReadWrite() {
		String path = "src/test/resources/tests";
		BasicAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		BasicChapter chapter2 = new BasicChapter();
		model.getChapters().add(chapter);
		model.getChapters().add(chapter2);
		BasicScene scene1 = new BasicScene();
		scene1.addSceneElement(new SceneElement());
		BasicScene scene2 = new BasicScene();
		scene2.addSceneElement(new SceneElement());
		chapter.addScene(scene1);
		chapter.addScene(scene2);
		model.setInitialChapter(chapter2);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		writer.write(model, path, new JavaTextFileWriter());
		AdventureReader reader = new AdventureReader(
				new JavaReflectionProvider(), new SaxXMLParser(),
				new TestTextFileReader());
	}
}
