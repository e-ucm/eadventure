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

package ead.engine.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.interfaces.features.Variabled;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.params.variables.EAdVarDef;
import ead.engine.core.game.interfaces.ValueMap;
import es.eucm.ead.tools.StringHandler;
import es.eucm.ead.tools.reflection.ReflectionProvider;

public class ValueMapImpl implements ValueMap {

	protected static final Logger logger = LoggerFactory.getLogger("ValueMap");

	/**
	 * Reflection provider
	 */
	protected ReflectionProvider reflectionProvider;

	/**
	 * String handler
	 */
	protected StringHandler stringHandler;

	/**
	 * Contains all variables values
	 */
	protected Map<Object, Map<EAdVarDef<?>, Object>> valuesMap;

	/**
	 * Contains the elements with variables updated
	 */
	protected ArrayList<Object> updateList;

	/**
	 * If the update list is enable
	 */
	private boolean updateEnable;

	public ValueMapImpl(ReflectionProvider reflectionProvider,
			StringHandler stringHandler) {
		this.stringHandler = stringHandler;
		this.reflectionProvider = reflectionProvider;
		valuesMap = new HashMap<Object, Map<EAdVarDef<?>, Object>>();
		logger.info("New instance");
		updateList = new ArrayList<Object>();
		updateEnable = true;
	}

	@Override
	public <S> void setValue(EAdField<S> field, S value) {
		setValue(field.getElement(), field.getVarDef(), value);
	}

	@Override
	public <S> void setValue(Object element, EAdVarDef<S> varDef, S value) {
		if (value == null
				|| reflectionProvider.isAssignableFrom(varDef.getType(), value
						.getClass())) {

			Map<EAdVarDef<?>, Object> valMap = valuesMap
					.get(maybeDecodeField(element));
			if (valMap == null) {
				valMap = new HashMap<EAdVarDef<?>, Object>();
				valuesMap.put(element, valMap);

				// Sets initial values, if any
				addInitVariables(element, valMap);
			}

			valMap.put(varDef, (S) value);
			if (updateEnable)
				addUpdatedElement(element);

		} else {
			logger.warn("setValue failed: Impossible to cast "
					+ varDef.getType() + " to " + value.getClass()
					+ " for element " + element + " of class "
					+ element.getClass());
		}
	}

	private void addInitVariables(Object o, Map<EAdVarDef<?>, Object> initVars) {
		if (o instanceof Variabled) {
			initVars.putAll(((Variabled) o).getVars());
		}
	}

	@Override
	public <S> S getValue(EAdField<S> var) {
		return getValue(var.getElement(), var.getVarDef());
	}

	@SuppressWarnings("unchecked")
	public <S> S getValue(Object element, EAdVarDef<S> varDef) {
		Map<EAdVarDef<?>, Object> valMap = valuesMap
				.get(maybeDecodeField(element));

		if (valMap == null) {
			valMap = new HashMap<EAdVarDef<?>, Object>();
			valuesMap.put(element, valMap);

			// Sets initial values, if any
			addInitVariables(element, valMap);
		}
		Object value = valMap.get(varDef);
		// If the variable has not been set, returns the initial value

		// reflectionProvider.isAssignableFrom is not used, because types are
		// checked in setValue
		return value == null ? varDef.getInitialValue() : (S) value;

	}

	@Override
	public Map<EAdVarDef<?>, Object> getElementVars(Object element) {
		return valuesMap.get(maybeDecodeField(element));
	}

	@Override
	public Object maybeDecodeField(Object element) {
		if (element != null && element instanceof EAdField<?>) {
			EAdField<?> field = (EAdField<?>) element;
			Object result = getValue(field.getElement(), field.getVarDef());
			return maybeDecodeField(result);
		}
		return element;
	}

	@Override
	public boolean checkForUpdates(Object element) {
		if (updateList.contains(element)) {
			updateList.remove(element);
			return true;
		}
		return false;
	}

	/**
	 * Adds ana element to the update list
	 * 
	 * @param element
	 */
	private void addUpdatedElement(Object element) {
		if (element != null && !updateList.contains(element)) {
			updateList.add(element);
		}
	}

	@Override
	public void setUpdateListEnable(boolean enable) {
		updateEnable = enable;
	}

	@Override
	public void remove(Object element) {
		valuesMap.remove(maybeDecodeField(element));
	}

	public boolean contains(Object element) {
		return valuesMap.get(element) != null;
	}

}
