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

package ead.engine.core.gameobjects.huds;

import com.google.inject.Inject;

import ead.common.model.params.variables.SystemFields;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

/**
 * 
 * This is the first HUD to be drawn. It prevents user interaction with the
 * scene when {@link SystemFields#BASIC_HUD_OPAQUE} is true
 * 
 */
public class BottomHUD extends AbstractHUD {

	public static final String ID = "BottomHUD";

	@Inject
	public BottomHUD(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(ID, assetHandler, gameObjectFactory, gui, gameState,
				eventFactory, 0);
	}

	@Override
	public SceneElementGO<?> processAction(InputAction<?> action) {
		if (gameState.getValue(SystemFields.BASIC_HUD_OPAQUE)) {
			action.consume();
			return this;
		}
		return null;
	}

	@Override
	public boolean contains(int x, int y) {
		return gameState.getValue(SystemFields.BASIC_HUD_OPAQUE);
	}

	@Override
	public void init() {

	}

	public void doLayout() {

	}

}
