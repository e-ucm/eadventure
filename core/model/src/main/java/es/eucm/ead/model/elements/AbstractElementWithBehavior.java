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

package es.eucm.ead.model.elements;

import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Evented;
import es.eucm.ead.model.interfaces.features.WithBehavior;
import es.eucm.ead.model.params.guievents.EAdGUIEvent;

/**
 * An abstract element with behavior, resources and events
 * 
 */
public abstract class AbstractElementWithBehavior extends BasicElement
		implements Evented, WithBehavior {

	/**
	 * Events associated with this element
	 */
	@Param
	protected es.eucm.ead.model.elements.extra.EAdList<Event> events;

	/**
	 * All behaviors contained by this bundle, associated with its events
	 */
	@Param
	protected EAdMap<EAdGUIEvent, EAdList<Effect>> behavior;

	/**
	 * Calculated attribute with all effects contained by the behavior. It must
	 * not be saved in the XML
	 */
	private transient EAdList<Effect> allEffects;

	/**
	 * Initial event for this element. This attribute WILL NOT BE SERIALIZED by
	 * the any writer. It is a transient attribute
	 */
	private transient SceneElementEv initEvent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.EAdElement#getEvents()
	 */
	@Override
	public EAdList<Event> getEvents() {
		return events;
	}

	@Override
	public void addEvent(Event e) {
		if (events == null) {
			events = new EAdList<Event>();
		}
		events.add(e);
	}

	public void setEvents(EAdList<Event> events) {
		this.events = events;
	}

	public void addAddedEffect(Effect e) {
		if (initEvent == null) {
			initEvent = new SceneElementEv();
			this.addEvent(initEvent);
		}
		initEvent.addEffect(SceneElementEvType.ADDED, e);
	}

	/**
	 * Adds an effect that will be executed only once when this scene element
	 * appears for the first time in the game
	 *
	 * @param e
	 *            the effect
	 */
	public void addInitEffect(Effect e) {
		if (initEvent == null) {
			initEvent = new SceneElementEv();
			this.addEvent(initEvent);
		}
		initEvent.addEffect(SceneElementEvType.INIT, e);
	}

	/**
	 * Adds an effect associated with an event
	 *
	 * @param event  the event
	 * @param effect the effect associated
	 */
	public void addBehavior(EAdGUIEvent event, Effect effect) {
		if (behavior == null) {
			behavior = new EAdMap<EAdGUIEvent, EAdList<Effect>>();
		}
		EAdList<Effect> list = behavior.get(event);
		if (list == null) {
			list = new EAdList<Effect>();
			behavior.put(event, list);
		}
		list.add(effect);
	}

	/**
	 * Returns a list with the attached effects to the given event, if exists.
	 * If not, returns {@code null}
	 *
	 * @param event the GUI event
	 * @return a list with the attached effects to the given event, if exists.
	 *         If not, returns {@code null}
	 */
	public EAdList<Effect> getEffects(EAdGUIEvent event) {
		return behavior == null ? null : behavior.get(event);
	}

	/**
	 * Adds an effect associated with an event
	 *
	 * @param event   the event
	 * @param effects a list of effects
	 */
	public void addBehavior(EAdGUIEvent event, EAdList<Effect> effects) {
		for (int i = effects.size() - 1; i >= 0; i--)
			addBehavior(event, effects.get(i));
	}

	public EAdMap<EAdGUIEvent, EAdList<Effect>> getBehavior() {
		return behavior;
	}

	/**
	 * Returns all the effects contained for this behavior. This list must NOT
	 * be modified
	 *
	 * @return
	 */
	public EAdList<Effect> getAllEffects() {
		if (allEffects == null) {
			allEffects = new EAdList<Effect>();
		}
		allEffects.clear();
		for (EAdList<Effect> l : behavior.values()) {
			allEffects.addAll(l);
		}

		return allEffects;
	}

	public void setBehavior(EAdMap<EAdGUIEvent, EAdList<Effect>> behavior) {
		this.behavior = behavior;
	}

	/**
	 * Returns if this behavior is empty
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return behavior.isEmpty();
	}
}
