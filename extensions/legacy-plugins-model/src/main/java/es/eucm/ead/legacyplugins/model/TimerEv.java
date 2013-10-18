package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.events.Event;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.params.text.EAdString;

/**
 * Event to represent old eadventure timers
 */
@Element
public class TimerEv extends Event {

	@Param
	private int time;

	@Param
	private Condition initCondition;

	@Param
	private Condition stopCondition;

	@Param
	private EAdList<Effect> expiredEffects;

	@Param
	private EAdList<Effect> stoppedEffects;

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

	public Condition getInitCondition() {
		return initCondition;
	}

	public void setInitCondition(Condition initCondition) {
		this.initCondition = initCondition;
	}

	public Condition getStopCondition() {
		return stopCondition;
	}

	public void setStopCondition(Condition stopCondition) {
		this.stopCondition = stopCondition;
	}

	public EAdList<Effect> getExpiredEffects() {
		return expiredEffects;
	}

	public void setExpiredEffects(EAdList<Effect> expiredEffects) {
		this.expiredEffects = expiredEffects;
	}

	public EAdList<Effect> getStoppedEffects() {
		return stoppedEffects;
	}

	public void setStoppedEffects(EAdList<Effect> stoppedEffects) {
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
