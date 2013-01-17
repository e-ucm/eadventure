package ead.engine.core.gameobjects.effects;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scenes.EAdComplexSceneElement;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public abstract class VisualEffectGO<P extends EAdEffect> extends
		AbstractEffectGO<P> {

	public VisualEffectGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(gameState);
	}

	protected SceneElementGO<?> visualRepresentation;

	protected abstract EAdComplexSceneElement getVisualRepresentation();

	public void setEffect(P effect) {
//		super.setEffect(effect);
		EAdComplexSceneElement e = getVisualRepresentation();
		if (e != null) {
			e.setId(e.getId() + "engine");
//			setElement(e);
		}
	}

	public boolean isVisualEffect() {
		return true;
	}

}
