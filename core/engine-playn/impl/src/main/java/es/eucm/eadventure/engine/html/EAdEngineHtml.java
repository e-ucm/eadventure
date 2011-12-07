/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

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
