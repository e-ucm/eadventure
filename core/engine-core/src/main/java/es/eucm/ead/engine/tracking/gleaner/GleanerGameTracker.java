package es.eucm.ead.engine.tracking.gleaner;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.gameobjects.effects.EffectGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.tracking.AbstractGameTracker;
import es.eucm.ead.model.elements.EAdAdventureModel;
import es.eucm.gleaner.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GleanerGameTracker extends AbstractGameTracker {

	private static final Logger logger = LoggerFactory
			.getLogger(GleanerGameTracker.class);

	public static final String GLEANER_CONFIG = "@gleaner.config";

	public static final String TRACK = "track";

	public static final String URL = "url";

	public static final String AUTHENTICATION = "authentication";

	public static final String GAME_KEY = "gamekey";

	public static final String PRESS = "pressed";
	public static final String PHASES = "phases";
	public static final String VARIABLES = "variables";
	private static final String MAX_TRACES = "maxtraces";

	private Tracker tracker;

	private AssetHandler assetHandler;

	private JsonReader jsonReader;

	private String currentPhase;

	// Configuration options
	private boolean phases;
	private boolean press;

	@Inject
	public GleanerGameTracker(GameState gameState, Tracker tracker,
			AssetHandler assetHandler) {
		super(gameState);
		this.tracker = tracker;
		this.assetHandler = assetHandler;
		this.jsonReader = new JsonReader();
	}

	@Override
	protected boolean startTrackingImpl(EAdAdventureModel model) {
		if (assetHandler.fileExists(GLEANER_CONFIG)) {
			JsonValue config = jsonReader.parse(assetHandler
					.getTextFile(GLEANER_CONFIG));
			boolean isTracking = config.getBoolean(TRACK, false);
			if (isTracking) {
				String url = config.getString(URL, null);
				String gamekey = config.getString(GAME_KEY, null);
				String authentication = config.getString(AUTHENTICATION, null);
				if (url == null || gamekey == null || authentication == null) {
					logger
							.warn("Invalid configuration for gleaner. Check gleaner.config");
				} else {
					tracker.setAuthToken(authentication);
					tracker.setServerURL(url);
					tracker.startTracking(gamekey);
					configure(config);
				}
			}
			return isTracking;
		}
		return false;
	}

	/**
	 * Configure the tracker
	 *
	 * @param config the json configuration
	 */
	private void configure(JsonValue config) {
		press = config.getBoolean(PRESS, false);
		phases = config.getBoolean(PHASES, false);
		tracker.setMaxTracesPerQueue(config.getInt(MAX_TRACES, -1));
		JsonValue variables = config.get(VARIABLES);
		if (variables != null) {
			if (variables.isArray()) {
				JsonValue current = variables.child();
				while (current != null) {
					this.watchField(current.asString());
					current = current.next();
				}
			} else {
				logger
						.warn("Invalid configuration value for variables. It should be an array of strings.");
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
		tracker.flush();
	}

	@Override
	public void input(Event action, SceneElementGO target) {
		if (action instanceof InputEvent) {
			InputEvent i = (InputEvent) action;
			switch (i.getType()) {
			case touchDown:
				if (press) {
					tracker.press(currentPhase, target.getName(),
							i.getStageX(), i.getStageY());
				}
				break;
			}
		}
	}

	@Override
	public void effect(EffectGO<?> effect) {

	}

	@Override
	public void phaseStart(String phaseId) {
		currentPhase = phaseId;
		if (phases) {
			tracker.phaseStart(phaseId);
		}
	}

	@Override
	public void phaseEnd(String phaseId) {
		if (phases) {
			tracker.phaseEnd(phaseId);
		}
	}

	@Override
	public void varUpdate(String varId, Object newValue) {
		tracker.varUpdate(varId, newValue);
	}

	@Override
	public void gameEnd() {
		tracker.gameEnd();
	}
}
