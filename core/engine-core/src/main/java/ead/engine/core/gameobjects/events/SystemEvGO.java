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

package ead.engine.core.gameobjects.events;

import java.util.logging.Logger;

import com.google.inject.Inject;

import ead.common.model.elements.events.SystemEv;
import ead.common.model.elements.events.enums.SystemEvType;
import ead.engine.core.game.Game;
import ead.engine.core.game.GameState;
import ead.engine.core.platform.assets.AssetHandler;

public class SystemEvGO extends AbstractEventGO<SystemEv> {

	private boolean triggered = false;
	
	private Logger logger = Logger.getLogger("SystemEvGO");

	private AssetHandler assetHandler;

	private Game game;
	
	@Inject
	public SystemEvGO(AssetHandler assetHandler, GameState gameState, Game game) {
		super(gameState);
		this.assetHandler = assetHandler;
		this.game = game;
	}

	@Override
	public void update() {
		// TODO probably not enough to just check for assets loaded
		logger.info("Checking if the game is loaded...");
		if (assetHandler.isLoaded() && game.getAdventureModel() != null && !triggered) {
			runEffects(element.getEffectsForEvent(SystemEvType.GAME_LOADED));
			triggered = true;
		}
	}

}
