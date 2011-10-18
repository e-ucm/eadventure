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

import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
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
public class ModifyActorStateGO extends AbstractEffectGO<EAdModifyActorState> {

	@Inject
	public ModifyActorStateGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#update
	 * (es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update() {
		super.update();
		switch (element.getModification()) {
		case PLACE_IN_INVENTORY:
			gameState.getRemovedActors().remove(element.getActor());
			if (!gameState.getInventoryActors().contains(element.getActor()))
				gameState.getInventoryActors().add(element.getActor());
			break;
		case PLACE_IN_SCENE:
			gameState.getRemovedActors().remove(element.getActor());
			gameState.getInventoryActors().remove(element.getActor());
			break;
		case REMOVE_SCENE_AND_INVENTORY:
			if (!gameState.getRemovedActors().contains(element.getActor()))
				gameState.getRemovedActors().add(element.getActor());
			gameState.getInventoryActors().remove(element.getActor());
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
