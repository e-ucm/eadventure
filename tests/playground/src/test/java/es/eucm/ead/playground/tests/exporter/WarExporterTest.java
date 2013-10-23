package es.eucm.ead.playground.tests.exporter;

import es.eucm.ead.exporter.WarExporter;

public class WarExporterTest {

	public static void main(String args[]) {
		WarExporter exporter = new WarExporter();
		exporter.export("src/test/resources/convertedproject", "export");
	}
}
