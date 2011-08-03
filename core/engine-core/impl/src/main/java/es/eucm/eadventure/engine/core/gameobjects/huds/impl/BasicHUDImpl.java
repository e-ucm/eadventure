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

package es.eucm.eadventure.engine.core.gameobjects.huds.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

/**
 * <p>
 * {@link BasicHUD} default implementation
 * </p>
 * 
 */
@Singleton
public class BasicHUDImpl implements BasicHUD<Void> {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("BasicHUDImpl");

	private GUI gui;

	private MenuHUD menuHUD;

	private Game game;
	
	private GameObjectFactory gameObjectFactory;
	
	private GameState gameState;
	
	private GameObjectManager gameObjectManager;
	
	@Inject
	public BasicHUDImpl(GUI gui, MenuHUD menuHUD, Game game, GameObjectFactory gameObjectFactory, GameState gameState, GameObjectManager gameObjectManager) {
		logger.info("New instance");
		this.gui = gui;
		this.menuHUD = menuHUD;
		this.game = game;
		this.gameObjectFactory = gameObjectFactory;
		this.gameState = gameState;
		this.gameObjectManager = gameObjectManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		if (action instanceof KeyAction
				&& ((KeyAction) action).getKeyCode() == KeyCode.ESC) {
			gameObjectManager.addHUD(menuHUD);
			gameState.setPaused(true);
			action.consume();
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#setElement(java
	 * .lang.Object)
	 */
	@Override
	public void setElement(Void element) {

	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		if (game.getAdventureModel().getInventory() != null)
			gui.addElement(gameObjectFactory.get(game.getAdventureModel().getInventory()), 0, 0);
	}

	@Override
	public void update(GameState state) {
		gameObjectFactory.get(game.getAdventureModel().getInventory()).update(state);
	}

	@Override
	public EAdPositionImpl getPosition() {
		return null;
	}

	@Override
	public Void getElement() {
		return null;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}

}
