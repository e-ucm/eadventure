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

package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class SceneGOImpl extends AbstractGameObject<EAdScene> implements SceneGO<EAdScene> {

	private static final Logger logger = Logger.getLogger("ScreenGOImpl");
	
	@Inject
	public SceneGOImpl( GameObjectFactory gameObjectFactory) {
		super();
		logger.info( "New instance" );
	}
	
	public void setElement(EAdScene space) {
		super.setElement(space);
	}
	
	public void doLayout(int offsetX, int offsetY) {
		//TODO scene offset
		gui.addElement(gameObjectFactory.get(element.getBackground()), offsetX, offsetY);
		for (EAdSceneElement sceneElement : element.getSceneElements()) {
			SceneElementGO<?> sceneElementGO = (SceneElementGO<?>) gameObjectFactory.get(sceneElement);
			if ( sceneElementGO.isVisible() )
				gui.addElement(sceneElementGO, offsetX, offsetY);
		}
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
		gameObjectFactory.get(element.getBackground()).update(state);
		for (EAdSceneElement sceneElement : element.getSceneElements())
			gameObjectFactory.get(sceneElement).update(state);
	}

	@Override
	public boolean acceptsVisualEffects() {
		return true;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(
			List<RuntimeAsset<?>> assetList, boolean allAssets) {
		assetList = gameObjectFactory.get(element.getBackground()).getAssets(assetList, allAssets);
		for (EAdSceneElement sceneElement : element.getSceneElements())
			assetList = gameObjectFactory.get(sceneElement).getAssets(assetList, allAssets);
		return assetList;
	}

}
