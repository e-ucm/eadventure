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

package es.eucm.eadventure.common.model.elements;

import java.util.List;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.interfaces.Positioned;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.variables.EAdVar;

public interface EAdSceneElement extends EAdElement, Positioned {

	/**
	 * Gets the scale of the scene element.
	 * 
	 * @return the scale value.
	 */
	EAdVar<Float> scaleVar();

	/**
	 * Returns the runtime variable holding the x position of the scene element
	 * 
	 * @return the runtime variable holding the x position of the scene element
	 */
	EAdVar<Integer> positionXVar();

	/**
	 * Returns the runtime variable holding the y position of the scene element
	 * 
	 * @return the runtime variable holding the y position of the scene element
	 */
	EAdVar<Integer> positionYVar();

	/**
	 * Returns the runtime variable associated to the visibility of the element
	 * 
	 * @return
	 */
	EAdVar<Boolean> visibleVar();

	/**
	 * Returns the runtime variable holding the width of the scene element,
	 * during game
	 * 
	 * @return
	 */
	EAdVar<Integer> widthVar();

	/**
	 * Returns the runtime variable holding the height of the scene element,
	 * during game
	 * 
	 * @return
	 */
	EAdVar<Integer> heightVar();

	/**
	 * Returns the runtime variable holding the rotation of the scene element
	 * 
	 * @return
	 */
	EAdVar<Float> rotationVar();

	/**
	 * Returns the runtime variable holding the alpha for the scene element
	 * 
	 * @return
	 */
	EAdVar<Float> alphaVar();

	/**
	 * Returns the var holding the state name of the element
	 * 
	 * @return the state var
	 */
	EAdVar<String> stateVar();

	/**
	 * Returns the effects list associated with the given GUI event,
	 * {@code null} if there is no effects associated. This method shouldn't be
	 * used to add new effects to the actor. Returned list could be {@code null}
	 * 
	 * @param event
	 *            the GUI event
	 * @return the effects list associated with the given event
	 */
	EAdList<EAdEffect> getEffects(EAdGUIEvent event);

	/**
	 * Returns true if this scene element must be cloned whenever is added to
	 * the game. This means that all its variables will be set with its initial
	 * values, instead of storing their last values
	 * 
	 * @return if this element must be cloned whenever is added to the game
	 */
	boolean isClone();

	/**
	 * Returns the orientation var
	 * 
	 * @return
	 */
	EAdVar<Orientation> orientationVar();

	/**
	 * Returns a list of all vars of this scene element
	 * 
	 * @return a list of all vars of this scene element
	 */
	List<EAdVar<?>> getVars();

}
