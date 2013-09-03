package es.eucm.ead.legacyplugins.engine.sceneelements;

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.legacyplugins.model.LegacyVars;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;

/**
 * Override standard scene behavior to implement behavior of old scenes
 * [GE - Arrows] [GE - Follow]
 */
public class DynamicSceneGO extends SceneGO {

	private boolean firstPerson;

	private boolean adjust;

	private int sceneWidth;

	private int gameWidth;

	@Inject
	public DynamicSceneGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, Game game,
			EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, game, eventFactory);
	}

	@Override
	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		firstPerson = game.getAdventureModel().getVarInitialValue(
				LegacyVars.FIRST_PERSON);
		sceneWidth = element.getVarInitialValue(LegacyVars.SCENE_WIDTH);
		adjust = sceneWidth > 800;
		gameWidth = gameState.getValue(SystemFields.GAME_WIDTH);
	}

	public void act(float delta) {
		super.act(delta);
		if (adjust) {
			if (firstPerson) {
				// [GE - Arrows]

			} else {
				// [GE - Follow]
				EAdSceneElement s = gameState
						.getValue(SystemFields.ACTIVE_ELEMENT);
				SceneElementGO active = gui.getSceneElement(s);
				this.setX(-Math.max(Math.min(sceneWidth - gameWidth,
						(int) active.getX() - gameWidth / 2), 0));
			}
		}
	}
}
