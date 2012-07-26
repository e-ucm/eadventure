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

package ead.engine.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.EAdAdventureModel;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.InitScene;
import ead.engine.core.game.Game;
import ead.engine.core.platform.GUI;
import ead.tools.StringHandler;
import ead.tools.java.JavaInjector;
import ead.tools.java.JavaToolsModule;

public class Main {
	public static void main(String[] args) {

		Injector i = Guice.createInjector(new GdxModule(),
				new JavaToolsModule());
		JavaInjector injector = new JavaInjector(i);
		GUI gui = injector.getInstance(GUI.class);		
		gui.initialize();
		
		InitScene initScene = new InitScene();
		EAdAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter = new BasicChapter();
		chapter.setInitialScene(initScene);
		model.getChapters().add(chapter);
		
		injector.getInstance(StringHandler.class).addStrings(
				EAdElementsFactory.getInstance().getStringFactory()
						.getStrings());
		
		Game game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));

	}



}
