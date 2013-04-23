package ead.engine.core.game.enginefilters;

import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;

public interface EngineHook extends Comparable<EngineHook> {

	void execute(Game game, GameState gameState, GUI gui);

}
