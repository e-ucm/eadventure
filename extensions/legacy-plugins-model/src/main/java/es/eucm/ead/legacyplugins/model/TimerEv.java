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
