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

package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.inventory.InventoryHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

/**
 * <p>
 * Game object for the {@link EAdModifiyActorState} effect
 * </p>
 * <p>
 * This effect places the actor in the appropriate list in the game state, be it
 * the actors in the inventory, the actors removed from the scene and the
 * inventory or no list at all.
 * </p>
 * 
 */
public class ModifyInventoryGO extends AbstractEffectGO<ModifyInventoryEf> {

	private InventoryHandler inventoryHandler;

	@Inject
	public ModifyInventoryGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementGOFactory, GUI gui,
			GameState gameState, InventoryHandler inventoryHandler,
			EventGOFactory eventFactory) {
		super(gameState);
		this.inventoryHandler = inventoryHandler;
	}

	@Override
	public void initialize() {
		super.initialize();
		switch (effect.getModification()) {
		case ADD_TO_INVENTORY:
			inventoryHandler.add(effect.getSceneElementDef());
			if (effect.isRemoveFromScene()) {
				EAdSceneElement sceneElement = gameState.getValue(effect
						.getSceneElementDef(),
						SceneElementDef.VAR_SCENE_ELEMENT);
				if (sceneElement != null) {
					gameState.setValue(sceneElement, SceneElement.VAR_VISIBLE,
							false);
					gameState.setValue(sceneElement, SceneElement.VAR_ENABLE,
							true);
				}
			}
			break;
		case REMOVE_FROM_INVENTORY:
			inventoryHandler.remove(effect.getSceneElementDef());
			break;
		}
	}

}
