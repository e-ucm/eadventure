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

package es.eucm.ead.engine.tracking;

import com.badlogic.gdx.scenes.scene2d.Event;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.AdventureGame;

/**
 * General interface for game engine trackers. Methods defined by this interface
 * can be extended if required
 */
public interface GameTracker {

	/**
	 * Starts the tracking
	 *
	 * @param model the game model to be tracked
	 */
	void startTracking(AdventureGame model);

	/**
	 * Tracks an input action executed over the target
	 *
	 * @param action the performed action
	 * @param target the game object receiving the action
	 */
	void input(Event action, SceneElementGO target);

	/**
	 * Tracks a launched effect
	 *
	 * @param effect the launched effect
	 */
	void effect(EffectGO<?> effect);

	/**
	 * A phase starts
	 *
	 * @param phaseId the phase id
	 */
	void phaseStart(String phaseId);

	/**
	 * A phase ends
	 *
	 * @param phaseId the phase id
	 */
	void phaseEnd(String phaseId);

	/**
	 * A variable has been updated
	 *
	 * @param varId    variable id
	 * @param newValue new value for the variable
	 */
	void varUpdate(String varId, Object newValue);

	/**
	 * The game has ended
	 */
	void gameEnd();

	/**
	 * @return Returns if it is currently sending tracking data
	 */
	boolean isTracking();

	/**
	 * Stops the tracking
	 */
	void stop();

}
