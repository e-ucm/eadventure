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

package ead.engine.core.test.launcher;

import java.util.Map;

import com.google.inject.Injector;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;
import ead.engine.core.game.Game;
import ead.engine.core.platform.PlatformLauncher;

/**
 * Base test launcher
 * 
 * 
 */
public abstract class BaseTestLauncher {

	private PlatformLauncher launcher;

	public BaseTestLauncher(Injector injector, EAdAdventureModel model,
			Map<EAdString, String> strings) {
		launcher = injector.getInstance(PlatformLauncher.class);
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
