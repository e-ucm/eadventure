package es.eucm.eadventure.engine.html;

import playn.core.PlayN;
import playn.html.HtmlAssetManager;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import playn.html.HtmlPlatform.Mode;

import com.google.gwt.core.client.GWT;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elementfactories.scenedemos.InitScene;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.engine.core.EAdEngine;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGinInjector;
import es.eucm.eadventure.engine.reader.GWTStringReader;

public class EAdEngineHtml extends HtmlGame {

	protected final static PlayNGinInjector injector = GWT
			.create(PlayNGinInjector.class);

	@Override
	public void start() {

		HtmlAssetManager assets = HtmlPlatform.register(Mode.WEBGL).assetManager();
		
		//FIXME This should be added when the cursor starts working correctly...
		//HtmlPlatform.setCursor(null);
		
		assets.setPathPrefix("");

		injector.getPlatformLauncher();
		injector.getPlayNInjector().setInjector(injector);

		
		Game game = loadGame();

		GUI gui = injector.getGUI();

		PlayN.run(new EAdEngine(game, gui, injector.getAssetHandler(), injector
				.getMouseState(), injector.getPlatformConfiguration()));
	}
	
	public Game loadGame() {
		Game game = injector.getGame();
		game.loadGame();

		EAdAdventureModel model = new EAdAdventureModelImpl();
		EAdChapterImpl chapter = new EAdChapterImpl();
		chapter.setId("chapter1");

		model.getChapters().add(chapter);
		EAdScene s = new EAdSceneImpl();
		s.setId("scene");
		Image i = new ImageImpl("@drawable/background1.png");
		s.getBackground().getDefinition()
				.getResources()
				.addAsset(s.getBackground().getDefinition().getInitialBundle(),
						EAdSceneElementDefImpl.appearance, i);

		EAdScene s2 = new InitScene();

		/*
		EAdScene s2 = new InventoryScene();

		if (EAdElementsFactory.getInstance().getInventory() != null)
			model.setInventory(EAdElementsFactory.getInstance().getInventory());
		 */
		
		// EAdScene s2 = new WebMVideoScene();

		// EAdScene s2 = new ShapeScene();

		//EAdScene s2 = new SpeakAndMoveScene();
		/*
		 * getBackground().getResources().addAsset(getBackground().getInitialBundle
		 * (), EAdBasicSceneElement.appearance, new
		 * ImageImpl("@drawable/loading.png"));
		 */
		chapter.getScenes().add(s2);
		chapter.setInitialScene(s2);

		game.setGame(model, chapter);

		// String handler after creating the scene
		StringHandler stringHandler = injector.getStringHandler();
		stringHandler.addStrings(EAdElementsFactory.getInstance()
				.getStringFactory().getStrings());
		GWTStringReader stringReader = new GWTStringReader();
		
		return game;
	}

}
