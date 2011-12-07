package es.eucm.eadventure.engine.core.gameobjects.impl.effects.sceneelement;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.EAdSceneElementEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.AbstractEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public abstract class SceneElementEffectGO<T extends EAdSceneElementEffect>
		extends AbstractEffectGO<T> {

	protected EAdSceneElement sceneElement;

	public SceneElementEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}

	@Override
	public void setElement(T element) {
		super.setElement(element);
		EAdElement sceneElement = gameState.getValueMap().getFinalElement(
				element.getSceneElement());

		if (sceneElement instanceof EAdSceneElement) {
			this.sceneElement = (EAdSceneElement) sceneElement;
		} else if (sceneElement != null) {
			this.sceneElement = gameState.getValueMap().getValue(sceneElement,
					EAdSceneElementDefImpl.VAR_SCENE_ELEMENT);
		}
	}
}