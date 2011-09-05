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

package es.eucm.eadventure.engine.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

@Singleton
public class ValueMapImpl implements ValueMap {

	protected Map<EAdElement, Map<EAdVarDef<?>, Object>> map;

	private OperatorFactory operatorFactory;

	private static final Logger logger = Logger.getLogger("Value Map");

	@Inject
	public ValueMapImpl(OperatorFactory operatorFactory) {
		map = new HashMap<EAdElement, Map<EAdVarDef<?>, Object>>();
		logger.info("New instance");
		this.operatorFactory = operatorFactory;
	}

	@Override
	public <S> void setValue(EAdElement element, EAdVarDef<S> varDef, S value) {
		Map<EAdVarDef<?>, Object> valMap = map.get(element);
		if (valMap == null) {
			valMap = new HashMap<EAdVarDef<?>, Object>();
			map.put(element, valMap);
			logger.info("New value map String " + varDef.getType());
		}

		valMap.put(varDef, value);
	}

	@Override
	public <S> void setValue(EAdField<S> field, S value) {
		setValue(field.getElement(), field.getVarDefinition(), value);
	}

	@Override
	public <S> S getValue(EAdField<S> var) {
		return getValue(var.getElement(), var.getVarDefinition());
	}

	public <S> void setValue(EAdField<S> var, EAdOperation operation) {
		operatorFactory.operate(var, operation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getValue(EAdElement element, EAdVarDef<S> varDef) {
		Map<EAdVarDef<?>, Object> valMap = map.get(element);
		if (valMap == null) {
			// If the variable has not been set, returns the initial value
			return varDef.getInitialValue();
		}
		Object value = valMap.get(varDef);
		// If the variable has not been set, returns the initial value
		return value == null || !value.getClass().equals(varDef.getType()) ? varDef
				.getInitialValue() : (S) value;
	}

	@Override
	public void remove(EAdElement element) {
		map.remove(element);
	}

	@Override
	public <S> void setValue(EAdElement element, EAdVarDef<S> var,
			EAdOperation operation) {
		// TODO try to avoid the new
		setValue(new EAdFieldImpl<S>(element, var), operation);

	}

	@Override
	public void setValue(EAdVarDef<?> varDef, Object value, EAdElement element) {
		if ( value.getClass().equals(varDef.getType())){
			Map<EAdVarDef<?>, Object> valMap = map.get(element);
			if (valMap == null) {
				valMap = new HashMap<EAdVarDef<?>, Object>();
				map.put(element, valMap);
				logger.info("New value map String " + varDef.getType());
			}

			valMap.put(varDef, value);
		}
		
	}

}
