package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;

@Element(detailed = EAdTimerImpl.class, runtime = EAdTimerImpl.class)
public class EAdTimerImpl extends AbstractEAdElement implements EAdTimer {
	
	/**
	 * Time in millisecons
	 */
	@Param("time")
	private Integer time;
	
	private EAdVar<Boolean> timerStarted;

	private EAdVar<Boolean> timerEnded;

	public EAdTimerImpl(String id) {
		super(id);
		time = 5000;
		timerStarted = new BooleanVar("timerStarted", this);
		timerEnded = new BooleanVar("timerEnded", this);
	}

	@Override
	public Integer getTime() {
		return time;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}

	@Override
	public EAdVar<Boolean> timerStartedVar() {
		return timerStarted;
	}

	@Override
	public EAdVar<Boolean> timerEndedVar() {
		return timerEnded;
	}
	
}
