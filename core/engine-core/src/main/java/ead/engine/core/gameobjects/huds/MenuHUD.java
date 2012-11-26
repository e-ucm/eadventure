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

package ead.engine.core.gameobjects.huds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.effects.QuitGameEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.text.BasicFont;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.KeyInputAction;
import ead.engine.core.platform.GUI;

/**
 * <p>
 * Abstract implementation of the Menu HUD
 * </p>
 * 
 */
public class MenuHUD extends AbstractHUD {

	/**
	 * The logger
	 */
	private static Logger logger = LoggerFactory
			.getLogger("MenuHUDImpl");

	/**
	 * The current {@link GameState}
	 */
	private GameState gameState;

	private SceneElementGOFactory sceneElementFactory;

	public MenuHUD(GUI gui, GameState gameState,
			SceneElementGOFactory sceneElementFactory) {
		super(gui);
		logger.info("New instance of MenuHUD");
		this.gameState = gameState;
		this.sceneElementFactory = sceneElementFactory;
		this.setPriority(100);

	}

	public void init() {
		this.setVisible(false);
		addExitButton(sceneElementFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.GameObject#processAction(es
	 * .eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		if (action instanceof KeyInputAction) {
			KeyInputAction keyAction = (KeyInputAction) action;
			if (keyAction.getKeyCode() == KeyGEvCode.ESCAPE
					&& keyAction.getType() == KeyEventType.KEY_PRESSED) {
				gameState.setPaused(false);
				action.consume();
				this.setVisible(!isVisible());
				return this;
			}
		}

		if (isVisible()) {
			DrawableGO<?> go = super.processAction(action);
			if (go != null) {
				return go;
			} else {
				action.consume();
				return this;
			}
		}
		return null;
	}

	@Override
	public boolean contains(int x, int y) {
		return isVisible();
	}

	private void addExitButton(
			SceneElementGOFactory sceneElementFactory) {
		Caption c = new Caption(new EAdString("engine.Exit"));
		SceneElement button = new SceneElement(c);
		c.setBubblePaint(new Paint(new LinearGradientFill(
				ColorFill.ORANGE, new ColorFill(255, 200, 0), 0, 40),
				ColorFill.BLACK, 2));
		c.setPadding(10);
		c.setFont(new BasicFont(18));
		button.getBehavior().addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				new QuitGameEf());
		button.setPosition(new EAdPosition(EAdPosition.Corner.CENTER,
				400, 300));
		addElement(sceneElementFactory.get(button));
	}

}
