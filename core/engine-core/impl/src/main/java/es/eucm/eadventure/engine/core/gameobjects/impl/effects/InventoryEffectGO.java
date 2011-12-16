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

package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdInventoryEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.inventory.InventoryHandler;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

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
public class InventoryEffectGO extends AbstractEffectGO<EAdInventoryEffect> {

	private InventoryHandler inventoryHandler;

	@Inject
	public InventoryEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, InventoryHandler inventoryHandler) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.inventoryHandler = inventoryHandler;
	}

	@Override
	public void initilize() {
		super.initilize();
		switch (element.getModification()) {
		case ADD_TO_INVENTORY:
			inventoryHandler.add(element.getSceneElementDef());
			if (element.isRemoveFromScene()) {
				EAdSceneElement sceneElement = gameState.getValueMap()
						.getValue(element.getSceneElementDef(),
								EAdSceneElementDefImpl.VAR_SCENE_ELEMENT);
				gameState.getValueMap().setValue(sceneElement,
						EAdBasicSceneElement.VAR_VISIBLE, false);
				gameState.getValueMap().setValue(sceneElement,
						EAdBasicSceneElement.VAR_ENABLE, true);
			}
			break;
		case REMOVE_FROM_INVENTORY:
			inventoryHandler.remove(element.getSceneElementDef());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.EffectGO#isVisualEffect()
	 */
	@Override
	public boolean isVisualEffect() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.EffectGO#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return true;
	}

}
