package es.eucm.ead.playground.exporter;

import es.eucm.ead.exporter.WarExporter;

public class WarExporterTest {

	public static void main(String args[]) {
		WarExporter exporter = new WarExporter();
		exporter.export("src/test/resources/convertedproject", "export");
	}
}
