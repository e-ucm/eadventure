package ead.demos.techdemo;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.scenes.InitScene;
import ead.engine.core.gdx.GdxEngine;
import ead.engine.core.gdx.html.GwtLauncher;
import ead.engine.core.gdx.html.platform.GwtGdxEngine;

public class TechDemoLauncher extends GwtLauncher {

	@Override
	public ApplicationListener getApplicationListener() {
		GdxEngine engine = (GdxEngine) super.getApplicationListener();
		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);
		((GwtGdxEngine) engine).setGame(model, EAdElementsFactory.getInstance()
				.getStringFactory().getStrings(), new HashMap<String, String>());

		return engine;
	}

}
