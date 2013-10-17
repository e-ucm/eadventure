package es.eucm.ead.legacyplugins.engine.sceneelements;

import com.google.inject.Inject;
import com.gwtent.reflection.client.Reflectable;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.legacyplugins.model.LegacyVars;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;

/**
 * Override standard scene behavior to implement behavior of old scenes
 * [GE - Arrows] [GE - Follow]
 */
@Reflectable
public class DynamicSceneGO extends SceneGO {

	private boolean firstPerson;

	private boolean adjust;

	private int sceneWidth;

	private int gameWidth;

	@Inject
	public DynamicSceneGO(AssetHandler assetHandler,
			SceneElementFactory sceneElementFactory, Game game,
			EventFactory eventFactory) {
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
				float x = gameState.getValue(SystemFields.MOUSE_X);
				float deltaX = delta * 0.5f;
				if (x < sceneWidth * 0.1f && this.getX() < 0) {
					this.setX(Math.min(0, this.getRelativeX() + deltaX));
				} else if (x > gameWidth * 0.9f
						&& this.getX() > gameWidth - sceneWidth) {
					this.setX(Math.max(gameWidth - sceneWidth, this
							.getRelativeX()
							- deltaX));
				}
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
