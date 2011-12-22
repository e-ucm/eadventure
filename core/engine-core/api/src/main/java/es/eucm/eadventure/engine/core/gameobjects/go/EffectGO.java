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

package es.eucm.eadventure.engine.core.gameobjects.go;

import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;

public interface EffectGO<P extends EAdEffect> extends DrawableGO<P> {

	void initilize();

	/**
	 * 
	 * 
	 * @return true when the effect has a visual representation in the effect
	 *         HUD
	 */
	boolean isVisualEffect();

	/**
	 * Returns {@code true} when the effect is finished
	 * 
	 * @return {@code true} when the effect is finished
	 */
	boolean isFinished();

	/**
	 * Returns the effect attached to this game object
	 * 
	 * @return the effect attached to this game object
	 */
	P getEffect();

	/**
	 * Returns {@code true} if this effect blocks the next effects until this
	 * one is finished
	 * 
	 * @return {@code true} if this effect blocks the next effects until this
	 *         one is finished
	 */
	boolean isBlocking();

	/**
	 * If returns {@code true} means that GUI events will be only processed for
	 * this effect or those which are over it
	 * 
	 * @return
	 */
	boolean isOpaque();

	/**
	 * Runs the effect. Right after calling this method,
	 * {@link EffectGO#isStopped()} will return {@code false}
	 */
	void run();

	/**
	 * Stops the effect. Right after calling this method,
	 * {@link EffectGO#isStopped()} will return {@code true}
	 */
	void stop();

	/**
	 * Returns if the effect has been stopped by an external element
	 * 
	 * @return
	 */
	boolean isStopped();

	boolean isInitilized();

	/**
	 * Ends the effect
	 */
	void finish();

	/**
	 * Sets the gui action that launched this effect
	 * 
	 * @param gui
	 *            the gui action
	 */
	void setGUIAction(GUIAction gui);

	/**
	 * Sets the element launching the effect
	 * 
	 * @param parent
	 */
	void setParent(EAdSceneElement parent);

	boolean isQueueable();

}
