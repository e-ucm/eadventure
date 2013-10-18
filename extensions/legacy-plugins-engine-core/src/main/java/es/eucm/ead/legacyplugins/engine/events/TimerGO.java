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

			if (stopped) {
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
