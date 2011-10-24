package es.eucm.eadventure.common.model.weev.story.element.impl.effects;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.weev.story.element.impl.AbstractEffect;
import es.eucm.eadventure.common.model.weev.story.element.impl.nodes.Timer;

/**
 * Effect to change the status of a {@link Timer}
 */
@Element(detailed = TimerEffect.class, runtime = TimerEffect.class)
public class TimerEffect extends AbstractEffect {

	public static enum Status {
		STARTED, STOPPED
	}

	/**
	 * The desired {@link Status} of the {@link Timer}
	 */
	@Param(value = "status")
	private Status status;

	/**
	 * The {@link Timer} to be affected
	 */
	@Param(value = "timer")
	private Timer timer;

	/**
	 * @param timer
	 *            The desired {@link Status} of the {@link Timer}
	 * @param status
	 *            The {@link Timer} to be affected
	 */
	public TimerEffect(Timer timer, Status status) {
		this.timer = timer;
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
