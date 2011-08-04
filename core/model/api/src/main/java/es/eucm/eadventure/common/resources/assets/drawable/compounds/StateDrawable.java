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

package es.eucm.eadventure.common.resources.assets.drawable.compounds;

import java.util.Set;

import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

/**
 * Represents a drawable bundle, in which each drawable is associated with a
 * state defined by a {@link String}
 */
public interface StateDrawable extends Drawable {

	/**
	 * Returns a {@link Drawable} for a given state state.
	 * 
	 * @param stateName
	 *            Sate name for the animation
	 * @return a {@link Drawable} for a given state state.
	 */
	Drawable getDrawable(String stateName);

	/**
	 * Adds an animation to the bundle. This animation will be associated with
	 * the given state name
	 * 
	 * @param stateName
	 *            State name for the animation
	 * @param drawable
	 *            {@link Drawable} to be added to the bundle
	 * @return <b>true</b> if the bundle does not contain an animation for the
	 *         given state name. <b>false</b> otherwise
	 */
	boolean addDrawable(String stateName, Drawable drawable);

	/**
	 * Get the states in the drawable bundle
	 * 
	 * @return A set with all the states in the drawable bundle
	 */
	Set<String> getStates();

}
