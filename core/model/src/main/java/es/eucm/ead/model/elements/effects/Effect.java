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

package es.eucm.ead.model.elements.effects;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.elements.ConditionedElement;
import es.eucm.ead.model.elements.extra.EAdList;

/**
 * <p>
 * Abstract implementation of an effect which includes the basic methods
 * </p>
 * 
 * 
 */
public abstract class Effect extends ConditionedElement {

	/**
	 * Sets if the effect must be conserved when the scene changes and the
	 * effects is still running
	 */
	@Param
	private boolean persistent;

	@Param
	private EAdList<Effect> nextEffects;

	@Param
	private EAdList<Effect> simultaneousEffects;

	@Param
	private boolean nextEffectsAlways;

	/**
	 * Creates an non-blocking and non-opaque effect with next effects list
	 * empty
	 * 
	 */
	public Effect() {
		super();
		nextEffectsAlways = false;
		nextEffects = new EAdList<Effect>();
		simultaneousEffects = new EAdList<Effect>();
	}

	/**
	 * Returns the effects to be launched when this effect ends
	 *
	 * @return
	 */
	public EAdList<Effect> getNextEffects() {
		return nextEffects;
	}

	/**
	 * Adds a effect to be executed when this effect ends
	 *
	 * @param e
	 *            next effect
	 */
	public void addNextEffect(Effect e) {
		nextEffects.add(e);
	}

	/**
	 * Returns the effects to be launched when this effect is launched
	 *
	 * @return
	 */
	public EAdList<Effect> getSimultaneousEffects() {
		return simultaneousEffects;
	}

	/**
	 * Adds an effect to be launched just before this effect is launched
	 *
	 * @param e
	 */
	public void addSimultaneousEffect(Effect e) {
		simultaneousEffects.add(e);
	}

	/**
	 * Sets if the effects in the next effects list are launched even when the
	 * condition for the event is not fulfilled
	 *
	 * @param always
	 */
	public void setNextEffectsAlways(boolean always) {
		this.nextEffectsAlways = always;
	}

	/**
	 * Returns if the effects in the next effects list must be launched when the
	 * effect's conditions is not fulfilled
	 *
	 * @return
	 */
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

	public void setNextEffects(EAdList<Effect> nextEffects) {
		this.nextEffects = nextEffects;
	}

	public void setSimultaneousEffects(EAdList<Effect> simultaneousEffects) {
		this.simultaneousEffects = simultaneousEffects;
	}

}
