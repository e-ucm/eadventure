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

package es.eucm.ead.engine.gameobjects.effects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.google.inject.Inject;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ActorActionsEf;
import es.eucm.ead.model.elements.effects.enums.ChangeActorActions;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.util.Position.Corner;

public class ActorActionsGO extends AbstractEffectGO<ActorActionsEf> implements
		EventListener {

	private SceneElementFactory sceneElementFactory;

	private GUI gui;

	private SceneElementGO effectsHUD;

	private SceneElementGO actions;

	@Inject
	public ActorActionsGO(SceneElementFactory sceneElementFactory, Game game) {
		super(game);
		this.sceneElementFactory = sceneElementFactory;
		this.gui = game.getGUI();
	}

	public void initialize() {
		GroupElement rep = getVisualRepresentation();
		if (rep != null) {
			actions = sceneElementFactory.get(getVisualRepresentation());
			actions.setInputProcessor(this, false);
			for (Actor child : actions.getChildren()) {
				((SceneElementGO) child).setInputProcessor(this, false);
			}
			effectsHUD = gui.getHUD(GUI.EFFECTS_HUD_ID);
			effectsHUD.addSceneElement(actions);
		}
	}

	@SuppressWarnings("unchecked")
	protected GroupElement getVisualRepresentation() {
		if (effect.getChange() == ChangeActorActions.SHOW_ACTIONS) {
			SceneElementDef ref = effect.getActionElement();
			GameState gameState = this.game.getGameState();
			if (ref != null) {
				EAdList<SceneElementDef> list = ref.getProperty(
						ActorActionsEf.VAR_ACTIONS, null);
				if (list != null) {
					float x = gameState
							.getValue(SystemFields.MOUSE_SCENE_X, 0f);
					float y = gameState
							.getValue(SystemFields.MOUSE_SCENE_Y, 0f);
					int gameWidth = gameState.getValue(SystemFields.GAME_WIDTH,
							800);
					int gameHeight = gameState.getValue(
							SystemFields.GAME_HEIGHT, 600);
					float radius = gameHeight / 8;
					float maxRadius = gameWidth - x < gameHeight - y ? gameWidth
							- x
							: gameHeight - y;
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

					float angle = (float) (Math.PI / 2.5) * signum;

					GroupElement hud = new GroupElement();

					GhostElement bg = new GhostElement();
					bg.setCatchAll(true);
					hud.getSceneElements().add(bg);
					boolean hasEnableActions = false;
					for (SceneElementDef a : list) {
						Condition cond = a.getProperty(
								ActorActionsEf.VAR_ACTION_COND, EmptyCond.TRUE);
						if (gameState.evaluate(cond)) {
							hasEnableActions = true;
							SceneElement element = new SceneElement(a);
							element.addProperties(a.getProperties());
							element.setPosition(Corner.CENTER, x, y);
							int targetX = (int) (Math.cos(accAngle) * radius);
							int targetY = (int) (Math.sin(accAngle) * radius);

							Tween.to(
									new ElementField(element,
											SceneElement.VAR_X), 0, 500.0f)
									.ease(Linear.INOUT).targetRelative(targetX)
									.start(this.game.getTweenManager());
							Tween.to(
									new ElementField(element,
											SceneElement.VAR_Y), 0, 500.0f)
									.ease(Linear.INOUT).targetRelative(targetY)
									.start(this.game.getTweenManager());
							hud.getSceneElements().add(element);
							accAngle += angle;
						}
					}
					return hasEnableActions ? hud : null;
				}

			}

		}
		return null;

	}

	@Override
	public boolean handle(Event event) {
		if (event instanceof InputEvent) {
			InputEvent e = (InputEvent) event;
			if (e.getType() == InputEvent.Type.touchDown
					&& e.getButton() == Input.Buttons.LEFT) {
				actions.remove();
			}
		}
		return false;
	}

}
