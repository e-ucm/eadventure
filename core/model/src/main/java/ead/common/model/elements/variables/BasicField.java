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

package ead.common.model.elements.variables;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.EAdElement;

@Element
public class BasicField<T> extends AbstractOperation implements EAdField<T> {

	@Param("element")
	private EAdElement element;

	@Param("variable")
	private EAdVarDef<T> varDef;

	public BasicField() {

	}

	/**
	 * Constructs a field
	 *
	 * @param element
	 *            field owner. It can be {@code null}, if there's no owner
	 * @param name
	 *            field name
	 * @param fieldType
	 *            field type
	 * @param initialValue
	 *            field initial value
	 */
	public BasicField(EAdElement element, String name, Class<T> fieldType,
			T initialValue) {
		this(element, new VarDef<T>(name, fieldType, initialValue));
	}

	/**
	 * Constructs a field
	 *
	 * @param element
	 *            field owner
	 * @param varDef
	 *            variable definition
	 */
	public BasicField(EAdElement element, EAdVarDef<T> varDef) {
		this.element = element;
		this.varDef = varDef;
	}

	@Override
	public EAdElement getElement() {
		return element;
	}

	@Override
	public EAdVarDef<T> getVarDef() {
		return varDef;
	}

	@Override
	public String toString() {
		return (element != null ? element : "NULL") + "." + varDef.getName();
	}

	public void setVarDef(EAdVarDef<T> varDef) {
		this.varDef = varDef;
	}

	public void setElement(EAdElement element) {
		this.element = element;
	}

}
