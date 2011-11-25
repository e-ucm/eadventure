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

package es.eucm.eadventure.engine.core.operators.impl;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.EAdOperation;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.OperatorFactoryMapProvider;
import es.eucm.eadventure.engine.core.operator.Operator;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;

@Singleton
public class OperatorFactoryImpl extends AbstractFactory<Operator<?>> implements
		OperatorFactory {

	private Logger log = Logger.getLogger("Operator Factory");

	private ValueMap valueMap;

	@Inject
	public OperatorFactoryImpl(ReflectionProvider interfacesProvider) {
		super(null, interfacesProvider);
	}

	public void install(ValueMap valueMap, EvaluatorFactory evaluatorFactory) {
		this.valueMap = valueMap;
		setMap(new OperatorFactoryMapProvider(this, evaluatorFactory, valueMap,
				reflectionProvider));
	}

	@Override
	public <T extends EAdOperation, S> S operate(EAdField<S> fieldResult,
			T operation) {
		S result = operate(fieldResult.getVarDef().getType(), operation);
		if (result != null) {
			log.finest(operation + ": " + fieldResult + " := " + result);
			valueMap.setValue(fieldResult, result);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdOperation, S> S operate(Class<S> clazz, T operation) {
		if (operation == null) {
			log.severe("null operation attempted: null was returned with class " + clazz);
			return null;
		}
		Operator<T> operator = (Operator<T>) get(operation.getClass());
		return operator.operate(clazz, operation);
	}

	@Override
	public <T extends EAdOperation, S> S operate(EAdElement element,
			EAdVarDef<S> varDef, T operation) {
		S result = operate(varDef.getType(), operation);
		if (result != null) {
			valueMap.setValue(element, varDef, result);
		}
		return result;
	}

}
