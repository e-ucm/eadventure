/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import es.eucm.ead.model.elements.scenes.SceneElement;

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
	public void setElement(SceneElement element) {
		super.setElement(element);
		firstPerson = game.getAdventureModel().getProperty(
				LegacyVars.FIRST_PERSON, false);
		sceneWidth = element.getProperty(LegacyVars.SCENE_WIDTH, 800);
		adjust = sceneWidth > 800;
		gameWidth = gameState.getValue(SystemFields.GAME_WIDTH, 800);
	}

	public void act(float delta) {
		super.act(delta);
		if (adjust) {
			if (firstPerson) {
				// [GE - Arrows]
				float x = gameState.getValue(SystemFields.MOUSE_X, 0f);
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
				SceneElement s = gameState.getValue(
						SystemFields.ACTIVE_ELEMENT, (SceneElement) null);
				SceneElementGO active = gui.getSceneElement(s);
				this.setX(-Math.max(Math.min(sceneWidth - gameWidth,
						(int) active.getX() - gameWidth / 2), 0));
			}
		}
	}
}
