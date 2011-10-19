package es.eucm.eadventure.engine.core.gameobjects.impl.effects.sceneelement;

import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public abstract class SceneElementEffectGO<T extends EAdSceneElementEffect>
		extends AbstractEffectGO<T> {

	protected EAdSceneElement sceneElement;

	public SceneElementEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}

	@Override
	public void setElement(T element) {
		super.setElement(element);
		if (element.getSceneElement() != null) {
			sceneElement = element.getSceneElement();
		} else if (element.getSceneElementField() != null) {
			sceneElement = gameState.getValueMap().getValue(
					element.getSceneElementField());
		} else {
			sceneElement = parent;
		}
	}

}
