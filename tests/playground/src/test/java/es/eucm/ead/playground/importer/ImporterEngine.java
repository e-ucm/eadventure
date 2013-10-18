package es.eucm.ead.playground.importer;

import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.importer.AdventureConverter;

public class ImporterEngine {

	public static String TEST = "/home/eva/eadventure/juegos/PrimerosAuxiliosGame";

	public static void main(String args[]) {
		AdventureConverter converter = new AdventureConverter();
		converter.setEnableTranslations(false);
		String convertedFolder = converter.convert(TEST, TEST + "/ead2");

		DesktopGame game = new DesktopGame();
		game.setDebug(true);
		game.setPath(convertedFolder);
		game.start();
	}
}
