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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.effects.impl.EAdQuitGame;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.MenuHUDImpl;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public class PlayNMenuHUDImpl extends MenuHUDImpl {

	/**
	 * The class logger
	 */
	private Logger logger = Logger.getLogger("PlayNMenuHUDImpl");

	/**
	 * An {@link EAdSceneElement} that represents a button on the menu
	 */
	private EAdSceneElement button;

	/**
	 * The {@link GameObjectFactory}
	 */
	private GameObjectFactory gameObjectFactory;

	@Inject
	public PlayNMenuHUDImpl(GameObjectFactory gameObjectFactory, GameState gameState, GameObjectManager gameObjectManager) {
		super(gameState, gameObjectManager);
		logger.info("New instance");
		
		this.gameObjectFactory = gameObjectFactory;

		button = new EAdButton("menuButton");
		((EAdButton) button).setText(new CaptionImpl(new EAdString("button")));
		((EAdButton) button).setUpNewInstance();
		((EAdButton) button).getBehavior().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				new EAdQuitGame("menuButton_quitGame"));
		((EAdButton) button).setPosition(new EAdPositionImpl(EAdPositionImpl.Corner.CENTER,
				300, 300));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#doLayout()
	 */
	@Override
	public void doLayout(EAdTransformation t) {
		gui.addElement(gameObjectFactory.get(button), t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#update(es.eucm.
	 * eadventure.engine.core.GameState)
	 */
	@Override
	public void update() {
		gameObjectFactory.get(button).update();
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#getAssets(java.util.List, boolean)
	 */
	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}

}
