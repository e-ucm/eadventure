package ead.demos.techdemo;

import java.util.HashMap;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.scenes.InitScene;
import ead.engine.core.gdx.desktop.DesktopGame;

public class TechDemoMain {

	public static void main(String[] args) {
		DesktopGame g = new DesktopGame();
		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);
		g.load(model, EAdElementsFactory.getInstance().getStringFactory()
				.getStrings(), new HashMap<String, String>());

	}

}
