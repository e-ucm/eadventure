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

package es.eucm.ead.model.elements.predef.effects;

import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.operations.ElementField;

/**
 * Represents an effect that is launched only once
 *
 */
public class OneShotEf extends ChangeFieldEf {

	public static final String LAUNCHED = "launched";

	public OneShotEf() {
	}

	/**
	 * 
	 * @param effect the effect to launch only once
	 */
	public OneShotEf(Effect effect) {
		this(effect, null);
	}

	/**
	 * 
	 * @param effect the effect to launch only once
	 * @param noEffect the effect to launch all times except first
	 */
	public OneShotEf(Effect effect, Effect noEffect) {
		// Sets true launched variable
		ElementField f = new ElementField(this, LAUNCHED);
		OperationCond cond = new OperationCond(f);

		if (noEffect != null) {
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			triggerMacro.putEffect(new NOTCond(cond), effect);
			triggerMacro.putEffect(cond, noEffect);
			addNextEffect(triggerMacro);
			addNextEffect(new ChangeFieldEf(f, EmptyCond.TRUE));
		} else {
			setElement(this);
			setVarName(LAUNCHED);
			setOperation(EmptyCond.TRUE);
			// Sets as condition that launched variable is false
			setCondition(new NOTCond(cond));

			// Adds as a next effect the one shot effect
			addNextEffect(effect);
		}
	}

}
