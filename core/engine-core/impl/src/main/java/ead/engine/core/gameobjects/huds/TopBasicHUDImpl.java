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

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.EAdElement;
import ead.common.model.elements.guievents.enums.KeyEventCode;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.predef.events.ChaseMouseEv;
import ead.common.model.predef.events.StayInBoundsEv;
import ead.common.params.EAdFontImpl;
import ead.common.params.fills.EAdColor;
import ead.common.params.text.EAdString;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.Drawable;
import ead.common.resources.assets.drawable.basics.CaptionImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.GameObjectManager;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.KeyActionImpl;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.RuntimeAsset;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

/**
 * <p>
 * The basic HUD contains the contextual information about elements (i.e. a sign
 * with the element name), and the mouse pointer. Mouse pointer can be disabled
 * through the system field {@link SystemFields#SHOW_MOUSE}
 * </p>
 * 
 */
@Singleton
public class TopBasicHUDImpl extends AbstractHUD implements TopBasicHUD {

	/**
	 * The logger
	 */
	protected static final Logger logger = Logger.getLogger("BasicHUDImpl");

	private static final int CURSOR_SIZE = 32;

	private MenuHUD menuHUD;

	private SceneElementGOFactory sceneElementFactory;

	protected GameState gameState;

	protected InputHandler inputHandler;

	private SceneElementImpl contextual;

	private CaptionImpl c = new CaptionImpl();

	private StringHandler stringHandler;

	private SceneElementImpl mouse;

	private Drawable cursor;

	private AssetHandler assetHandler;

	private GameObjectManager gameObjectManager;

	@Inject
	public TopBasicHUDImpl(MenuHUD menuHUD,
			SceneElementGOFactory gameObjectFactory, GameState gameState,
			InputHandler inputHandler, StringHandler stringHandler, GUI gui,
			AssetHandler assetHandler) {
		super(gui);
		logger.info("New instance");
		this.menuHUD = menuHUD;
		this.sceneElementFactory = gameObjectFactory;
		this.gameState = gameState;
		this.inputHandler = inputHandler;
		this.stringHandler = stringHandler;
		this.assetHandler = assetHandler;
	}

	public void setGameObjectManager(GameObjectManager gameObjectManager) {
		this.gameObjectManager = gameObjectManager;
	}

	@Override
	public boolean processAction(InputAction<?> action) {
		if (action instanceof KeyActionImpl) {
			KeyActionImpl keyAction = (KeyActionImpl) action;
			if (keyAction.getKeyCode() == KeyEventCode.ESC
					&& keyAction.getType() == KeyEventType.KEY_PRESSED) {
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
		if (contextual != null)
			updateContextual();
		if (mouse != null)
			updateMouse();
		super.update();
	}

	public void doLayout(EAdTransformation t) {
		if (contextual == null)
			initContextual();
		if (mouse == null)
			initMouse();

		super.doLayout(t);
	}

	@Override
	public EAdTransformation getTransformation() {
		return EAdTransformationImpl.INITIAL_TRANSFORMATION;
	}

	@Override
	public void render(GenericCanvas<?> c) {

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
		contextual = new SceneElementImpl(c);
		contextual.setPosition(new EAdPosition(0, 0, 0.5f, 1.5f));
		contextual.setVarInitialValue(SceneElementImpl.VAR_VISIBLE,
				Boolean.FALSE);
		contextual.setVarInitialValue(SceneElementImpl.VAR_ENABLE,
				Boolean.FALSE);
		contextual.getEvents().add(new StayInBoundsEv(contextual));
		contextual.getEvents().add(new ChaseMouseEv());
		addElement(sceneElementFactory.get(contextual));
	}

	private void updateContextual() {
		DrawableGO<?> go = inputHandler.getGameObjectUnderPointer();

		ValueMap valueMap = gameState.getValueMap();
		if (go != null) {
			EAdString name = valueMap.getValue((EAdElement) go.getElement(),
					SceneElementImpl.VAR_NAME);
			if (name != null && !stringHandler.getString(name).equals("")) {
				stringHandler.setString(c.getText(),
						stringHandler.getString(name));

				SceneElementGO<?> cgo = sceneElementFactory.get(contextual);

				cgo.getRenderAsset().update();
				sceneElementFactory.get(contextual).update();

				gameState.getValueMap().setValue(contextual,
						SceneElementImpl.VAR_VISIBLE, true);
			} else {
				gameState.getValueMap().setValue(contextual,
						SceneElementImpl.VAR_VISIBLE, false);

			}
		} else {
			gameState.getValueMap().setValue(contextual,
					SceneElementImpl.VAR_VISIBLE, false);
		}
	}

	// Mouse
	private void initMouse() {
		mouse = new SceneElementImpl(cursor);
		mouse.setId("mouse");
		mouse.setVarInitialValue(SceneElementImpl.VAR_ENABLE, Boolean.FALSE);
		addElement(sceneElementFactory.get(mouse));
	}

	@SuppressWarnings("unchecked")
	private void checkMouseImage() {
		ImageImpl newCursor = gameState.getValueMap().getValue(
				SystemFields.MOUSE_CURSOR);
		if (cursor != newCursor) {
			cursor = newCursor;
			if (cursor != null) {
				DrawableAsset<Drawable, ?> rAsset = (DrawableAsset<Drawable, ?> ) assetHandler
						.getRuntimeAsset(cursor);
				logger.info("width" + rAsset.getWidth());
				rAsset.loadAsset();
				float scale = 1.0f / (rAsset.getWidth() > rAsset.getHeight() ? rAsset
						.getWidth() / CURSOR_SIZE
						: rAsset.getHeight() / CURSOR_SIZE);
				mouse.getDefinition()
						.getResources()
						.addAsset(mouse.getDefinition().getInitialBundle(),
								SceneElementDefImpl.appearance, cursor);
				gameState.getValueMap().setValue(mouse,
						SceneElementImpl.VAR_SCALE, scale);
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
				SceneElementImpl.VAR_VISIBLE,
				!(x < 0 || y < 0) && showMouse);
		if (x >= 0 && y >= 0) {
			gameState.getValueMap().setValue(mouse, SceneElementImpl.VAR_X,
					x);
			gameState.getValueMap().setValue(mouse, SceneElementImpl.VAR_Y,
					y);
		}
	}

}
