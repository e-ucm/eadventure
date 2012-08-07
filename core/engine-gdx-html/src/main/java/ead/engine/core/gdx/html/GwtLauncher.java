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

package ead.engine.core.gdx.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.core.client.GWT;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.InitScene;
import ead.engine.core.game.Game;
import ead.engine.core.gdx.EAdEngine;
import ead.engine.core.gdx.html.tools.GdxGinInjector;
import ead.tools.StringHandler;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800,
				600);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		GdxGinInjector injector = GWT.create(GdxGinInjector.class);

		EAdEngine engine = new EAdEngine(injector.getEngineConfiguration(),
				injector.getCanvas(), injector.getInputHandler());
		Game g = injector.getGame();

		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter();
		chapter.setInitialScene(scene);

		BasicAdventureModel adventure = new BasicAdventureModel();
		adventure.getChapters().add(chapter);

		g.setGame(adventure, chapter);

		StringHandler stringHandler = injector.getStringHandler();
		stringHandler.addStrings(EAdElementsFactory.getInstance()
				.getStringFactory().getStrings());

		engine.setGame(g);
		return engine;
	}
}