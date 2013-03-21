package ead.engine.core.gameobjects.effects.sceneelement;

import com.google.inject.Inject;

import ead.common.model.elements.effects.sceneelements.ChangeColorEf;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;

public class ChangeColorGO extends SceneElementEffectGO<ChangeColorEf> {

	private SceneElementGOFactory sceneElementFactory;

	@Inject
	public ChangeColorGO(GameState gameState,
			SceneElementGOFactory sceneElementFactory) {
		super(gameState);
		this.sceneElementFactory = sceneElementFactory;
	}

	public void initialize() {
		super.initialize();
		SceneElementGO sceneElementGO = sceneElementFactory.get(sceneElement);
		if (sceneElementGO != null) {
			sceneElementGO.setColor(effect.getRed(), effect.getGreen(), effect
					.getBlue(), sceneElementGO.getColor().a);
		}
	}

}
