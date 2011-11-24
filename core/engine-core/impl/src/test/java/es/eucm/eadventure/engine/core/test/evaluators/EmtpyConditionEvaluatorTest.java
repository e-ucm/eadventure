package es.eucm.eadventure.engine.core.test.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.EmptyConditionValue;
import es.eucm.eadventure.engine.core.evaluators.impl.EmptyConditionEvaluator;

public class EmtpyConditionEvaluatorTest {
	
	protected EmptyConditionEvaluator evaluator;	
	protected EmptyCondition cTrue = new EmptyCondition(EmptyConditionValue.TRUE);
	protected EmptyCondition cFalse = new EmptyCondition(EmptyConditionValue.FALSE);
	
	@Before
	public void setUp(){
		evaluator = new EmptyConditionEvaluator();
	}

	@Test
	public void test() {
		assertTrue(evaluator.evaluate(cTrue));
		assertFalse(evaluator.evaluate(cFalse));
	}

}
