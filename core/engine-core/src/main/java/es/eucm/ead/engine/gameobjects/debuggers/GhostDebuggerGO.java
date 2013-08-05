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

package es.eucm.ead.engine.gameobjects.debuggers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.google.inject.Inject;

import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.factories.EventGOFactory;
import es.eucm.ead.engine.factories.SceneElementGOFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.gameobjects.sceneelements.GhostElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;

public class GhostDebuggerGO extends SceneElementGO {

	private SceneGO currentScene;

	private List<GhostElementGO> currentSceneElements;

	@Inject
	public GhostDebuggerGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
		currentSceneElements = new ArrayList<GhostElementGO>();
	}

	public void act(float delta) {
		if (this.currentScene != gui.getScene()) {
			currentScene = gui.getScene();
			if (currentScene != null) {
				for (GhostElementGO g : currentSceneElements) {
					g.setInteractionAreaVisible(false);
				}
				currentSceneElements.clear();
				collect(currentScene);
			}
		}
	}

	public void collect(Group e) {
		for (Actor go : e.getChildren()) {
			if (go instanceof GhostElementGO) {
				currentSceneElements.add((GhostElementGO) go);
				((GhostElementGO) go).setInteractionAreaVisible(true);
			}
			collect((Group) go);
		}
	}

	public boolean remove() {
		for (GhostElementGO g : currentSceneElements) {
			g.setInteractionAreaVisible(false);
		}
		currentScene = null;
		currentSceneElements.clear();
		return super.remove();
	}

}
