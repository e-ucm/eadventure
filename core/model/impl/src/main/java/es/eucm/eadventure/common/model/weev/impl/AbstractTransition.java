package es.eucm.eadventure.common.model.weev.impl;

import es.eucm.eadventure.common.model.weev.Transition;

public class AbstractTransition<S> extends AbstractPositionedWEEVElement implements
		Transition<S> {

	private S start;
	
	private S end;
	
	@Override
	public S getStart() {
		return start;
	}
	
	public void setStart(S start) {
		this.start = start;
	}

	@Override
	public S getEnd() {
		return end;
	}
	
	public void setEnd(S end) {
		this.end = end;
	}

}
