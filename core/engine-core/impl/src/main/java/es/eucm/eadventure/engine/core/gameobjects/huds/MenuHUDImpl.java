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

package es.eucm.eadventure.engine.core.gameobjects.huds;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.effects.QuitGameEf;
import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.guievents.enums.KeyEventType;
import es.eucm.eadventure.common.model.elements.guievents.enums.KeyEventCode;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.CaptionImpl;
import es.eucm.eadventure.common.util.EAdPosition;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.input.InputAction;
import es.eucm.eadventure.engine.core.input.actions.KeyActionImpl;
import es.eucm.eadventure.engine.core.platform.GUI;

/**
 * <p>
 * Abstract implementation of the Menu HUD
 * </p>
 * 
 */
@Singleton
public class MenuHUDImpl extends AbstractHUD implements MenuHUD {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger("MenuHUDImpl");

	/**
	 * The current {@link GameState}
	 */
	private GameState gameState;

	private GameObjectManager gameObjectManager;

	@Inject
	public MenuHUDImpl(GUI gui, GameState gameState,
			GameObjectManager gameObjectManager, StringHandler stringHandler,
			SceneElementGOFactory sceneElementFactory) {
		super(gui);
		logger.info("New instance");
		this.gameState = gameState;
		this.gameObjectManager = gameObjectManager;
		addExitButton(stringHandler, sceneElementFactory);
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
	public boolean processAction(InputAction<?> action) {
		if (action instanceof KeyActionImpl) {
			KeyActionImpl keyAction = (KeyActionImpl) action;
			if (keyAction.getKeyCode() == KeyEventCode.ESC
					&& keyAction.getType() == KeyEventType.KEY_PRESSED) {
				gameObjectManager.removeHUD(this);
				gameState.setPaused(false);
				action.consume();
				return true;
			}
		}

		// Returns true to block interaction with lower HUDs and GOs
		action.consume();
		return true;
	}

	public boolean contains(int x, int y) {
		return true;
	}

	private void addExitButton(StringHandler stringHandler,
			SceneElementGOFactory sceneElementFactory) {
		CaptionImpl c = new CaptionImpl();
		stringHandler.setString(c.getText(), "Exit");
		SceneElementImpl button = new SceneElementImpl( c);
		button.setId("exit_button");
		c.setBubblePaint(new EAdPaintImpl(new EAdLinearGradient(
				EAdColor.ORANGE, new EAdColor(255, 200, 0), 0, 40), EAdColor.BLACK, 2));
		c.setPadding(10);
		c.setFont(new EAdFontImpl(18));
		button.getBehavior().addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK,
				new QuitGameEf());
		button.setPosition(new EAdPosition(EAdPosition.Corner.CENTER,
				400, 300));
		addElement(sceneElementFactory.get(button));
	}

}
