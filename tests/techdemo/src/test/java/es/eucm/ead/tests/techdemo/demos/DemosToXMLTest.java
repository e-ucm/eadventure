package es.eucm.ead.tests.techdemo.demos;

import es.eucm.ead.model.elements.AdventureGame;
import es.eucm.ead.model.elements.Chapter;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.reader.AdventureReader;
import es.eucm.ead.reader.model.Manifest;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.InitScene;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.java.JavaTextFileReader;
import es.eucm.ead.tools.java.JavaTextFileWriter;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import es.eucm.ead.writer.AdventureWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class DemosToXMLTest {

	static private Logger logger = LoggerFactory
			.getLogger(DemosToXMLTest.class);

	@Test
	public void testWriteDemo() throws IOException {
		JavaTextFileWriter fileWriter = new JavaTextFileWriter();
		JavaTextFileReader fileReader = new JavaTextFileReader();

		InitScene scene = new InitScene();
		Chapter chapter = new Chapter(scene);
		AdventureGame model = new AdventureGame();
		model.getChapters().add(chapter);
		for (Scene s : scene.getScenes()) {
			chapter.addScene(s);
		}

		String path = "src/main/resources/";
		String path2 = "src/main/resources/techdemo2/";

		File folder2 = new File(path2);
		folder2.mkdirs();

		logger.debug("Writing demo model to {}", path);
		AdventureWriter writer = new AdventureWriter(
				new JavaReflectionProvider());
		AdventureReader reader = new AdventureReader(
				new JavaReflectionProvider(), new SaxXMLParser(), fileReader);

		writer.write(model, path, fileWriter);
		writer.write(model, path2, fileWriter);

		reader.setPath(path);
		Manifest manifest1 = reader.getManifest();
		Manifest manifest2 = reader.getManifest();
		reader.setPath(path2);
		Manifest manifest3 = reader.getManifest();

		assertTrue(EAdUtils.equals(manifest1, manifest2, false));
		assertTrue(EAdUtils.equals(manifest1, manifest3, false));

		for (String c : manifest1.getChapterIds()) {
			reader.setPath(path);
			Chapter chapter1 = reader.readChapter(c);
			reader.setPath(path2);
			Chapter chapter2 = reader.readChapter(c);
			assertTrue(EAdUtils.equals(chapter1, chapter2, false));
		}
		FileUtils.deleteRecursive(new File(path2));

	}
}
