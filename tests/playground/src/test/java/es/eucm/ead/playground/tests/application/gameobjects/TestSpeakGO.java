package es.eucm.ead.playground.tests.application.gameobjects;

import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.effects.AbstractEffectGO;
import es.eucm.ead.model.elements.effects.text.SpeakEf;

import javax.inject.Inject;

public class TestSpeakGO extends AbstractEffectGO<SpeakEf> {

	private int time;

	@Inject
	public TestSpeakGO(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		super.initialize();
		time = 1000;
	}

	@Override
	public void act(float delta) {
		time -= delta;
	}

	@Override
	public boolean isFinished() {
		return time <= 0;
	}

	@Override
	public boolean isQueueable() {
		return true;
	}
}
