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

package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.model.elements.scenes.EAdGroupElement;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.SystemFields;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.EAdShape;
import ead.common.resources.assets.drawable.basics.shapes.BalloonShape;
import ead.common.util.EAdPosition;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.InputActionProcessor;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.input.actions.MouseInputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.drawables.basics.RuntimeCaption;

public class SpeakGO extends AbstractEffectGO<SpeakEf> implements
		InputActionProcessor {

	private GUI gui;

	private SceneElementGOFactory sceneElementFactory;

	private AssetHandler assetHandler;

	private static final int MARGIN_PROPORTION = 35;

	private static final int HEIGHT_PROPORTION = 4;

	private static final int MARGIN = 30;

	private RuntimeCaption<?> caption;

	private boolean finished;

	private boolean gone;

	private SceneElement textSE;

	private float alpha;

	private String previousState;

	private SceneElementGO<?> bubbleDialog;

	private SceneElementGO<?> effectsHud;

	@Inject
	public SpeakGO(GameState gameState, GUI gui,
			SceneElementGOFactory sceneElementFactory, AssetHandler assetHandler) {
		super(gameState);
		this.gui = gui;
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
	}

	@Override
	public SceneElementGO<?> processAction(InputAction<?> action) {
		if (action instanceof MouseInputAction) {
			MouseInputAction mouseAction = (MouseInputAction) action;
			action.consume();
			if (!finished)
				if (mouseAction.getType() == MouseGEvType.PRESSED) {
					if (caption.getTimesRead() >= 1
							|| caption.getCurrentPart() == caption
									.getTotalParts() - 1)
						finished = true;
					else
						caption.goForward(1);
				}
			return bubbleDialog;
		}
		return null;
	}

	@Override
	public void initialize() {
		super.initialize();
		if (effect.getStateField() != null) {
			previousState = gameState.getValue(effect.getStateField());
			gameState.setValue(effect.getStateField(),
					CommonStates.EAD_STATE_TALKING.toString());
		}
		finished = false;
		alpha = 0.0f;
		gone = false;
		effectsHud = gui.getHUD(GUI.EFFECTS_HUD_ID);
		bubbleDialog = sceneElementFactory.get(this.getVisualRepresentation());
		bubbleDialog.setInputProcessor(this);
		effectsHud.addSceneElement(bubbleDialog);
	}

	protected EAdGroupElement getVisualRepresentation() {
		int width = gameState.getValue(SystemFields.GAME_WIDTH);
		int height = gameState.getValue(SystemFields.GAME_HEIGHT);
		int horizontalMargin = width / MARGIN_PROPORTION;
		int verticalMargin = height / MARGIN_PROPORTION;
		int left = horizontalMargin;
		int right = width - horizontalMargin;
		int top = verticalMargin;
		int bottom = height / HEIGHT_PROPORTION + top;

		EAdShape rectangle = null;

		if (effect.getX() != null && effect.getY() != null) {

			Integer xOrigin = gameState.operate(Integer.class, effect.getX());
			Integer yOrigin = gameState.operate(Integer.class, effect.getY());

			xOrigin += effectsHud.getX();
			yOrigin += effectsHud.getY();

			if (yOrigin < height / 2) {
				bottom = height - verticalMargin;
				top = bottom - height / HEIGHT_PROPORTION;
				yOrigin = top - MARGIN * 2;
			} else {
				yOrigin = bottom + MARGIN * 2;
			}

			rectangle = new BalloonShape(left, top, right, bottom, effect
					.getBallonType(), xOrigin, yOrigin);
		} else {
			int offsetY = height / 2 - (bottom - top) / 2;
			top += offsetY;
			bottom += offsetY;
			rectangle = new BalloonShape(left, top, right, bottom, effect
					.getBallonType());
		}

		rectangle.setPaint(effect.getBubbleColor());
		EAdCaption text = effect.getCaption();
		text.setPadding(MARGIN);
		text.setPreferredWidth(right - left);
		text.setPreferredHeight(bottom - top);

		textSE = new SceneElement(text);
		textSE.setPosition(new EAdPosition(left, top));

		GroupElement complex = new GroupElement(rectangle);
		// To capture clicks all over the screen
		GhostElement bg = new GhostElement();
		bg.setCatchAll(true);
		complex.getSceneElements().add(bg);
		complex.getSceneElements().add(textSE);

		caption = (RuntimeCaption<?>) assetHandler.getRuntimeAsset(text);
		caption.reset();

		return complex;
	}

	@Override
	public boolean isFinished() {
		return finished && gone;
	}

	public void update() {
		super.update();

		if (finished) {
			alpha -= 0.003f * gui.getSkippedMilliseconds();
			if (alpha <= 0.0f) {
				alpha = 0.0f;
				gone = true;
			}
		} else {
			if (alpha >= 1.0f) {
				finished = finished || caption.getTimesRead() > 0;
			} else {
				alpha += 0.003f * gui.getSkippedMilliseconds();
				if (alpha > 1.0f) {
					alpha = 1.0f;
				}
			}
		}

		bubbleDialog.setAlpha(alpha);
	}

	@Override
	public void finish() {
		if (effect.getStateField() != null) {
			gameState.setValue(effect.getStateField(), previousState);
		}
		effectsHud.removeSceneElement(bubbleDialog);
		super.finish();
	}

	public boolean isQueueable() {
		return true;
	}

}
