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

import java.util.List;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.EAdEvent;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;

/**
 * <p>
 * Abstract implementation of an eAdventure event.
 * </p>
 * 
 */
public abstract class AbstractEvent extends BasicElement implements EAdEvent {

	/**
	 * List of effects
	 */
	@Param
	private es.eucm.ead.model.elements.extra.EAdMap<Enum<?>, EAdList<EAdEffect>> effects;

	/**
	 * Calculated attribute with all effects contained by the behavior. It must
	 * not be saved in the XML
	 */
	private transient EAdList<EAdEffect> allEffects;

	public AbstractEvent() {
		super();
		effects = new EAdMap<Enum<?>, EAdList<EAdEffect>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.common.model.elements.EAdEvent#getEffects()
	 */
	@Override
	public EAdList<EAdEffect> getEffectsForEvent(Enum<?> event) {
		return effects.get(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.model.events.EAdEvent#addEffect(java.lang.Enum,
	 * es.eucm.eadventure.common.model.effects.EAdEffect)
	 */
	@Override
	public void addEffect(Enum<?> event, EAdEffect effect) {
		EAdList<EAdEffect> effects = this.effects.get(event);
		if (effects == null) {
			effects = new EAdList<EAdEffect>();
			this.effects.put(event, effects);
		}
		effects.add(effect);
	}

	public void addEffects(Enum<?> event, List<EAdEffect> effects) {
		for (EAdEffect e : effects) {
			addEffect(event, e);
		}
	}

	public EAdMap<Enum<?>, EAdList<EAdEffect>> getEffects() {
		return effects;
	}

	public EAdList<EAdEffect> getAllEffects() {
		if (allEffects == null) {
			allEffects = new EAdList<EAdEffect>();
			for (EAdList<EAdEffect> l : effects.values()) {
				allEffects.addAll(l);
			}
		}
		return allEffects;
	}

	public void setEffects(EAdMap<Enum<?>, EAdList<EAdEffect>> effects) {
		this.effects = effects;
	}

	public void setAllEffects(EAdList<EAdEffect> allEffects) {
		this.allEffects = allEffects;
	}

}
