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

package ead.engine.core.gdx.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.inject.Guice;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.InitScene;
import ead.engine.core.game.Game;
import ead.engine.core.gdx.EAdEngine;
import ead.tools.StringHandler;
import ead.tools.java.JavaInjector;
import ead.tools.java.JavaToolsModule;

public class MainActivity extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		JavaInjector injector = new JavaInjector(Guice.createInjector(
				new GdxAndroidModule(), new JavaToolsModule()));

		EAdEngine engine = injector.getInstance(EAdEngine.class);
		Game g = injector.getInstance(Game.class);

		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter();
		chapter.setInitialScene(scene);

		BasicAdventureModel adventure = new BasicAdventureModel();
		adventure.getChapters().add(chapter);

		g.setGame(adventure, chapter);
		
		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.addStrings(EAdElementsFactory.getInstance().getStringFactory().getStrings());
		
		engine.setGame(g);
		initialize(engine, cfg);
	}
}