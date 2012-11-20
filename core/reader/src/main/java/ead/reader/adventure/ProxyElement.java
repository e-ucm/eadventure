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

package ead.reader.adventure;

import ead.common.model.elements.BasicElement;
import ead.common.model.elements.extra.EAdList;
import ead.tools.reflection.ReflectionField;

/**
 * Represents a field that cannot be filled until its reference is resolved.
 */
public class ProxyElement extends BasicElement {

	/**
	 * Object for which the field should be set
	 */
	private Object parent;

	/**
	 * Field to set
	 */
	private ReflectionField field;

	/**
	 * Reference value that is still unresolved
	 */
	private String value;

	/**
	 * (If list-reference) list that should be set
	 */
	private EAdList<Object> list;

	/**
	 * Index in list to set
	 */
	private int listPos;

	public ProxyElement(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void setField(ReflectionField field) {
		this.field = field;
	}

	public ReflectionField getField() {
		return field;
	}

	public Object getParent() {
		return parent;
	}

	public void setList(EAdList<Object> list, int i) {
		this.list = list;
		this.listPos = i;
	}

	public EAdList<Object> getList() {
		return list;
	}

	public int getListPos() {
		return listPos;
	}
}
