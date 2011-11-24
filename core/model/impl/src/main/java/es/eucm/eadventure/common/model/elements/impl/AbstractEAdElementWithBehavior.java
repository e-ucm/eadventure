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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.behavior.EAdBehavior;
import es.eucm.eadventure.common.model.behaviors.impl.EAdBehaviorImpl;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.guievents.EAdGUIEvent;
import es.eucm.eadventure.common.model.impl.ResourcedElementImpl;

/**
 * An abstract element with behavior, resources and events
 * 
 */
public abstract class AbstractEAdElementWithBehavior extends
		ResourcedElementImpl {

	@Param("behavior")
	protected EAdBehavior behavior;

	public AbstractEAdElementWithBehavior() {
		super();
		this.behavior = new EAdBehaviorImpl();
		behavior.setId(id + "behaviors");
	}

	public EAdList<EAdEffect> getEffects(EAdGUIEvent event) {
		return behavior.getEffects(event);
	}

	/**
	 * Adds a behavior to this actor attached to the given event
	 * 
	 * @param event
	 *            the event
	 * @param behavior
	 *            the effect
	 */
	public void addBehavior(EAdGUIEvent event, EAdEffect effect) {
		behavior.addBehavior(event, effect);
	}

	/**
	 * Adds the given effects to the list of the events that will be executed
	 * when the given event is processed by this element
	 * 
	 * @param event the GUI event
	 * @param effects the list of effects
	 */
	public void addBehavior(EAdGUIEvent event, EAdList<EAdEffect> effects) {
		behavior.addBehavior(event, effects);
	}

	/**
	 * Sets the behavior for this actor
	 * 
	 * @param behavior
	 *            the behavior
	 */
	public void setBehavior(EAdBehavior behavior) {
		this.behavior = behavior;
	}

	/**
	 * Returns the behavior for this actor
	 * 
	 * @return the behavior for this actor
	 */
	public EAdBehavior getBehavior() {
		return behavior;
	}

}
