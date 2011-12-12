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

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.enums.KeyActionType;
import es.eucm.eadventure.common.model.guievents.enums.KeyCode;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.predef.model.events.ChaseTheMouseEvent;
import es.eucm.eadventure.common.predef.model.events.StayInBoundsEvent;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.huds.MenuHUD;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.KeyAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.EAdCanvas;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.impl.EAdTransformationImpl;

/**
 * <p>
 * The basic HUD contains the contextual information about elements (i.e. a sign
 * with the element name), and the mouse pointer. Mouse pointer can be disabled
 * through the system field {@link SystemFields#SHOW_MOUSE}
 * </p>
 * 
 */
@Singleton
public class BasicHUDImpl extends AbstractHUD implements BasicHUD {

	/**
	 * The logger
	 */
	protected static final Logger logger = Logger.getLogger("BasicHUDImpl");

	private static final int CURSOR_SIZE = 32;

	private MenuHUD menuHUD;

	private SceneElementGOFactory sceneElementFactory;

	protected GameState gameState;

	protected MouseState mouseState;

	private EAdBasicSceneElement contextual;

	private CaptionImpl c = new CaptionImpl();

	private StringHandler stringHandler;

	private EAdBasicSceneElement mouse;

	private Drawable cursor;

	private AssetHandler assetHandler;

	private GameObjectManager gameObjectManager;

	@Inject
	public BasicHUDImpl(MenuHUD menuHUD,
			SceneElementGOFactory gameObjectFactory, GameState gameState,
			MouseState mouseState, StringHandler stringHandler, GUI gui,
			AssetHandler assetHandler) {
		super(gui);
		logger.info("New instance");
		this.menuHUD = menuHUD;
		this.sceneElementFactory = gameObjectFactory;
		this.gameState = gameState;
		this.mouseState = mouseState;
		this.stringHandler = stringHandler;
		this.assetHandler = assetHandler;

		initContextual();
		initMouse();
	}

	public void setGameObjectManager(GameObjectManager gameObjectManager) {
		this.gameObjectManager = gameObjectManager;
	}

	@Override
	public boolean processAction(GUIAction action) {
		if (action instanceof KeyAction) {
			KeyAction keyAction = (KeyAction) action;
			if (keyAction.getKeyCode() == KeyCode.ESC
					&& keyAction.getType() == KeyActionType.KEY_PRESSED) {
				gameObjectManager.addHUD(menuHUD);
				gameState.setPaused(true);
				action.consume();
				return true;
			}
		}

		return false;
	}

	@Override
	public void update() {
		updateContextual();
		updateMouse();
		super.update();
	}

	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}

	@Override
	public void render(EAdCanvas<?> c) {

	}

	@Override
	public boolean contains(int x, int y) {
		return false;
	}

	// Contextual

	private void initContextual() {
		c = new CaptionImpl();
		c.setFont(new EAdFontImpl(12.0f));
		c.setBubblePaint(new EAdColor(255, 255, 125));
		c.setPadding(10);
		c.setTextPaint(EAdColor.BLACK);
		stringHandler.setString(c.getText(), "");
		contextual = new EAdBasicSceneElement(c);
		contextual.setPosition(new EAdPositionImpl(0, 0, 0.5f, 1.5f));
		contextual.setVarInitialValue(EAdBasicSceneElement.VAR_VISIBLE,
				Boolean.FALSE);
		contextual.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE,
				Boolean.FALSE);
		contextual.getEvents().add(new StayInBoundsEvent(contextual));
		contextual.getEvents().add(new ChaseTheMouseEvent());
		addElement(sceneElementFactory.get(contextual));
	}

	private void updateContextual() {
		DrawableGO<?> go = mouseState.getGameObjectUnderMouse();

		ValueMap valueMap = gameState.getValueMap();
		if (go != null) {
			EAdString name = valueMap.getValue((EAdElement) go.getElement(),
					EAdBasicSceneElement.VAR_NAME);
			if (name != null && !stringHandler.getString(name).equals("")) {
				stringHandler.setString(c.getText(),
						stringHandler.getString(name));

				SceneElementGO<?> cgo = sceneElementFactory.get(contextual);

				cgo.getRenderAsset().update();
				sceneElementFactory.get(contextual).update();

				gameState.getValueMap().setValue(contextual,
						EAdBasicSceneElement.VAR_VISIBLE, true);
			} else {
				gameState.getValueMap().setValue(contextual,
						EAdBasicSceneElement.VAR_VISIBLE, false);

			}
		} else {
			gameState.getValueMap().setValue(contextual,
					EAdBasicSceneElement.VAR_VISIBLE, false);
		}
	}

	// Mouse
	private void initMouse() {
		mouse = new EAdBasicSceneElement(cursor);
		mouse.setId("mouse");
		mouse.setVarInitialValue(EAdBasicSceneElement.VAR_ENABLE, Boolean.FALSE);
		addElement(sceneElementFactory.get(mouse));
	}

	@SuppressWarnings("unchecked")
	private void checkMouseImage() {
		Image newCursor = gameState.getValueMap().getValue(
				SystemFields.MOUSE_CURSOR);
		if (cursor != newCursor) {
			cursor = newCursor;
			if (cursor != null) {

				DrawableAsset<? extends Image, ?> rAsset = (DrawableAsset<? extends Image, ?>) assetHandler
						.getRuntimeAsset(cursor);
				float scale = 1.0f / (rAsset.getWidth() > rAsset.getHeight() ? rAsset
						.getWidth() / CURSOR_SIZE
						: rAsset.getHeight() / CURSOR_SIZE);
				mouse.getDefinition().getResources().addAsset(mouse.getDefinition().getInitialBundle(),
						EAdSceneElementDefImpl.appearance, cursor);
				gameState.getValueMap().setValue(mouse,
						EAdBasicSceneElement.VAR_SCALE, scale);
			}

		}
	}

	private void updateMouse() {
		checkMouseImage();
		int x = gameState.getValueMap().getValue(SystemFields.MOUSE_X);
		int y = gameState.getValueMap().getValue(SystemFields.MOUSE_Y);
		boolean showMouse = gameState.getValueMap().getValue(
				SystemFields.SHOW_MOUSE);

		gameState.getValueMap().setValue(mouse,
				EAdBasicSceneElement.VAR_VISIBLE,
				!(x < 0 || y < 0) && showMouse);
		gameState.getValueMap().setValue(mouse, EAdBasicSceneElement.VAR_X, x);
		gameState.getValueMap().setValue(mouse, EAdBasicSceneElement.VAR_Y, y);
	}

}
