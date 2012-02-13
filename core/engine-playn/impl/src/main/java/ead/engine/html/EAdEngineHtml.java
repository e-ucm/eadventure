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

package ead.engine.html;

import playn.core.PlayN;
import playn.html.HtmlAssetManager;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import playn.html.HtmlPlatform.Mode;

import com.google.gwt.core.client.GWT;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.scene.EAdScene;
import ead.common.util.StringHandler;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.InitScene;
import ead.engine.core.EAdEngine;
import ead.engine.core.game.Game;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.PlayNGinInjector;

public class EAdEngineHtml extends HtmlGame {

	protected final static PlayNGinInjector injector = GWT
			.create(PlayNGinInjector.class);

	@Override
	public void start() {

		HtmlAssetManager assets = HtmlPlatform.register(Mode.AUTODETECT).assetManager();
		
		
		
		//FIXME This should be added when the cursor starts working correctly...
//		HtmlPlatform.setCursor(null);
		
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

		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		chapter.setId("chapter1");

		model.getChapters().add(chapter);

		EAdScene s2 = new InitScene();
		chapter.getScenes().add(s2);
		chapter.setInitialScene(s2);
//		model.setInventory(EAdElementsFactory.getInstance().getInventory());

		game.setGame(model, chapter);

		// String handler after creating the scene
		StringHandler stringHandler = injector.getStringHandler();
		stringHandler.addStrings(EAdElementsFactory.getInstance()
				.getStringFactory().getStrings());
		
		return game;
	}

}
