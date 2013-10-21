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

package es.eucm.ead.engine.game;

import aurelienribon.tweenengine.TweenAccessor;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.StringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ValueMap implements TweenAccessor<ElementField> {

	static private Logger logger = LoggerFactory.getLogger(ValueMap.class);

	/**
	 * String handler
	 */
	protected StringHandler stringHandler;

	/**
	 * Contains all variables values
	 */
	protected Map<String, Map<String, Object>> valuesMap;

	public ValueMap(StringHandler stringHandler) {
		this.stringHandler = stringHandler;
		valuesMap = new HashMap<String, Map<String, Object>>();
	}

	@SuppressWarnings("all")
	public void setValue(String element, String varName, Object value) {
		Map<String, Object> valMap = valuesMap.get(element);
		if (valMap == null) {
			valMap = new HashMap<String, Object>();
			valuesMap.put(element == null ? null : element, valMap);
		}
		valMap.put(varName, value);
		logger.debug("{}.{}={}", element, varName, value);
	}

	public void setValue(Identified element, String varName, Object value) {
		this.setValue(element == null ? null : maybeDecodeField(element)
				.getId(), varName, value);
	}

	/**
	 * Sets the field to given value
	 *
	 * @param field the field
	 * @param value the value to the field
	 */
	public <S> void setValue(ElementField field, S value) {
		setValue(
				field.getElement() == null ? null : field.getElement().getId(),
				field.getVarDef(), value);
	}

	/**
	 * Returns the value for a variable in the given element
	 * Be cautious using this method because it could ignore initial values for variables
	 * @param elementId the element identifier
	 * @param varName the variable name
	 * @param defaultValue the default value, in case it's not set
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public <S> S getValue(String elementId, String varName, S defaultValue) {
		Map<String, Object> valMap = valuesMap.get(elementId);
		if (valMap == null) {
			valMap = new HashMap<String, Object>();
			valuesMap.put(elementId, valMap);
		}
		Object value = valMap.get(varName);
		// If the variable has not been set, returns the initial value
		return (S) (value == null ? defaultValue : value);
	}

	public <S> S getValue(Identified element, String varName, S defaultValue) {
		return getValue(element == null ? null : maybeDecodeField(element)
				.getId(), varName, defaultValue);
	}

	/**
	 * Returns the value of the field
	 *
	 *
	 * @param field the field to be consulted
	 * @param defaultValue the default value
	 * @return the value of the field
	 */
	public <S> S getValue(ElementField field, S defaultValue) {
		return getValue(field.getElement() == null ? null : field.getElement()
				.getId(), field.getVarDef(), defaultValue);
	}

	/**
	 * Returns the variables associated to an element, whose values are
	 * different from the defaults
	 *
	 * @param element the element. If the element is null, it returns system vars
	 * @return a map with the variables
	 */
	public Map<String, Object> getElementVars(Identified element) {
		return getElementVars(maybeDecodeField(element).getId());
	}

	public Map<String, Object> getElementVars(String elementId) {
		return valuesMap.get(elementId);
	}

	/**
	 * Returns the final element associated to the given element. It could be
	 * the element itself, but if the element is a field (with type
	 * {@link es.eucm.ead.model.elements.BasicElement}), the element pointed by the field will be returned,
	 *
	 * @param element the element
	 * @return the final element pointed by the element
	 */
	public Identified maybeDecodeField(Identified element) {
		if (element != null && element instanceof ElementField) {
			ElementField field = (ElementField) element;
			Object result = getValue(field.getElement() == null ? null : field
					.getElement().getId(), field.getVarDef(), null);
			return maybeDecodeField((Identified) result);
		}
		return element;
	}

	/**
	 * Removes all fields associated to the given element
	 *
	 * @param element the element
	 */
	public void remove(Identified element) {
		valuesMap.remove(maybeDecodeField(element).getId());
	}

	@Override
	public int getValues(ElementField elementField, int tweentype,
			float[] floats) {
		floats[0] = getValue(elementField, 0.0f);
		return 1;
	}

	@Override
	public void setValues(ElementField elementField, int tweentype,
			float[] floats) {
		setValue(elementField, floats[0]);
	}

}
