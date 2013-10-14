package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.elements.EAdCondition;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.events.AbstractEvent;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.text.EAdString;

/**
 * Event to represent old eadventure timers
 */
@Element
public class TimerEv extends AbstractEvent {

	@Param
	private int time;

	@Param
	private EAdCondition initCondition;

	@Param
	private EAdCondition stopCondition;

	@Param
	private EAdList<EAdEffect> expiredEffects;

	@Param
	private EAdList<EAdEffect> stoppedEffects;

	@Param
	private EAdString displayName;

	@Param
	private boolean display;

	@Param
	private boolean countdown;

	@Param
	private boolean showWhenStopped;

	@Param
	private boolean multipleStarts;

	@Param
	private boolean runsInLoops;

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public EAdCondition getInitCondition() {
		return initCondition;
	}

	public void setInitCondition(EAdCondition initCondition) {
		this.initCondition = initCondition;
	}

	public EAdCondition getStopCondition() {
		return stopCondition;
	}

	public void setStopCondition(EAdCondition stopCondition) {
		this.stopCondition = stopCondition;
	}

	public EAdList<EAdEffect> getExpiredEffects() {
		return expiredEffects;
	}

	public void setExpiredEffects(EAdList<EAdEffect> expiredEffects) {
		this.expiredEffects = expiredEffects;
	}

	public EAdList<EAdEffect> getStoppedEffects() {
		return stoppedEffects;
	}

	public void setStoppedEffects(EAdList<EAdEffect> stoppedEffects) {
		this.stoppedEffects = stoppedEffects;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isCountdown() {
		return countdown;
	}

	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}

	public boolean isShowWhenStopped() {
		return showWhenStopped;
	}

	public void setShowWhenStopped(boolean showWhenStopped) {
		this.showWhenStopped = showWhenStopped;
	}

	public boolean isMultipleStarts() {
		return multipleStarts;
	}

	public void setMultipleStarts(boolean multipleStarts) {
		this.multipleStarts = multipleStarts;
	}

	public boolean isRunsInLoops() {
		return runsInLoops;
	}

	public void setRunsInLoops(boolean runsInLoops) {
		this.runsInLoops = runsInLoops;
	}

	public EAdString getDisplayName() {
		return displayName;
	}

	public void setDisplayName(EAdString displayName) {
		this.displayName = displayName;
	}
}
