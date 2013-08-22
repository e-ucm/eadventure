package es.eucm.ead.tests.exporter;

import es.eucm.ead.exporter.AndroidExporter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExporterTest {

	@Test
	public void testExport() {
		AndroidExporter apkExporter = new AndroidExporter();
		Properties properties = new Properties();
		InputStream is;
		boolean error = false;
		try {
			is = ClassLoader.getSystemResourceAsStream("test.properties");
			properties.load(is);
			error = properties.get(AndroidExporter.SDK_HOME) == null;
		} catch (Exception e) {
			error = true;
		} finally {
			if (error) {
				fail("You need to create a file named 'test.properties' with the form attribute=value. One of those lines must be android-sdk=path/to/android/sdk");
			}
		}
		properties.setProperty(AndroidExporter.PACKAGE_NAME,
				"es.eucm.ead.exporter.test.game");
		properties.setProperty(AndroidExporter.TITLE, "Exporter Test Game");
		properties.setProperty(AndroidExporter.ICON,
				"src/test/resources/orangelogo.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(baos);
		apkExporter.setStdErr(stream);
		apkExporter.setStdOut(stream);
		apkExporter.export("src/test/resources/convertedproject/", null,
				properties, false);

		String result = baos.toString();
		if (result.contains("BUILD FAILURE")) {
			fail(result);
		} else {
			assertTrue(result.contains("BUILD SUCCESS"));
		}
	}
}
