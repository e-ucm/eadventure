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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.engine.core.ValueMap;

@Singleton
public class ValueMapImpl implements ValueMap {

	protected Map<Class<?>, Map<?, ?>> map;

	private static final Logger logger = Logger.getLogger("Value Map");

	@Inject
	public ValueMapImpl() {
		map = new HashMap<Class<?>, Map<?, ?>>();
		logger.info("New instance");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> void setValue(EAdVar<S> var, S value) {
		if (var.isConstant()) {
			logger.log(Level.WARNING, "Attempted to modify a constant value");
			return;
		} else {
			Map<EAdVar<S>, S> valMap = (Map<EAdVar<S>, S>) map.get(var
					.getType());
			if (valMap == null) {
				valMap = new HashMap<EAdVar<S>, S>();
				map.put(var.getType(), valMap);
				logger.info("New value map String " + var.getType());
			}

			valMap.put(var, value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getValue(EAdVar<S> var) {
		Map<EAdVar<S>, S> valMap = (Map<EAdVar<S>, S>) map.get(var.getType());
		if (valMap == null) {
			logger.log(Level.WARNING, "Initializing variable " + var);
			setValue(var, var.getInitialValue());
			valMap = (Map<EAdVar<S>, S>) map.get(var.getType());
		}
		S value = valMap.get(var);
		if (value == null) {
			logger.log(Level.WARNING, "No such value " + var.getName()
					+ " of type " + var.getType());
			setValue(var, var.getInitialValue());
			value = var.getInitialValue();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clean() {
		int i = 0;
		logger.log(Level.INFO, "Cleaning...");
		for ( Map<?, ?> m: map.values() ){
			Map<EAdVar<?>, ?> mp = (Map<EAdVar<?>, ?>) m;
			for ( EAdVar<?> var: mp.keySet() ){
				if ( !var.isConstant() && !var.isGlobal()){
					mp.remove(var);
					i++;
				}
			}
		}
		logger.log(Level.INFO, i + " variables deleted.");
	}

}
