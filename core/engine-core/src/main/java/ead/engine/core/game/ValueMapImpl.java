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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.interfaces.features.Variabled;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.EAdVarDef;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.operators.OperatorFactory;
import ead.tools.reflection.ReflectionProvider;

@Singleton
public class ValueMapImpl implements ValueMap {

	protected Map<Object, Map<EAdVarDef<?>, Object>> map;

	private ArrayList<Object> updateList;

	protected OperatorFactory operatorFactory;

	protected static final Logger logger = LoggerFactory
			.getLogger("Value Map");

	private ReflectionProvider reflectionProvider;

	private boolean updateEnable;

	@Inject
	public ValueMapImpl(ReflectionProvider reflectionProvider,
			OperatorFactory operatorFactory,
			EvaluatorFactory evaluatorFactory) {
		map = new HashMap<Object, Map<EAdVarDef<?>, Object>>();
		logger.info("New instance");
		this.reflectionProvider = reflectionProvider;
		this.operatorFactory = operatorFactory;
		if (operatorFactory != null) {
			operatorFactory.install(this, evaluatorFactory);
		}

		if (evaluatorFactory != null) {
			evaluatorFactory.install(this, operatorFactory);
		}
		updateList = new ArrayList<Object>();
		updateEnable = true;
	}

	@Override
	public void setValue(EAdField<?> field, Object value) {
		setValue(maybeDecodeField(field), field.getVarDef(), value);
	}

	@Override
	public void setValue(Object element, EAdVarDef<?> varDef,
			Object value) {
		if (value == null
				|| reflectionProvider.isAssignableFrom(
						varDef.getType(), value.getClass())) {

			Map<EAdVarDef<?>, Object> valMap = map
					.get(maybeDecodeField(element));
			if (valMap == null) {
				valMap = new HashMap<EAdVarDef<?>, Object>();
				map.put(element, valMap);

				// Sets initial values, if any
				addInitVariables(element, valMap);
			}

			valMap.put(varDef, value);
			if (updateEnable)
				addUpdatedElement(element);

		} else {
			logger.warn("setValue failed: Impossible to cast "
					+ varDef.getType() + " to " + value.getClass()
					+ " for element " + element + " of class "
					+ element.getClass());
		}
	}

	private void addInitVariables(Object o,
			Map<EAdVarDef<?>, Object> initVars) {
		if (o instanceof Variabled) {
			initVars.putAll(((Variabled) o).getVars());
		}
	}

	public void setValue(EAdField<?> var, EAdOperation operation) {
		Object result = operatorFactory.operate(var.getVarDef(),
				operation);
		if (result != null) {
			setValue(var, result);
		}
	}

	@Override
	public void setValue(Object element, EAdVarDef<?> var,
			EAdOperation operation) {
		Object result = operatorFactory.operate(var.getType(),
				operation);
		setValue(element, var, result);
	}

	@Override
	public <S> S getValue(EAdField<S> var) {
		return getValue(var.getElement(), var.getVarDef());
	}

	@Override
	public <S> S getValue(Object element, EAdVarDef<S> varDef) {
		return getFinalValue(maybeDecodeField(element), varDef);
	}

	@SuppressWarnings("unchecked")
	private <S> S getFinalValue(Object element, EAdVarDef<S> varDef) {
		Map<EAdVarDef<?>, Object> valMap = map.get(element);

		if (valMap == null) {
			valMap = new HashMap<EAdVarDef<?>, Object>();
			map.put(element, valMap);

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
		return map.get(maybeDecodeField(element));
	}

	@Override
	public Object maybeDecodeField(Object element) {
		if (element != null && element instanceof EAdField<?>) {
			EAdField<?> field = (EAdField<?>) element;
			Object fieldElement = field.getElement();
			if (fieldElement instanceof EAdField<?>) {
				Object result = getValue(field.getElement(),
						field.getVarDef());
				return maybeDecodeField(result);
			} else {
				return fieldElement;
			}
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
		map.remove(maybeDecodeField(element));
	}

}
