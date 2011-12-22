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

package es.eucm.eadventure.common.model.elements.variables;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.EAdVarDef;

@Reflectable
@Element(detailed = FieldImpl.class, runtime = FieldImpl.class)
public class FieldImpl<T> implements EAdField<T> {

	@Param("element")
	private EAdElement element;

	@Param("variable")
	private EAdVarDef<T> varDef;

	public FieldImpl() {

	}

	public FieldImpl(EAdElement element, EAdVarDef<T> varDef) {
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
	public String getId() {
		return (element != null ? element.getId() : "") + "_" + varDef.getId()
				+ "_field";
	}
	
	@Override
	public void setId(String id) {
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof EAdField) {
			EAdField<?> f = ((EAdField<?>) o);
			boolean elementEquals = (f.getElement() == null && element == null) ||
					(f.getElement() != null && f.getElement().equals(element));
			if (elementEquals)
				return f.getVarDef().equals(varDef);
		}
		return false;
	}

	public int hashCode() {
		return ("" + (element != null ? element.hashCode() : "") + "_" + varDef.hashCode()).hashCode();
	}
	
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
