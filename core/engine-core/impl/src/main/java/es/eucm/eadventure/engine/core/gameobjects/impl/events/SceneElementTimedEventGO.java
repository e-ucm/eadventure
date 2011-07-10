package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class SceneElementTimedEventGO extends
		AbstractEventGO<EAdSceneElementTimedEvent> {

	private int elapsedTime;

	private int repeats;

	@Inject
	public SceneElementTimedEventGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public void initialize() {
		super.initialize();
		elapsedTime = 0;
		repeats = element.getRepeats();
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (repeats != 0) {
			if (elapsedTime == 0) {
				this.runEffects(element
						.getEffects(EAdSceneElementTimedEvent.SceneElementTimedEventType.START_TIME));
			}
			elapsedTime += GameLoop.SKIP_MILLIS_TICK;
			if (elapsedTime > element.getTime()) {
				this.runEffects(element
						.getEffects(EAdSceneElementTimedEvent.SceneElementTimedEventType.END_TIME));
				elapsedTime = 0;
				repeats--;
			}
		}
	}
}
