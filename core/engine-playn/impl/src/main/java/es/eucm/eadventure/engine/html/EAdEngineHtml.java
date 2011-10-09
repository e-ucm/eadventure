package es.eucm.eadventure.engine.html;

import playn.core.PlayN;
import playn.html.HtmlAssetManager;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.google.gwt.core.client.GWT;

import es.eucm.eadventure.common.elementfactories.scenedemos.CharacterScene;
import es.eucm.eadventure.common.elementfactories.scenedemos.ComplexElementScene;
import es.eucm.eadventure.common.elementfactories.scenedemos.SceneDemos;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdCutscene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.engine.core.EAdEngine;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGinInjector;

public class EAdEngineHtml extends HtmlGame {

	private final static PlayNGinInjector injector = GWT.create(PlayNGinInjector.class);

	@Override
	public void start() {
	    HtmlAssetManager assets = HtmlPlatform.register().assetManager();
	    assets.setPathPrefix("eadengine/");

	    injector.getPlatformLauncher();
	    Game game = injector.getGame();
	    game.loadGame();
	    
	    EAdAdventureModel model = new EAdAdventureModelImpl();
		EAdChapterImpl chapter = new EAdChapterImpl("chapter1");

		model.getChapters().add(chapter);
		EAdScene s = new EAdSceneImpl("scene");
		Image i = new ImageImpl("@drawable/background1.png");
		s.getBackground().getResources().addAsset(s.getBackground().getInitialBundle(),
				EAdBasicSceneElement.appearance, i);
		
		EAdScene s2 = new CharacterScene();
		
//		EAdScene s2 = new ComplexElementScene();
/*
		getBackground().getResources().addAsset(getBackground().getInitialBundle(), EAdBasicSceneElement.appearance,
				new ImageImpl("@drawable/loading.png"));
*/
		chapter.getScenes().add(s2);
		chapter.setInitialScene(s2);
	    
		game.setGame(model, chapter);

	    GUI gui = injector.getGUI();

	    PlayN.run(new EAdEngine(game, gui, injector.getAssetHandler()));
	}

}
