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

package ead.engine.core.operators;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.EAdElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdOperation;
import ead.common.model.elements.variables.EAdVarDef;
import ead.common.util.AbstractFactory;
import ead.common.util.ReflectionProvider;
import ead.engine.core.evaluators.EvaluatorFactory;
import ead.engine.core.factorymapproviders.OperatorFactoryMapProvider;
import ead.engine.core.game.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class OperatorFactoryImpl extends AbstractFactory<Operator<?>> implements
		OperatorFactory {

	private Logger log = LoggerFactory.getLogger("Operator Factory");

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
			valueMap.setValue(fieldResult, result);
		}
		else {
			log.debug("Null result for " + operation + ": {} := {}", fieldResult, result);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdOperation, S> S operate(Class<S> clazz, T operation) {
		if (operation == null) {
			log.error("Null operation attempted: null returned as class {}", clazz);
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
