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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.EAdKeyEvent.KeyCode;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.events.StayInBoundsEvent;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

/**
 * <p>
 * {@link BasicHUD} default implementation
 * </p>
 * 
 */
@Singleton
public class BasicHUDImpl implements BasicHUD {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger("BasicHUDImpl");

	private GUI gui;

	private MenuHUD menuHUD;

	private Game game;

	private GameObjectFactory gameObjectFactory;

	private GameState gameState;

	private GameObjectManager gameObjectManager;

	private MouseState mouseState;

	private EAdBasicSceneElement contextual;

	private CaptionImpl c = new CaptionImpl();

	private boolean contextualOn = false;

	private ValueMap valueMap;

	private StringHandler stringHandler;

	@Inject
	public BasicHUDImpl(MenuHUD menuHUD, GameObjectFactory gameObjectFactory,
			GameState gameState, GameObjectManager gameObjectManager,
			MouseState mouseState, ValueMap valueMap,
			StringHandler stringHandler) {
		logger.info("New instance");
		this.menuHUD = menuHUD;
		this.gameObjectFactory = gameObjectFactory;
		this.gameState = gameState;
		this.gameObjectManager = gameObjectManager;
		this.mouseState = mouseState;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
		c = new CaptionImpl();
		c.setFont(new EAdFontImpl(12.0f));
		c.setBubbleColor(new EAdColor(255, 255, 125));
		c.setPadding(10);
		c.setTextColor(EAdColor.BLACK);
		stringHandler.setString(c.getText(), "");
		contextual = new EAdBasicSceneElement("contextual", c);
		contextual.setPosition(new EAdPositionImpl(Corner.CENTER, 0, 0));
		contextual.getEvents().add(new StayInBoundsEvent(contextual));
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void setGUI(GUI gui) {
		this.gui = gui;
		this.menuHUD.setGUI(gui);
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
	public void doLayout(EAdTransformation transformation) {
		if (game.getAdventureModel().getInventory() != null)
			gui.addElement(gameObjectFactory.get(game.getAdventureModel()
					.getInventory()), transformation);

		if (contextualOn)
			gui.addElement(gameObjectFactory.get(contextual), transformation);

		if (mouseState.getDraggingGameObject() != null && mouseState.isInside()) {
			GameObject<?> draggedGO = mouseState.getDraggingGameObject();
			EAdTransformation t = (EAdTransformation) transformation.clone();
			t.getMatrix().preTranslate(mouseState.getDragDifX(),
					mouseState.getDragDifY());
			gui.addElement(draggedGO, t);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		contextualOn = false;
		GameObject<? extends EAdElement> go = (GameObject<? extends EAdElement>) mouseState
				.getGameObjectUnderMouse();

		if (go != null && go.getElement() instanceof EAdElement) {
			EAdString name = valueMap.getValue((EAdElement) go.getElement(),
					EAdBasicSceneElement.VAR_NAME);
			if (name != null) {
				stringHandler.setString(c.getText(),
						stringHandler.getString(name));

				SceneElementGO<?> cgo = ((SceneElementGO<?>) gameObjectFactory
						.get(contextual));

				cgo.getRenderAsset().update();

				int mouseX = valueMap.getValue(SystemFields.MOUSE_X);
				int mouseY = valueMap.getValue(SystemFields.MOUSE_Y) - 40;

				valueMap.setValue(contextual, EAdBasicSceneElement.VAR_X,
						mouseX);
				valueMap.setValue(contextual, EAdBasicSceneElement.VAR_Y,
						mouseY);
				gameObjectFactory.get(contextual).update();

				contextualOn = true;
			} else {
				contextualOn = false;

			}
		} else {
			contextualOn = false;
		}
		
		if (game.getAdventureModel().getInventory() != null)
			gameObjectFactory.get(game.getAdventureModel().getInventory()).update();
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

	@Override
	public void setPosition(EAdPosition p) {

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
