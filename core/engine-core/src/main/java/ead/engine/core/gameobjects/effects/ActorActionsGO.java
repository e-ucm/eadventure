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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;

import com.google.inject.Inject;

import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.effects.enums.ChangeActorActions;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scenes.EAdGroupElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.guievents.MouseGEv;
import ead.common.util.EAdPosition.Corner;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.InputActionProcessor;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.TweenController;
import ead.engine.core.platform.TweenControllerImpl;

public class ActorActionsGO extends AbstractEffectGO<ActorActionsEf> implements
		InputActionProcessor {

	private SceneElementGOFactory sceneElementFactory;

	private TweenController tweenController;

	private GUI gui;

	private SceneElementGO<?> effectsHUD;

	private SceneElementGO<?> actions;

	@Inject
	public ActorActionsGO(SceneElementGOFactory sceneElementFactory,
			GameState gameState, TweenController tweenController, GUI gui) {
		super(gameState);
		this.tweenController = tweenController;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
	}

	public void initialize() {
		actions = sceneElementFactory.get(getVisualRepresentation());
		actions.setInputProcessor(this);
		for (SceneElementGO<?> child : actions.getChildren()) {
			child.setInputProcessor(this);
		}
		effectsHUD = gui.getHUD(GUI.EFFECTS_HUD_ID);
		effectsHUD.addSceneElement(actions);
	}

	@SuppressWarnings("unchecked")
	protected EAdGroupElement getVisualRepresentation() {
		if (effect.getChange() == ChangeActorActions.SHOW_ACTIONS) {
			EAdSceneElementDef ref = effect.getActionElement();
			if (ref != null) {
				EAdList<EAdSceneElementDef> list = gameState.getValue(ref,
						ActorActionsEf.VAR_ACTIONS);
				if (list != null) {
					int x = gameState.getValue(SystemFields.MOUSE_SCENE_X);
					int y = gameState.getValue(SystemFields.MOUSE_SCENE_Y);
					int gameWidth = gameState.getValue(SystemFields.GAME_WIDTH);
					int gameHeight = gameState
							.getValue(SystemFields.GAME_HEIGHT);
					float radius = gameHeight / 8;
					int maxRadius = gameWidth - x < gameHeight - y ? gameWidth
							- x : gameHeight - y;
					maxRadius = x < maxRadius ? x : maxRadius;
					maxRadius = y < maxRadius ? y : maxRadius;
					radius = Math.min(maxRadius, radius);

					float signum = -1.0f;
					if (x < gameWidth - x) {
						signum = 1.0f;
					}

					float accAngle = (float) -Math.PI / 2;
					if (y < gameHeight - y) {
						accAngle = -accAngle;
					}

					float angle = (float) (Math.PI / 4.5) * signum;

					GroupElement hud = new GroupElement();

					GhostElement bg = new GhostElement();
					bg.setCatchAll(true);
					hud.getSceneElements().add(bg);
					for (EAdSceneElementDef a : list) {
						SceneElement element = new SceneElement(a);
						element.setId(element.getId() + "engine");
						element.setPosition(Corner.CENTER, x, y);
						int targetX = (int) (Math.cos(accAngle) * radius);
						int targetY = (int) (Math.sin(accAngle) * radius);

						Tween.to(
								new BasicField<Integer>(element,
										SceneElement.VAR_X),
								TweenControllerImpl.DEFAULT, 5000.0f).ease(
								Linear.INOUT).targetRelative(targetX).start(
								tweenController.getManager());
						Tween.to(
								new BasicField<Integer>(element,
										SceneElement.VAR_Y),
								TweenControllerImpl.DEFAULT, 500.0f).ease(
								Linear.INOUT).targetRelative(targetY).start(
								tweenController.getManager());
						hud.getSceneElements().add(element);
						accAngle += angle;
					}
					return hud;
				}

			}

		}
		return null;

	}

	@Override
	public SceneElementGO<?> processAction(InputAction<?> action) {
		if (action.getGUIEvent().equals(MouseGEv.MOUSE_LEFT_PRESSED)
				|| action.getGUIEvent().equals(MouseGEv.MOUSE_RIGHT_PRESSED)) {
			effectsHUD.removeSceneElement(actions);
		}
		return null;
	}

}
