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

package es.eucm.eadventure.common.model.behaviors.impl;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.behavior.EAdBehavior;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.impl.EAdElementListImpl;
import es.eucm.eadventure.common.model.impl.EAdMapImpl;
import es.eucm.eadventure.common.model.params.guievents.EAdGUIEvent;

/**
 * 
 * <p>
 * Data model implementation for behaviors
 * </p>
 * <p>
 * Behaviors provide a mapping between actions in the game GUI (
 * {@link EAdGUIEvent}) and {@link EAdEffect}s within the game.
 * </p>
 * 
 */
@Element(runtime = EAdBehaviorImpl.class, detailed = EAdBehaviorImpl.class)
public class EAdBehaviorImpl extends AbstractEAdElement implements EAdBehavior {

	/**
	 * All behaviors contained by this bundle, associated with its events
	 */
	protected EAdMap<String, EAdElementList<EAdEffect>> behavior;

	/**
	 * Constructs an empty behavior
	 * 
	 * @param parent
	 *            parent for this element
	 * @param id
	 *            id for this element
	 */
	public EAdBehaviorImpl(String id) {
		super(id);
		behavior = new EAdMapImpl<String, EAdElementList<EAdEffect>>(id
				+ "_behavior", String.class, EAdElementList.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.model.behavior.EAdBehavior#addBehavior(es.eucm
	 * .eadventure.common.model.params.guievents.EAdGUIEvent,
	 * es.eucm.eadventure.common.model.effects.EAdEffect)
	 */
	@Override
	public void addBehavior(EAdGUIEvent event, EAdEffect effect) {
		EAdElementList<EAdEffect> list = behavior.get(event.toString());
		if (list == null) {
			list = new EAdElementListImpl<EAdEffect>();
			list.add(effect);
			behavior.put(event.toString(), list);
		} else
			list.add(effect);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.common.model.behavior.EAdBehavior#getEffects(es.eucm
	 * .eadventure.common.model.params.guievents.EAdGUIEvent)
	 */
	@Override
	public EAdElementList<EAdEffect> getEffects(EAdGUIEvent event) {
		return behavior.get(event.toString());
	}

	@Override
	public void addBehavior(EAdGUIEvent event, EAdElementList<EAdEffect> effects) {
		for (int i = effects.size() - 1; i >= 0; i--)
			addBehavior(event, effects.get(i));
	}

}
