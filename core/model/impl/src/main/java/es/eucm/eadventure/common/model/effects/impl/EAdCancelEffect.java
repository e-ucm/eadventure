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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.impl.EAdListImpl;

/**
 * This effects cancels other effects
 * 
 */
@Element(runtime = EAdCancelEffect.class, detailed = EAdCancelEffect.class)
public class EAdCancelEffect extends AbstractEAdEffect {

	/**
	 * Effect to be stopped
	 */
	private EAdList<EAdEffect> effects;

	/**
	 * Creates a cancel effect. If it's not added any effect with
	 * {@link EAdCancelEffect#addEffect(EAdEffect)}, then this effect will
	 * cancel all running effects
	 * 
	 * @param id
	 *            Element's id
	 */
	public EAdCancelEffect(String id) {
		super(id);
		effects = new EAdListImpl<EAdEffect>(EAdEffect.class);
	}

	/**
	 * Adds an effect to be canceled
	 * 
	 * @param effect
	 *            the effect
	 */
	public void addEffect(EAdEffect effect) {
		effects.add(effect);
	}

	/**
	 * Returns the effects to be canceled. If the list is empty, must be
	 * interpreted as a cancel all effects
	 * 
	 * @return the effects to be canceled
	 */
	public EAdList<EAdEffect> getEffects() {
		return effects;
	}

	/**
	 * Returns if this effect cancel all running effects
	 * 
	 * @return if this effect cancel all running effects
	 */
	public boolean cancelAll() {
		return effects.size() == 0;
	}

}
