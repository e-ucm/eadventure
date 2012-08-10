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

package ead.engine.core.evaluators;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ead.common.model.elements.EAdCondition;
import ead.engine.core.factorymapproviders.EvaluatorFactoryMapProvider;
import ead.engine.core.game.ValueMap;
import ead.engine.core.operators.OperatorFactory;
import ead.tools.AbstractFactory;
import ead.tools.reflection.ReflectionProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EvaluatorFactoryImpl extends AbstractFactory<Evaluator<?>> implements EvaluatorFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger("EvaluatorFactoryImpl");

	@Inject
	public EvaluatorFactoryImpl(ReflectionProvider interfacesProvider) {
		super(null, interfacesProvider);
	}

    @Override
	public void install( ValueMap valueMap, OperatorFactory operatorFactory ){
		setMap(new EvaluatorFactoryMapProvider( valueMap, this, operatorFactory ));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EAdCondition> boolean evaluate(T condition) {
		if (condition == null)
			return true;
		Evaluator<T> evaluator = (Evaluator<T>) get(condition.getClass());
		return evaluator.evaluate(condition);
	}

}
