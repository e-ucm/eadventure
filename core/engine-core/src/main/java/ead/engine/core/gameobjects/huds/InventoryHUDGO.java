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

import ead.common.model.elements.huds.InventoryHud;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class InventoryHUDGO extends SceneElementGOImpl {

	private enum InventoryState {
		HIDDEN, GOING_UP, GOING_DOWN, SHOWN
	};

	private static final int TIME_TO_SHOW = 500;

	private int maxY;

	private int height;

	private int dispY;

	private int initialY;

	private InventoryState currentState;

	public InventoryHUDGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EvaluatorFactory evaluatorFactory,
			EventGOFactory eventFactory) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
	}

	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		height = ((InventoryHud) element).getHeight();
		maxY = gameState.getValue(SystemFields.GAME_HEIGHT);
		maxY = maxY - maxY / 10;
		currentState = InventoryState.HIDDEN;
		dispY = 0;
		initialY = (Integer) element.getVars().get(SceneElement.VAR_X);
	}

	public void update() {
		super.update();
		int y = gameState.getValue(SystemFields.MOUSE_Y);

		if (y > maxY) {
			switch (currentState) {
			case HIDDEN:
			case GOING_DOWN:
				currentState = InventoryState.GOING_UP;
				break;
			default:
				break;
			}
		} else {
			switch (currentState) {
			case SHOWN:
				currentState = InventoryState.GOING_DOWN;
			default:
				break;
			}
		}

		boolean changed = false;
		switch (currentState) {
		case GOING_DOWN:
			dispY += TIME_TO_SHOW * gui.getSkippedMilliseconds();
			changed = true;
			break;
		case GOING_UP:
			dispY -= TIME_TO_SHOW * gui.getSkippedMilliseconds();
			changed = true;
			break;
		default:
			break;
		}

		if (changed) {
			setY(initialY + dispY);
		}

	}

}
