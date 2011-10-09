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

package es.eucm.eadventure.engine.core.test.launcher;

import java.util.Map;

import com.google.inject.Injector;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;

/**
 * Base test launcher
 * 
 * 
 */
public abstract class BaseTestLauncher {

	private PlatformLauncher launcher;
	
	private EAdAdventureModel model;

	private EAdChapterImpl c;
	
	public BaseTestLauncher(Injector injector){
		launcher = injector.getInstance(PlatformLauncher.class);
		model = new EAdAdventureModelImpl();
		c = new EAdChapterImpl("chapter1");
		model.getChapters().add(c);
	}

	public BaseTestLauncher(Injector injector, EAdScene scene) {
		this( injector );
		c.getScenes().add(scene);
		c.setInitialScene(scene);
		Game game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));
		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.addStrings(EAdElementsFactory.getInstance().getStringFactory().getStrings());
	}
	
	public BaseTestLauncher(Injector injector, EAdAdventureModel model, Map<EAdString, String> strings){
		this( injector );
		Game game = injector.getInstance(Game.class);
		game.setGame(model, model.getChapters().get(0));
		StringHandler stringHandler = injector.getInstance(StringHandler.class);
		stringHandler.setStrings(strings);
	}

	/**
	 * Launches the test
	 */
	public void start() {
		launcher.launch(null);
	}

}
