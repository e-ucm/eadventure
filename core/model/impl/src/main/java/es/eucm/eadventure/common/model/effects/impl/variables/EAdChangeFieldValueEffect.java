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

package es.eucm.eadventure.common.model.effects.impl.variables;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.effects.impl.AbstractEAdEffect;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;

/**
 * Effect for changing a field value
 * 
 */
@Element(runtime = EAdChangeFieldValueEffect.class, detailed = EAdChangeFieldValueEffect.class)
public class EAdChangeFieldValueEffect extends AbstractEAdEffect {

	/**
	 * Field to be changed
	 */
	@Param("fields")
	private EAdList<EAdField<?>> fields;

	/**
	 * Operation to be done. The result of this operation should be assigned to
	 * the variable
	 */
	@Param("operation")
	private EAdOperation operation;

	/**
	 * Creates an empty effect
	 * 
	 * @param id
	 *            Elements's id
	 */
	public EAdChangeFieldValueEffect(String id) {
		this(id, null, null);
	}

	/**
	 * Creates an effect with the required parameters
	 * 
	 * @param id
	 *            Elements id
	 * @param var
	 *            The field to be changed
	 * @param operation
	 *            The operation to be performed to obtain the value of the field
	 */
	public EAdChangeFieldValueEffect(String id, EAdField<?> field,
			EAdOperation operation) {
		super(id);
		this.fields = new EAdListImpl<EAdField<?>>(EAdField.class);
		if (field != null)
			fields.add(field);
		this.operation = operation;
	}

	/**
	 * Adds a field to be updated with the operation result
	 * 
	 * @param var
	 *            the variable
	 */
	public void addVar(EAdField<?> var) {
		fields.add(var);
	}

	/**
	 * Sets the operation to be done by this effect. The result of this
	 * operation should be assigned to the fields contained by the effect
	 * 
	 * @param operation
	 *            the operation
	 */
	public void setOperation(EAdOperation operation) {
		this.operation = operation;
	}

	/**
	 * Returns a list of the fields to be updated with the operation result
	 * 
	 * @return a list of the fields to be updated with the operation result
	 */
	public EAdList<EAdField<?>> getVars() {
		return fields;
	}

	/**
	 * Returns the operation to be done by this effect
	 * 
	 * @return the operation to be done by this effect
	 */
	public EAdOperation getOperation() {
		return operation;
	}

}
