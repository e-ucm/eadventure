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

package ead.engine.core.game.enginefilters;

import java.util.List;

import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.huds.BottomHUD;
import ead.engine.core.gameobjects.huds.EffectsHUD;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class HudsCreationFilter extends AbstractEngineFilter<List<HudGO>> {

	public HudsCreationFilter() {
		super(0);
	}

	@Override
	public List<HudGO> filter(List<HudGO> o, Object[] params) {
		AssetHandler assetHandler = (AssetHandler) params[0];
		SceneElementGOFactory sceneElementFactory = (SceneElementGOFactory) params[1];
		GUI gui = (GUI) params[2];
		GameState gameState = (GameState) params[3];
		EventGOFactory eventFactory = (EventGOFactory) params[4];
		Game g = (Game) params[5];
		// Bottom HUD
		o.add(new BottomHUD(assetHandler, sceneElementFactory, gui, gameState,
				eventFactory));

		// gui.addHud(inventoryHUD, 1);

		// Menu HUD
		MenuHUD menuHud = new MenuHUD(assetHandler, sceneElementFactory, gui,
				gameState, eventFactory);
		o.add(menuHud);
		g.addFilter(GameImpl.FILTER_PROCESS_ACTION, menuHud);

		// Effects HUD
		EffectsHUD effectHUD = new EffectsHUD(assetHandler,
				sceneElementFactory, gui, gameState, eventFactory);
		o.add(effectHUD);

		return o;
	}

}
