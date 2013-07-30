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

package ead.common.model.elements.weev.story.element.impl.effects;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.effects.ModifyInventoryEf;
import ead.common.model.elements.effects.enums.InventoryEffectAction;
import ead.common.model.elements.weev.story.element.impl.AbstractEffect;
import ead.common.model.weev.Actor;

/**
 * Effect to change the state of an {@link Actor}.
 * <p>
 * This effect is the WEEV implementation of the {@link ModifyInventoryEf} in
 * eAdventure. This effect allows to change or modify the actor state using the
 * values in {@link ModifyInventoryEf.InventoryEffectAction}.
 */
@Element
public class ChangeActorState extends AbstractEffect {

	/**
	 * The change or {@link ModifyInventoryEf.InventoryEffectAction} to be applied to
	 * the {@link Actor}
	 */
	@Param
	//TODO should probably use an enum with more values...
	private InventoryEffectAction modification;

	/**
	 * The {@link Actor} where to apply the change
	 */
	@Param
	private Actor actor;

	/**
	 * @param actor
	 *            The {@link Actor} where to apply the change
	 * @param modification
	 *            The change or {@link ModifyInventoryEf.InventoryEffectAction} to be
	 *            applied to the {@link Actor}
	 */
	public ChangeActorState(Actor actor, InventoryEffectAction modification) {
		this.actor = actor;
		this.modification = modification;
	}

	public InventoryEffectAction getModification() {
		return modification;
	}

	public void setModification(InventoryEffectAction modification) {
		this.modification = modification;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
