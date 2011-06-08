package es.eucm.eadventure.common.model.elements;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;

public interface EAdTimer extends EAdElement {

	/**
	 * Returns the time, as an integer, that the timer should run for
	 * 
	 * @return the time that the timer should run for
	 */
	Integer getTime();
	
	EAdVar<Boolean> timerStartedVar();
	
	EAdVar<Boolean> timerEndedVar();
		
}
