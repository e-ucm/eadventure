package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdActiveElementEffect;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class ActiveElementEffectGO extends AbstractEffectGO<EAdActiveElementEffect>{

	public ActiveElementEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public void initilize() {
		super.initilize();
		if ( gameState.getActiveElement() != null ){
			element.getSceneElementEffect().setSceneElement(gameState.getActiveElement());
			gameState.addEffect(element.getSceneElementEffect());
		}
	}

	
	
	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
