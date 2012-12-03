package ead.engine.core.gameobjects.effects;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

public abstract class VisualEffectGO<P extends EAdEffect> extends
		AbstractEffectGO<P> {

	protected SceneElementGO<?> visualRepresentation;

	public VisualEffectGO(SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(gameObjectFactory, gui, gameState);
	}

	protected abstract EAdSceneElement getVisualRepresentation();

	public void initialize() {
		super.initialize();
		EAdSceneElement e = getVisualRepresentation();
		if (e != null) {
			e.setId(e.getId() + "engine");
			visualRepresentation = this.sceneElementFactory.get(e);
		}
	}

	@Override
	public void doLayout(EAdTransformation transformation) {
		if (visualRepresentation != null) {
			gui.addElement(visualRepresentation, transformation);
		}
	}

	public boolean isVisualEffect() {
		return true;
	}

	public void update() {
		if (visualRepresentation != null) {
			visualRepresentation.update();
		}
	}

}
