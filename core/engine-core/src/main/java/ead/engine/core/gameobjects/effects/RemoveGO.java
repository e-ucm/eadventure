package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.RemoveEf;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.platform.GUI;

public class RemoveGO extends AbstractEffectGO<RemoveEf> {

	private GUI gui;

	@Inject
	public RemoveGO(GameState gameState, GUI gui) {
		super(gameState);
		this.gui = gui;
	}

	public void initialize() {
		super.initialize();
		SceneElementGO<?> sceneElement = gui.getSceneElement(effect
				.getElement());
		if (sceneElement != null) {
			sceneElement.remove();
		}
	}

}
