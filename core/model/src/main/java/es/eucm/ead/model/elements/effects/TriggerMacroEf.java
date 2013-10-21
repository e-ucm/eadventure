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

import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

/**
 * Effect to trigger the effects contained in a macro. It only trigger the first
 * macro whose conditions are true
 */
@Element
public class TriggerMacroEf extends Effect {

	@Param
	private EAdList<EAdList<Effect>> macros;

	@Param
	private EAdList<Condition> conditions;

	public TriggerMacroEf() {
		super();
		macros = new EAdList<EAdList<Effect>>();
		conditions = new EAdList<Condition>();
	}

	public void putEffects(Condition condition, EAdList<Effect> macro) {
		macros.add(macro);
		conditions.add(condition);
	}

	public EAdList<EAdList<Effect>> getMacros() {
		return macros;
	}

	public EAdList<Condition> getConditions() {
		return conditions;
	}

	public void setMacros(EAdList<EAdList<Effect>> macros) {
		this.macros = macros;
	}

	public void setConditions(EAdList<Condition> conditions) {
		this.conditions = conditions;
	}

	public void putEffect(Condition c, Effect effect) {
		EAdList<Effect> macro = new EAdList<Effect>();
		macro.add(effect);
		putEffects(c, macro);

	}

}
