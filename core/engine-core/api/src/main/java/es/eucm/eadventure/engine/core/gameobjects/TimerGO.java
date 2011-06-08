package es.eucm.eadventure.engine.core.gameobjects;

import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.engine.core.GameState;

public interface TimerGO extends GameObject<EAdTimer> {

	void update(GameState gameState);

}
