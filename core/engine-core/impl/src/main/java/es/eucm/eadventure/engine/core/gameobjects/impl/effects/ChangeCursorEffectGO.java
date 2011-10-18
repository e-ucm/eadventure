package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdChangeCursorEffect;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class ChangeCursorEffectGO extends AbstractEffectGO<EAdChangeCursorEffect>{

	@Inject
	public ChangeCursorEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}
	


	@Override
	public void initilize() {
		super.initilize();
		gui.changeCursor(element.getImage());
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
