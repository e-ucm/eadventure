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

package es.eucm.eadventure.common.model.elements.behaviors;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdBehavior;
import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdElementImpl;
import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.model.elements.extra.EAdListImpl;
import es.eucm.eadventure.common.model.elements.extra.EAdMap;
import es.eucm.eadventure.common.model.elements.extra.EAdMapImpl;
import es.eucm.eadventure.common.model.elements.guievents.EAdGUIEvent;

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
@Element(runtime = BehaviorImpl.class, detailed = BehaviorImpl.class)
public class BehaviorImpl extends EAdElementImpl implements EAdBehavior {

	/**
	 * All behaviors contained by this bundle, associated with its events
	 */
	@Param("behavior")
	protected EAdMap<EAdGUIEvent, EAdList<EAdEffect>> behavior;

	/**
	 * Constructs an empty behavior
	 * 
	 * @param parent
	 *            parent for this element
	 * @param id
	 *            id for this element
	 */
	public BehaviorImpl() {
		super();
		behavior = new EAdMapImpl<EAdGUIEvent, EAdList<EAdEffect>>(String.class, EAdList.class);
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
		EAdList<EAdEffect> list = behavior.get(event);
		if (list == null) {
			list = new EAdListImpl<EAdEffect>(EAdEffect.class);
			behavior.put(event, list);
		} 
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
	public EAdList<EAdEffect> getEffects(EAdGUIEvent event) {
		return behavior.get(event);
	}

	@Override
	public void addBehavior(EAdGUIEvent event, EAdList<EAdEffect> effects) {
		for (int i = effects.size() - 1; i >= 0; i--)
			addBehavior(event, effects.get(i));
	}
	
	public EAdMap<EAdGUIEvent, EAdList<EAdEffect>> getBehavior() {
		return behavior;
	}

}
