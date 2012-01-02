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

package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.scene.EAdScene;
import ead.common.resources.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.TransitionGO;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;

public class DisplaceTransitionGO extends EmptyTransitionGO implements
		TransitionGO {

	private int cont = 1;

	@Inject
	public DisplaceTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler, SceneElementGOFactory gameObjectFactory,
			GUI gui, GameState gameState,
			EngineConfiguration platformConfiguration, EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				platformConfiguration, eventFactory);
	}

	@Override
	public void setElement(EAdScene element) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl#doLayout(int,
	 * int)
	 * 
	 * The bloking element and the loading text should never be offset
	 */
	@Override
	public void doLayout(EAdTransformation transformation) {
		// int x = 800 * 100 / cont;
		gui.addElement(sceneElementFactory.get(screenBlock), transformation);
		gui.addElement(sceneElementFactory.get(loadingText), transformation);
		if (loaded) {
			gui.addElement(nextSceneGO, transformation);
		}
	}

	@Override
	public void update() {
		if (!loading) {
			loading = true;
			new Thread(new Loader()).start();
		}

		if (previousSceneImage == null) {
//			previousSceneImage = gui.commitToImage();
		}

		if (loaded) {
			cont += 3;
			if (cont > 100) {
				gameState.setScene(nextSceneGO);
				previousSceneImage.freeMemory();
			}
		}
	}

}
