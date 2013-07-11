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

package ead.common.model.elements;

import ead.common.interfaces.features.Conditioned;
import ead.common.model.elements.extra.EAdList;

/**
 * <p>
 * An effect in the eAdventure model represents any set of actions to be taken
 * in the game, both modifying the runtime model (e.g. increasing a variable) or
 * modifying the view (e.g. starting an animation).
 * </p>
 * 
 */
public interface EAdEffect extends EAdElement, Conditioned {

	/**
	 * Returns the effects to be launched when this effect ends
	 * 
	 * @return
	 */
	EAdList<EAdEffect> getNextEffects();

	/**
	 * Adds a effect to be executed when this effect ends
	 * 
	 * @param e
	 *            next effect
	 */
	void addNextEffect(EAdEffect e);

	/**
	 * Returns the effects to be launched when this effect is launched
	 * 
	 * @return
	 */
	EAdList<EAdEffect> getSimultaneousEffects();

	/**
	 * Adds an effect to be launched just before this effect is launched
	 * 
	 * @param e
	 */
	void addSimultaneousEffect(EAdEffect e);

	/**
	 * Sets if the effects in the next effects list are launched even when the
	 * condition for the event is not fulfilled
	 * 
	 * @param always
	 */
	void setNextEffectsAlways(boolean always);

	/**
	 * Returns if the effects in the next effects list must be launched when the
	 * effect's conditions is not fulfilled
	 * 
	 * @return
	 */
	boolean isNextEffectsAlways();

	/**
	 * Indicates if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	boolean isPersistent();

	/**
	 * Sets if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	void setPersistent(boolean persistent);

}
