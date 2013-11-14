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

package es.eucm.ead.legacyplugins.engine.events;

import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.events.AbstractEventGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.legacyplugins.engine.sceneelements.ClockDisplayGO;
import es.eucm.ead.legacyplugins.model.TimerEv;
import es.eucm.ead.legacyplugins.model.elements.ClockDisplay;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.elements.huds.BottomHud;
import es.eucm.ead.model.params.fills.Paint;

@Reflectable
public class TimerGO extends AbstractEventGO<TimerEv> {

	private final SceneElementFactory sceneElementFactory;

	private int remainingTime;

	private boolean running;

	private boolean stopped;

	private boolean done;

	private ClockDisplayGO display;

	@Inject
	public TimerGO(Game game, SceneElementFactory sceneElementFactory) {
		super(game);
		this.sceneElementFactory = sceneElementFactory;

	}

	@Override
	public void initialize() {
		super.initialize();
		remainingTime = element.getTime();
		running = false;
		stopped = false;
		done = false;
		if (element.isDisplay()) {
			if (display == null) {
				ClockDisplay clock = new ClockDisplay();
				clock.setColor(Paint.BLACK_ON_WHITE);
				clock.setFont(BasicFont.REGULAR);
				clock.setPosition(0, 0);
				display = (ClockDisplayGO) sceneElementFactory.get(clock);
			}
			SceneElementGO hud = game.getGUI().getHUD(BottomHud.ID);
			hud.addSceneElement(display);
		}
	}

	@Override
	public void act(float delta) {
		if (!done) {
			running = game.getGameState().evaluate(element.getInitCondition());
			boolean newStopped = element.getStopCondition() == null ? !running
					: game.getGameState().evaluate(element.getStopCondition());
			// The timer was just stopped
			if (running && newStopped && !stopped) {
				stopped = newStopped;
				runEffects(this.element.getStoppedEffects());
				done = !element.isMultipleStarts();
				running = false;
			}

			if (running && !stopped) {
				remainingTime -= delta;
				if (remainingTime <= 0) {
					runEffects(this.element.getExpiredEffects());
					if (element.isRunsInLoops()) {
						remainingTime = element.getTime();
					} else {
						done = true;
					}
				}
			}

			if (stopped && display != null) {
				display.setVisible(element.isShowWhenStopped());
			} else if (element.isDisplay()) {
				int ms = element.isCountdown() ? remainingTime : element
						.getTime()
						- remainingTime;
				int seconds = (ms / 1000 % 60);
				int minutes = (ms / 1000 / 60);
				int hours = (ms / 1000 / 3600);
				display.setTime(hours, minutes, seconds);
				display.setVisible(true);
			}

		}
	}

	@Override
	public void release() {
		super.release();
		display.remove();
	}
}
