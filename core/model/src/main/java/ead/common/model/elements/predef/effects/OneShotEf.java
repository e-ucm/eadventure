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

package ead.common.model.elements.predef.effects;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.params.variables.VarDef;

/**
 * Represents an effect that is launched only once
 *
 */
public class OneShotEf extends ChangeFieldEf {

	public static final VarDef<Boolean> LAUNCHED = new VarDef<Boolean>(
			"OneShotEf.Launched", Boolean.class, false);

	/**
	 * 
	 * @param effect the effect to launch only once
	 */
	public OneShotEf(EAdEffect effect) {
		// Sets true launched variable
		BasicField<Boolean> f = new BasicField<Boolean>(this, LAUNCHED);
		addField(f);
		setOperation(EmptyCond.TRUE_EMPTY_CONDITION);
		// Sets as condition that launched variable is false
		setCondition(new NOTCond(new OperationCond(f)));

		// Adds as a next effect the one shot effect
		getNextEffects().add(effect);
	}

}
