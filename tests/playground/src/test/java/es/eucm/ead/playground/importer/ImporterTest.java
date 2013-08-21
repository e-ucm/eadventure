package es.eucm.ead.playground.importer;

import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.reader2.model.XMLFileNames;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ImporterTest {

	public static final String TEST_PROJECT = "src/test/resources/testproject/";
	public static final String CONVERTED_PROJECT = "src/test/resources/convertedproject/";

	@Test
	public void test() {
		AdventureConverter converter = new AdventureConverter();
		converter.convert(TEST_PROJECT, CONVERTED_PROJECT);
		assertTrue(new File(CONVERTED_PROJECT + XMLFileNames.MANIFEST).exists());
	}
}
