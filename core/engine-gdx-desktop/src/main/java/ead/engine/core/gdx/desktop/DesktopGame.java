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

package ead.engine.core.gdx.desktop;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Guice;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.common.util.EAdURI;
import ead.engine.core.game.GameLoader;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.engine.core.platform.assets.AssetHandler;
import ead.tools.java.JavaInjector;
import ead.tools.java.JavaToolsModule;

public class DesktopGame extends JavaInjector {

	public DesktopGame() {
		super(Guice.createInjector(new GdxDesktopModule(),
				new JavaToolsModule()));
	}

	public void load(String dataFile, String stringsFile, String propertiesFile) {
		GameLoader g = getInstance(GameLoader.class);
		g.loadGameFromFiles(dataFile, stringsFile, propertiesFile);
	}

	/**
	 * Loads a game from its model, with strings and properties.
	 * 
	 * @param model
	 *            the model
	 * @param strings
	 *            the game strings. It can be {@code null}
	 * @param properties
	 *            the game properties. It can be {@code null}
	 */
	public void load(EAdAdventureModel model, Map<EAdString, String> strings,
			Map<String, String> properties) {
		GameLoader g = getInstance(GameLoader.class);
		if (strings == null) {
			strings = new HashMap<EAdString, String>();
		}

		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		g.loadGame(model, strings, properties);
	}

	public void setResourcesLocation(String path) {
		this.getInstance(AssetHandler.class).setResourcesLocation(
				new EAdURI(path));
	}

}
