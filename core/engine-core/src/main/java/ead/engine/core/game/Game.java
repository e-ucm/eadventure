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

package ead.engine.core.game;


import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.extra.EAdList;
import ead.engine.core.util.EAdTransformation;

/**
 * Main game interface. Include the methods to update the game state, render the
 * current game state, evaluate conditions according to the current game state,
 * etc.
 */
public interface Game {

	/**
	 * Updates the game state. It mostly processes the pending actions for the
	 * model and recreates a representation of the game.
	 */
	void update();

	/**
	 * Renders the game to the screen.
	 * 
	 * @param interpolation
	 *            The interpolation factor between game update cycles, allows
	 *            for smother animations when the frame updates are faster than
	 *            the model updates.
	 */
	void render(float interpolation);

	/**
	 * Returns the current adventure game model ({@link EAdAdventureModel})
	 * 
	 * @return The adventure game model
	 */
	EAdAdventureModel getAdventureModel();

	/**
	 * Load the game
	 */
	void loadGame();

	void setGame(EAdAdventureModel model, EAdChapter eAdChapter);

	/**
	 * Loads the given game
	 * 
	 * @param model
	 * @param chpater
	 * @param initialEffects
	 *            the effects to be launched after the first scene of the game
	 *            is launched
	 */
	void setGame(EAdAdventureModel model, EAdChapter chpater,
			EAdList<EAdEffect> initialEffects);

	void updateInitialTransformation();

	EAdTransformation getInitialTransformation();

	EAdChapter getCurrentChapter();

	void setGameLoader(GameLoader gameLoader);

}
