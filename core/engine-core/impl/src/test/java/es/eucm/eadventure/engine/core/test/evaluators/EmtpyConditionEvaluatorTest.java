package es.eucm.eadventure.engine.core.test.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition.Value;
import es.eucm.eadventure.engine.core.evaluators.impl.EmptyConditionEvaluator;

public class EmtpyConditionEvaluatorTest {
	
	protected EmptyConditionEvaluator evaluator;	
	protected EmptyCondition cTrue = new EmptyCondition(Value.TRUE);
	protected EmptyCondition cFalse = new EmptyCondition(Value.FALSE);
	
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
