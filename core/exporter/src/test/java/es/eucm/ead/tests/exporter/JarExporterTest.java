package es.eucm.ead.tests.exporter;

import es.eucm.ead.exporter.JarExporter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JarExporterTest {

	public void testJarExport() {
		JarExporter exporter = new JarExporter();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.export("src/test/resources", null, new PrintStream(os));
		String result = os.toString();
		if (result.contains("BUILD SUCCESS")) {
			assertTrue(result.contains("BUILD SUCCESS"));
		} else {
			fail(result);
		}
	}
}
