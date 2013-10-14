package es.eucm.ead.tests.importer;

import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.ead.reader2.AdventureReader;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.java.JavaTextFileReader;
import es.eucm.ead.tools.java.reflection.JavaReflectionProvider;
import es.eucm.ead.tools.java.utils.FileUtils;
import es.eucm.ead.tools.java.xml.SaxXMLParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TestImporter {

	private AdventureConverter converter;

	private AdventureReader reader;

	private final String ROOT = "src/test/resources/";

	private final String[] GAMES = new String[] { "DamaBoba", "PrimerosAuxilios" };

	private final File TEMP = new File("temp/");

	@Before
	public void setUp() {
		converter = new AdventureConverter();
		converter.setEnableTranslations(false);
		reader = new AdventureReader(new JavaReflectionProvider(),
				new SaxXMLParser(), new JavaTextFileReader());
		TEMP.mkdirs();
	}

	@Test
	public void testImports() {
		for (String g : GAMES) {
			String destiny = converter
					.convert(ROOT + g, TEMP.getAbsolutePath());
			EAdAdventureModel modelConverted = converter.getModel();
			reader.setPath(destiny + "/");
			EAdAdventureModel modelRead = reader.readFullModel();
			assertTrue(EAdUtils.equals(modelConverted, modelRead, false,
					new EAdUtils.NotEqualHandler() {
						@Override
						public void notEqual(Object o1, Object o2) {
							System.out.println(o1 + " is not equal to " + o2);
						}
					}));
		}
		try {
			FileUtils.deleteRecursive(TEMP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
