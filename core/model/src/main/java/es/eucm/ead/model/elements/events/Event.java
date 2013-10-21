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

package es.eucm.ead.model.elements.events;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.Param;

import java.util.List;

/**
 * <p>
 * Abstract implementation of an eAdventure event.
 * </p>
 * 
 */
public abstract class Event extends BasicElement {

	/**
	 * List of effects
	 */
	@Param
	private es.eucm.ead.model.elements.extra.EAdMap<Enum<?>, EAdList<Effect>> effects;

	/**
	 * Calculated attribute with all effects contained by the behavior. It must
	 * not be saved in the XML
	 */
	private transient EAdList<Effect> allEffects;

	public Event() {
		super();
		effects = new EAdMap<Enum<?>, EAdList<Effect>>();
	}

	/**
	 * The list of effects triggered with the event
	 *
	 * @return The list of effects
	 */
	public EAdList<Effect> getEffectsForEvent(Enum<?> event) {
		return effects.get(event);
	}

	public void addEffect(Enum<?> event, Effect effect) {
		EAdList<Effect> effects = this.effects.get(event);
		if (effects == null) {
			effects = new EAdList<Effect>();
			this.effects.put(event, effects);
		}
		effects.add(effect);
	}

	public void addEffects(Enum<?> event, List<Effect> effects) {
		for (Effect e : effects) {
			addEffect(event, e);
		}
	}

	public EAdMap<Enum<?>, EAdList<Effect>> getEffects() {
		return effects;
	}

	/**
	 * Returns all the effects that can be produced by this element. This list
	 * must NOT be modified
	 *
	 * @return
	 */
	public EAdList<Effect> getAllEffects() {
		if (allEffects == null) {
			allEffects = new EAdList<Effect>();
			for (EAdList<Effect> l : effects.values()) {
				allEffects.addAll(l);
			}
		}
		return allEffects;
	}

	public void setEffects(EAdMap<Enum<?>, EAdList<Effect>> effects) {
		this.effects = effects;
	}

	public void setAllEffects(EAdList<Effect> allEffects) {
		this.allEffects = allEffects;
	}

}
