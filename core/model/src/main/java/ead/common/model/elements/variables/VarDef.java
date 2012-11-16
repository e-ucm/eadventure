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
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.variables.EAdVarDef;

@Element
public class VarDef<T> extends BasicElement implements EAdVarDef<T> {

	@Param("name")
	private String name;

	@Param("class")
	private Class<T> type;

	@Param("initialValue")
	private T initialValue;

	public VarDef() {
		this.id = "varDef";
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
	 * @param constant
	 *            if the variable is constant
	 * @param global
	 *            if the variable is global
	 */
	public VarDef(String name, Class<T> type, T initialValue) {
		this.id = "var_" + name + "_" + randomSuffix();
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
	public String toString(){
		return name + "";
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

}
