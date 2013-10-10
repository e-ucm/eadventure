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

package es.eucm.ead.model.params.variables;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.AbstractParam;

@Element
public class VarDef<T> extends AbstractParam implements EAdVarDef<T> {

	@Param
	private String name;

	@Param
	private Class<T> type;

	@Param
	private T initialValue;

	public VarDef() {

	}

	/**
	 * Constructs a variable definition
	 * 
	 * @param name
	 *            variable's name
	 * @param type
	 *            variable's type
	 * @param initialValue
	 *            variable's initial value
	 */
	public VarDef(String name, Class<T> type, T initialValue) {
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getInitialValue() {
		return initialValue;
	}

	@Override
	public String toString() {
		return toStringData();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public void setInitialValue(T initialValue) {
		this.initialValue = initialValue;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
		if (o instanceof VarDef) {
			VarDef v = (VarDef) o;
			return this.getName().equals(v.getName());
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toStringData() {
		return name + ";" + type.getName()
				+ (initialValue == null ? "" : ";" + initialValue);
	}

	@Override
	public boolean parse(String data) {
		// We can't directly parse this param from its string
		return false;
	}

}
