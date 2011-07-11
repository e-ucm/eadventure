package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveActiveElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class MoveActiveElementGO extends AbstractEffectGO<EAdMoveActiveElement> {

	@Inject
	public MoveActiveElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
	}

	@Override
	public void initilize() {
		super.initilize();
		Object object = gameState.getScene().getElement();
		if (object instanceof EAdScene && action instanceof MouseAction) {
			int x = ((MouseAction) action).getVirtualX();
			int y = ((MouseAction) action).getVirtualY();
			EAdScene scene = (EAdScene) object;
			if (scene.getTrajectoryGenerator() != null) {
				EAdList<EAdPosition> trajectory = scene
						.getTrajectoryGenerator().getTrajectory(
								gameState.getActiveElement().getPosition(), x,
								y);
				for (EAdPosition p : trajectory) {
					EAdMoveSceneElement effect = new EAdMoveSceneElement(
							"trajectory", gameState.getActiveElement(),
							p.getX(), p.getY(), MovementSpeed.NORMAL);
					gameState.addEffect(effect);
				}
			}
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
