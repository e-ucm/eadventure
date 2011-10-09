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

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

/**
 * <p>Abstract implementation of the Menu HUD</p>
 *
 */
@Singleton
public abstract class MenuHUDImpl implements MenuHUD {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger("MenuHUDImpl");
	
	/**
	 * The game's {@link GUI}
	 */
	protected GUI gui;
		
	/**
	 * The current {@link GameState}
	 */
	private GameState gameState;
	
	private GameObjectManager gameObjectManager;
	
	@Inject
	public MenuHUDImpl(GameState gameState, GameObjectManager gameObjectManager) {
		logger.info("New instance");
		this.gameState = gameState;
		this.gameObjectManager = gameObjectManager;
	}

	@Override
	public void setGUI(GUI gui) {
		this.gui = gui;
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
			gameObjectManager.removeHUD(this);
			gameState.setPaused(false);
			action.consume();
			return true;
		}

		// Returns true to block interaction with lower HUDs and GOs
		action.consume();
		return true;
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
		// DO NOTHING
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#getDraggableElement
	 * (es.eucm.eadventure.engine.core.MouseState)
	 */
	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#getPosition()
	 */
	@Override
	public EAdPositionImpl getPosition() {
		return null;
	}
	
	@Override
	public void setPosition( EAdPosition position ){
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.GameObject#getElement()
	 */
	@Override
	public Void getElement() {
		return null;
	}
	
	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}
	
	@Override
	public boolean isEnable() {
		return true;
	}

}
