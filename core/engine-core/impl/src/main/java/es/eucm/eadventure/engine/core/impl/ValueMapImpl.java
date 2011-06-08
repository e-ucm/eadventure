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
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.ValueMap;

@Singleton
public class ValueMapImpl implements ValueMap {

	protected Map<Class<?>, Map<?, ?>> map;

	private static final Logger logger = LoggerFactory
			.getLogger("Value Map");

	@Inject
	public ValueMapImpl() {
		map = new HashMap<Class<?>, Map<?, ?>>();
		logger.info("New instance");
	}

	@Override
	public <T> void setValue(EAdElement id, T value) {
		@SuppressWarnings("unchecked")
		Map<EAdElement, T> valMap = (Map<EAdElement, T>) map.get(value.getClass());
		if (valMap == null) {
			valMap = new HashMap<EAdElement, T>();
			map.put(value.getClass(), valMap);
			logger.info("New value map " + id.getClass() + "  " + value.getClass());
		}
		valMap.put(id, value);
	}

	@Override
	public <T> T getValue(EAdElement id, Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Map<EAdElement, T> valMap = (Map<EAdElement, T>) map.get(clazz);
		if (valMap == null) {
			logger.warn("No values for the class " + clazz);
			return null;
		}
		T value = valMap.get(id);
		if (value == null)
			logger.warn("No such value " + id);
		return value;
	}

	@Override
	public <S> boolean contains(S id) {
		for (Entry<Class<?>, Map<?, ?>> m : this.map.entrySet()) {
			Object o = m.getValue().get(id);
			if (o != null)
				return true;
		}
		return false;
	}

	@Override
	public <S> void setValue(EAdVar<S> var, S value) {
		@SuppressWarnings("unchecked")
		Map<String, S> valMap = (Map<String, S>) map.get(var.getType());
		if (valMap == null) {
			valMap = new HashMap<String, S>();
			map.put(var.getType(), valMap);
			logger.info("New value map String " + var.getType());
		}
		
		valMap.put((var.getElement() != null ? "" + var.getElement().hashCode() : "") + var.getName(), value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getValue(EAdVar<S> var) {
		Map<String, S> valMap = (Map<String, S>) map.get(var.getType());
		if (valMap == null) {
			logger.warn("No values for the class " + var.getType());
			setValue(var, var.getInitialValue());
			valMap = (Map<String, S>) map.get(var.getType());
		}
		S value = valMap.get((var.getElement() != null ? "" + var.getElement().hashCode() : "") + var.getName() );
		if (value == null) {
			logger.warn("No such value " + (var.getElement() != null ? "" + var : "") );
			setValue(var, var.getInitialValue());
			value = var.getInitialValue();
		}
		return value;
	}

}
