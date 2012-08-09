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

import ead.common.model.EAdElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.EAdVarDef;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.operators.OperatorFactory;
import ead.tools.ReflectionProvider;

@Singleton
public class ValueMapImpl implements ValueMap {

	protected Map<EAdVarDef<?>, Object> systemVars;

	protected Map<EAdElement, Map<EAdVarDef<?>, Object>> map;

	private ArrayList<EAdElement> updateList;

	protected OperatorFactory operatorFactory;

	protected static final Logger logger = LoggerFactory.getLogger("Value Map");

	private ReflectionProvider reflectionProvider;

	private boolean updateEnable;

	@Inject
	public ValueMapImpl(ReflectionProvider reflectionProvider,
			OperatorFactory operatorFactory, EvaluatorFactory evaluatorFactory) {
		map = new HashMap<EAdElement, Map<EAdVarDef<?>, Object>>();
		systemVars = new HashMap<EAdVarDef<?>, Object>();
		logger.info("New instance");
		this.reflectionProvider = reflectionProvider;
		setOperatorFactory(operatorFactory);
		if ( operatorFactory != null ){
			operatorFactory.install(this, evaluatorFactory);
		}
		
		if ( evaluatorFactory != null ){
			evaluatorFactory.install(this, operatorFactory);
		}
		updateList = new ArrayList<EAdElement>();
		updateEnable = true;
	}

	@Override
	public void setOperatorFactory(OperatorFactory operatorFactory) {
		this.operatorFactory = operatorFactory;
	}

	@Override
	public void setValue(EAdElement element, EAdVarDef<?> varDef, Object value) {
		if (value == null
				|| reflectionProvider.isAssignableFrom(varDef.getType(),
						value.getClass())) {

			Map<EAdVarDef<?>, Object> valMap = element == null ? systemVars
					: map.get(getFinalElement(element));
			if (valMap == null) {
				valMap = new HashMap<EAdVarDef<?>, Object>();
				map.put(element, valMap);
			}

			valMap.put(varDef, value);
			if (updateEnable)
				addUpdatedElement(element);

		} else {
			logger.warn("setValue failed: Impossible to cast "
					+ varDef.getType() + " to " + value.getClass()
					+ " for element " + element.getId() + " of class "
					+ element.getClass());
		}
	}

	@Override
	public void setValue(EAdField<?> field, Object value) {
		setValue(field.getElement(), field.getVarDef(), value);
	}

	public void setValue(EAdField<?> var, EAdOperation operation) {
		operatorFactory.operate(var.getElement(), var.getVarDef(), operation);
	}

	@Override
	public void setValue(EAdElement element, EAdVarDef<?> var,
			EAdOperation operation) {
		Object result = operatorFactory.operate(var.getType(), operation);
		setValue(element, var, result);
	}

	@Override
	public <S> S getValue(EAdField<S> var) {
		return getValue(var.getElement(), var.getVarDef());
	}

	@Override
	public <S> S getValue(EAdElement element, EAdVarDef<S> varDef) {
		return getFinalValue(getFinalElement(element), varDef);
	}

	@SuppressWarnings("unchecked")
	private <S> S getFinalValue(EAdElement element, EAdVarDef<S> varDef) {
		Map<EAdVarDef<?>, Object> valMap = element == null ? systemVars : map
				.get(element);

		if (valMap == null) {
			// If the variable has not been set, returns the initial value
			return varDef.getInitialValue();
		}
		Object value = valMap.get(varDef);
		// If the variable has not been set, returns the initial value

		// reflectionProvider.isAssignableFrom is not used, because types are
		// checked in setValue
		return value == null ? varDef.getInitialValue() : (S) value;

	}

	@Override
	public void remove(EAdElement element) {
		map.remove(getFinalElement(element));
	}

	public Map<EAdVarDef<?>, Object> getElementVars(EAdElement element) {
		return element == null ? systemVars : map.get(getFinalElement(element));
	}

	public EAdElement getFinalElement(EAdElement element) {
		if (element != null && element instanceof EAdField<?>) {
			EAdField<?> field = (EAdField<?>) element;
			Object result = getValue(field.getElement(), field.getVarDef());
			if (result instanceof EAdElement)
				return (EAdElement) result;
			else
				return element;
		} else
			return element;
	}

	@Override
	public boolean checkForUpdates(EAdElement element) {
		if (updateList.contains(element)) {
			updateList.remove(element);
			return true;
		}
		return false;
	}

	private void addUpdatedElement(EAdElement element) {
		if (element != null && !updateList.contains(element)) {
			updateList.add(element);
		}
	}

	@Override
	public void setUpdateListEnable(boolean enable) {
		updateEnable = enable;
	}

	@Override
	public void clearUpdateList() {
		updateList.clear();
	}
	
	public Map<EAdVarDef<?>, Object> getSystemVars() {
		return systemVars;
	}

	public void setSystemVars(Map<EAdVarDef<?>, Object> systemVars) {
		this.systemVars = systemVars;
	}

	public Map<EAdElement, Map<EAdVarDef<?>, Object>> getElementVars() {
		return map;
	}

	public void setElementVars(Map<EAdElement, Map<EAdVarDef<?>, Object>> map) {
		this.map = map;
	}

	public ArrayList<EAdElement> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(ArrayList<EAdElement> updateList) {
		this.updateList = updateList;
	}

}
