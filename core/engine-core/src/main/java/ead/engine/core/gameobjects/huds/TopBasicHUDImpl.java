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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.EAdElement;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.predef.events.ChaseMouseEv;
import ead.common.model.predef.events.StayInBoundsEv;
import ead.common.params.fills.Paint;
import ead.common.params.text.EAdString;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.enums.FontStyle;
import ead.common.util.EAdPosition;
import ead.engine.core.game.GameState;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.input.InputHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeDrawable;
import ead.tools.StringHandler;

/**
 * <p>
 * The basic HUD contains the contextual information about elements (i.e. a sign
 * with the element name), and the mouse pointer. Mouse pointer can be disabled
 * through the system field {@link SystemFields#SHOW_MOUSE}
 * </p>
 * 
 */
@Singleton
public class TopBasicHUDImpl extends AbstractHUD implements
		TopBasicHUD {

	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger("BasicHUDImpl");

	private static final int CURSOR_SIZE = 32;

	protected InputHandler inputHandler;

	private StringHandler stringHandler;

	private DrawableGO<?> currentGO;

	// Contextual
	private SceneElement contextual;

	private Caption contextualCaption;

	// Mouse
	private SceneElement mouse;

	private DrawableGO<?> mouseGO;

	private EAdDrawable cursor;

	private EAdBundleId mouseBundle = new EAdBundleId("mouse2");

	private int i = 0;

	@Inject
	public TopBasicHUDImpl(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			StringHandler stringHandler, InputHandler inputHandler) {
		super(assetHandler, gameObjectFactory, gui, gameState,
				eventFactory);
		setPriority(1000);
		this.stringHandler = stringHandler;
		this.inputHandler = inputHandler;
	}

	@Override
	public boolean contains(int x, int y) {
		return false;
	}

	public void init() {
		initContextual();
		initMouse();
		ComplexSceneElement element = new ComplexSceneElement();
		element.getSceneElements().add(contextual);
		element.getSceneElements().add(mouse);
		setElement(element);
	}

	// Contextual
	private void initContextual() {
		contextualCaption = new Caption(new EAdString(
				"engine.contextual"));
		contextualCaption
				.setFont(new BasicFont(16.0f, FontStyle.BOLD));
		contextualCaption.setPadding(0);
		contextualCaption.setTextPaint(Paint.BLACK_ON_WHITE);
		stringHandler.setString(contextualCaption.getText(), "");
		contextual = new SceneElement(contextualCaption);
		contextual.setPosition(new EAdPosition(0, 0, 0.5f, 1.5f));
		contextual.setVarInitialValue(SceneElement.VAR_VISIBLE,
				Boolean.FALSE);
		contextual.setVarInitialValue(SceneElement.VAR_ENABLE,
				Boolean.FALSE);
		contextual.getEvents().add(new StayInBoundsEv(contextual));
		contextual.getEvents().add(new ChaseMouseEv());
	}

	// Mouse
	private void initMouse() {
		mouse = new GhostElement(cursor, null);
		mouse.setVarInitialValue(SceneElement.VAR_ENABLE,
				Boolean.FALSE);
		mouse.setInitialEnable(false);
		mouseGO = sceneElementFactory.get(mouse);
	}

	public void update() {
		updateContextual();
		updateMouse();
		super.update();
	}

	private void updateContextual() {
		DrawableGO<?> go = inputHandler.isProcessingInput() ? inputHandler
				.getGameObjectUnderPointer() : null;
		if (go != currentGO) {
			ValueMap valueMap = gameState.getValueMap();
			if (go != null) {
				EAdElement element = (EAdElement) go.getElement();
				EAdString name = element instanceof EAdSceneElement ? valueMap
						.getValue(((EAdSceneElement) element)
								.getDefinition(),
								SceneElementDef.VAR_DOC_NAME) : null;
				if (name != null
						&& !stringHandler.getString(name).equals("")) {
					stringHandler.setString(
							contextualCaption.getText(),
							stringHandler.getString(name));

					gameState.getValueMap().setValue(contextual,
							SceneElement.VAR_VISIBLE, true);
				} else {
					gameState.getValueMap().setValue(contextual,
							SceneElement.VAR_VISIBLE, false);

				}
			} else {
				gameState.getValueMap().setValue(contextual,
						SceneElement.VAR_VISIBLE, false);
			}
			currentGO = go;
		}
	}

	@SuppressWarnings("unchecked")
	private void checkMouseImage() {
		Image newCursor = gameState.getValueMap().getValue(
				SystemFields.MOUSE_CURSOR);
		if (cursor != newCursor) {
			cursor = newCursor;
			if (cursor != null) {
				RuntimeDrawable<EAdDrawable, ?> rAsset = (RuntimeDrawable<EAdDrawable, ?>) assetHandler
						.getRuntimeAsset(cursor);
				EAdBundleId bundle = i++ % 2 == 1 ? mouse
						.getDefinition().getInitialBundle()
						: mouseBundle;
				mouse.getDefinition()
						.getResources()
						.addAsset(bundle, SceneElementDef.appearance,
								cursor);
				float scale = 1.0f;
				if (rAsset.getWidth() > 0 && rAsset.getHeight() > 0) {
					scale = 1.0f / (rAsset.getWidth() > rAsset
							.getHeight() ? rAsset.getWidth()
							/ CURSOR_SIZE : rAsset.getHeight()
							/ CURSOR_SIZE);

				}
				gameState.getValueMap().setValue(mouse,
						SceneElement.VAR_SCALE, scale);
				gameState.getValueMap().setValue(mouse,
						ResourcedElement.VAR_BUNDLE_ID, bundle);
			}

		}
	}

	private void updateMouse() {
		checkMouseImage();
		int x = gameState.getValueMap()
				.getValue(SystemFields.MOUSE_X);
		int y = gameState.getValueMap()
				.getValue(SystemFields.MOUSE_Y);
		boolean showMouse = gameState.getValueMap().getValue(
				SystemFields.SHOW_MOUSE);

		gameState.getValueMap()
				.setValue(mouse, SceneElement.VAR_X, x);
		gameState.getValueMap()
				.setValue(mouse, SceneElement.VAR_Y, y);

		gameState.getValueMap().setValue(mouse,
				SceneElement.VAR_VISIBLE, showMouse);

		mouseGO.update();
	}

}
