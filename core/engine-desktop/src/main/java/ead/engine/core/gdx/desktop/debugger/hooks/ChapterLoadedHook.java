package ead.engine.core.gdx.desktop.debugger.hooks;

import ead.common.model.elements.EAdChapter;
import ead.engine.core.game.enginefilters.EngineHook;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;

/**
 * @author anserran
 *         Date: 17/05/13
 *         Time: 12:02
 */
public class ChapterLoadedHook extends DebuggerHook<EAdChapter> implements
		EngineHook {
	@Override
	public void execute(Game game, GameState gameState, GUI gui) {
		EAdChapter c = game.getCurrentChapter();
		super.fireListeners(c);
	}

	@Override
	public int compareTo(EngineHook engineHook) {
		return 0;
	}
}
