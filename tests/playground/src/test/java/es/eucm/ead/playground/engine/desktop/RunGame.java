package es.eucm.ead.playground.engine.desktop;

import es.eucm.ead.engine.desktop.DesktopGame;

public class RunGame {

	public static void main(String args[]) {
		DesktopGame game = new DesktopGame();
		game.setPath("convertedproject/");
		game.start();
	}
}
