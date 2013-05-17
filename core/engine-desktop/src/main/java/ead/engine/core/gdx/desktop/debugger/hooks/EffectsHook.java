package ead.engine.core.gdx.desktop.debugger.hooks;

import ead.common.model.elements.EAdEffect;
import ead.engine.core.game.enginefilters.EngineHook;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Hook to launch effects in the engine
 */
public class EffectsHook implements EngineHook {

	private List<EAdEffect> effects;

	public EffectsHook() {
		effects = new ArrayList<EAdEffect>();
	}

	public void addEffect(EAdEffect e) {
		synchronized (effects) {
			effects.add(e);
		}
	}

	@Override
	public int compareTo(EngineHook o) {
		return 0;
	}

	@Override
	public void execute(Game game, GameState gameState, GUI gui) {
		synchronized (gameState) {
			while (!effects.isEmpty()) {
				EAdEffect e = effects.remove(0);
				gameState.addEffect(e);
			}
		}
	}

}
