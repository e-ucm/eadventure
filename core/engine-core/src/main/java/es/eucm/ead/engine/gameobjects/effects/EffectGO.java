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

package es.eucm.ead.engine.gameobjects.effects;

import com.badlogic.gdx.scenes.scene2d.Event;
import es.eucm.ead.engine.gameobjects.GameObject;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.scenes.SceneElement;

public interface EffectGO<P extends Effect> extends GameObject<P> {

	/**
	 * Sets the gui action that launched this effect
	 * 
	 * @param gui
	 *            the gui action
	 */
	void setGUIAction(Event gui);

	/**
	 * Sets the element launching the effect
	 * 
	 * @param parent
	 */
	void setParent(SceneElement parent);

	/**
	 * Initializes the effect game object
	 */
	void initialize();

	/**
	 * Returns {@code true} when the effect is finished
	 * 
	 * @return {@code true} when the effect is finished
	 */
	boolean isFinished();

	/**
	 * Returns {@code true} if this effect blocks the next effects until this
	 * one is finished
	 * 
	 * @return {@code true} if this effect blocks the next effects until this
	 *         one is finished
	 */
	boolean isBlocking();

	/**
	 * Ends the effect normally
	 */
	void finish();

	/**
	 * Stops the effect. Right after calling this method,
	 * {@link EffectGO#isStopped()} will return {@code true}. Use this method to
	 * end the effect in abnormal circumstances (to avoid the execution of the
	 * final effects, for example)
	 */
	void stop();

	/**
	 * Returns if the effect has been stopped by an external element
	 * 
	 * @return
	 */
	boolean isStopped();

	/**
	 * Returns if this effects takes more than one tick to execute
	 * 
	 * @return
	 */
	boolean isQueueable();

}
