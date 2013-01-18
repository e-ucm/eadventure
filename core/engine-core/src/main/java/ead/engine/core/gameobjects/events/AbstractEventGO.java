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

package ead.engine.core.gameobjects.events;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.GameObject;
import ead.engine.core.gameobjects.GameObjectImpl;

/**
 * Basic game object for events
 * 
 * @param <T>
 */
public abstract class AbstractEventGO<T extends EAdEvent> extends
		GameObjectImpl<T> implements GameObject<T>, EventGO<T> {

	/**
	 * Event parent (the element who launched the event)
	 */
	protected EAdSceneElement parent;

	protected GameState gameState;

	public AbstractEventGO(GameState gameState) {
		this.gameState = gameState;
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
	public void setParent(EAdSceneElement parent) {
		this.parent = parent;
	}

	protected void runEffects(EAdList<EAdEffect> effects) {
		if (effects != null) {
			for (EAdEffect effect : effects)
				gameState.addEffect(effect, null, parent);
		}

	}

}
