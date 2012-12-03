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

package ead.engine.core.gdx.html.platform;

import java.util.Map;

import com.google.inject.Inject;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.engine.core.game.GameLoader;
import ead.engine.core.gdx.GdxEngineImpl;
import ead.engine.core.gdx.platform.GdxCanvas;
import ead.engine.core.input.InputHandler;

public class GwtGdxEngine extends GdxEngineImpl {

	private GameLoader gameLoader;

	private EAdAdventureModel model;

	private Map<EAdString, String> strings;

	private Map<String, String> properties;

	@Inject
	public GwtGdxEngine(GdxCanvas canvas, InputHandler inputHandler) {
		super(canvas, inputHandler);
	}

	public void create() {
		super.create();
		if (model == null) {
			gameLoader.loadGameFromFiles("@data.xml", "@strings.xml",
					"@ead.properties");
		} else {
			gameLoader.loadGame(model, strings, properties);
		}
	}

	public void setGameLoader(GameLoader gameLoader) {
		this.gameLoader = gameLoader;
	}

	public void setGame(EAdAdventureModel model,
			Map<EAdString, String> strings, Map<String, String> properties) {
		this.model = model;
		this.strings = strings;
		this.properties = properties;
	}

}
