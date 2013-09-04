package es.eucm.ead.playground.engine.desktop;

import es.eucm.ead.engine.desktop.DesktopGame;

public class RunGame {

	public static void main(String args[]) {
		DesktopGame game = new DesktopGame(true, "convertedproject/");
		game.setFullscreen(true);
		game.start();
		game.load();
	}
}
