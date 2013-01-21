package ead.demos.desktop;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.scenes.BasicScene;
import ead.engine.core.gdx.desktop.DesktopGame;

public class TechDemoMain2 {

	public static void main(String args[]) {
		DesktopGame game = new DesktopGame();
		BasicAdventureModel adventure = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		BasicScene scene = new BasicScene();
		chapter.setInitialScene(scene);
		adventure.getChapters().add(chapter);
		game.setModel(null);
		game.start();

	}

}
