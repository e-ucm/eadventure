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

package es.eucm.ead.engine.gameobjects.events;

import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.GameObject;
import es.eucm.ead.engine.gameobjects.GameObjectImpl;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.scenes.SceneElement;

/**
 * Basic game object for events
 * 
 * @param <T>
 */
public abstract class AbstractEventGO<T extends Event> extends
		GameObjectImpl<T> implements GameObject<T>, EventGO<T> {

	/**
	 * Event parent (the element who launched the event)
	 */
	protected SceneElement parent;

	protected Game game;

	public AbstractEventGO(Game game) {
		this.game = game;
	}

	/**
	 * Initializes the event
	 */
	public void initialize() {

	}

	/**
	 * Sets the element holding this event
	 * 
	 * @param parent
	 *            the scene element holding this event
	 */
	public void setParent(SceneElement parent) {
		this.parent = parent;
	}

	protected void runEffects(EAdList<Effect> effects) {
		if (effects != null) {
			for (Effect effect : effects)
				game.addEffect(effect, null, parent);
		}
	}

}
