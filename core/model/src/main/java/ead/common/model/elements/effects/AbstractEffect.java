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

package ead.common.model.elements.effects;

import ead.common.interfaces.Param;
import ead.common.model.elements.ConditionedElement;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.extra.EAdList;

/**
 * <p>
 * Abstract implementation of {@link EAdEffect} which includes the basic methods
 * </p>
 * 
 * 
 */
public abstract class AbstractEffect extends ConditionedElement implements
		EAdEffect {

	/**
	 * Sets if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	@Param
	private boolean persistent;

	@Param
	private EAdList<EAdEffect> nextEffects;

	@Param
	private EAdList<EAdEffect> simultaneousEffects;

	@Param
	private boolean nextEffectsAlways;

	/**
	 * Creates an non-blocking and non-opaque effect with next effects list
	 * empty
	 * 
	 * @param parent
	 *            parent for this element
	 * @param id
	 *            id for this element
	 */
	public AbstractEffect() {
		super();
		nextEffectsAlways = false;
		nextEffects = new EAdList<EAdEffect>();
		simultaneousEffects = new EAdList<EAdEffect>();
	}

	public EAdList<EAdEffect> getNextEffects() {
		return nextEffects;
	}

	public void addNextEffect(EAdEffect e) {
		nextEffects.add(e);
	}

	public EAdList<EAdEffect> getSimultaneousEffects() {
		return simultaneousEffects;
	}

	public void addSimultaneousEffect(EAdEffect e) {
		simultaneousEffects.add(e);
	}

	public void setNextEffectsAlways(boolean always) {
		this.nextEffectsAlways = always;
	}

	public boolean isNextEffectsAlways() {
		return nextEffectsAlways;
	}

	/**
	 * Indicates if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Sets if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public void setNextEffects(EAdList<EAdEffect> nextEffects) {
		this.nextEffects = nextEffects;
	}

	public void setSimultaneousEffects(EAdList<EAdEffect> simultaneousEffects) {
		this.simultaneousEffects = simultaneousEffects;
	}

}
