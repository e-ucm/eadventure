package ead.engine.core.gdx.desktop.debugger.hooks;

import ead.common.model.elements.EAdAdventureModel;
import ead.engine.core.game.enginefilters.EngineHook;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;

public class ModelLoadedHook extends DebuggerHook<EAdAdventureModel> implements
		EngineHook {

	@Override
	public void execute(Game game, GameState gameState, GUI gui) {
		super.fireListeners(game.getAdventureModel());
	}

	@Override
	public int compareTo(EngineHook engineHook) {
		return 0;
	}

}