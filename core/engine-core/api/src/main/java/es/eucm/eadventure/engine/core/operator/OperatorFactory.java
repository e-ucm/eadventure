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

package es.eucm.eadventure.engine.core.operator;

import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.ValueMap;

/**
 * A factory with all {@link Operator} for all {@link EAdOperation}.
 */
public interface OperatorFactory {

	/**
	 * <p>
	 * Calculates the result of the given {@link EAdOperation} with the current
	 * values in the {@link ValueMap}
	 * </p>
	 * The value should be stored in the {@link ValueMap} by the actual
	 * {@link Operator}
	 * 
	 * @param <T>
	 * @param eAdVar
	 *            the var where the result will be stored
	 * @param eAdOperation
	 *            operation to be done
	 * @return operation's result. If operation is {@code null}, a null is
	 *         returned.
	 */
	public <T extends EAdOperation, S> S operate(EAdVar<S> eAdVar,
			T eAdOperation);

}
