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

package es.eucm.eadventure.common.model.variables.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;

public class EAdVarImpl<T> implements EAdVar<T> {

	private Class<T> type;
	
	protected String name;
	
	private T defaultValue;
	
	private EAdElement element;

	public EAdVarImpl(Class<T> type, String name, EAdElement element) {
		this.type = type;
		this.name = name;
		this.element = element;
	}

	public EAdVarImpl(Class<T> type, String name) {
		this(type, name, null);
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
		return defaultValue;
	}
	

	@Override
	public void setInitialValue( T defaultValue ){
		this.defaultValue = defaultValue;
	}

	@Override
	public EAdElement getElement() {
		return element;
	}
	
	public void setElement(EAdElement element) {
		this.element = element;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EAdVar))
			return false;
		EAdVar<?> var = (EAdVar<?>) o;
		if (var.getElement() != getElement())
			return false;
		if (var.getName() != getName() || var.getType() != getType())
			return false;
		return true;
	}
	
	public String toString( ){
		return "Element:" + element + ";" + name; 
	}
	
	@Override
	public int hashCode() {
		if (element != null)
			return getName().hashCode();
		else
			return element.hashCode() + getName().hashCode();
	}
	
}
