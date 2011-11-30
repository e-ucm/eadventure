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

package es.eucm.eadventure.engine.core.evaluators.impl;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.conditions.impl.ListedCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.EmptyConditionValue;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.engine.core.evaluators.Evaluator;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;

@Singleton
public class ListedConditionEvaluator implements Evaluator<ListedCondition> {

	private static final Logger logger = Logger.getLogger("ListedConditionEvaluator");
	
	private EvaluatorFactory evaluatorFactory;
	
	@Inject
	public ListedConditionEvaluator(EvaluatorFactory evaluatorFactory) {
		this.evaluatorFactory = evaluatorFactory;
	}
	
	@Override
	public boolean evaluate(ListedCondition condition) {
		boolean temp = false;
		if (condition.getNullOperator().getValue() == EmptyConditionValue.TRUE)
			temp = true;
		
		Iterator<EAdCondition> conditions = condition.getConditionsIterator();
		while (conditions.hasNext()) {
			EAdCondition cond = conditions.next();
			boolean temp2 = evaluatorFactory.evaluate(cond);
			switch (condition.getOperator()) {
			case AND:
				temp = temp && temp2;
				break;
			case OR:
				temp = temp || temp2;
				break;
			default:
				logger.log(Level.WARNING, "No valid operator, condition: " + condition);
			}
		}
		return temp;
	}

}
