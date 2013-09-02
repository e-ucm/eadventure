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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.google.inject.Inject;
import es.eucm.ead.engine.events.DragEvent;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.interfaces.Game;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.effects.DragEf;
import es.eucm.ead.model.elements.huds.BottomHud;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.params.guievents.DragGEv;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.guievents.enums.DragGEvType;

public class DragGO extends AbstractEffectGO<DragEf> {

	private GUI gui;

	private SceneElementGO target;

	private SceneElementGO parent;

	private SceneElementGO currentGO;

	private int z;

	private float x;

	private float y;

	private SceneElementGO hud;

	private boolean done;

	private float mouseInitX;

	private float mouseInitY;

	private float diffX;

	private float diffY;

	private DragEvent dragEvent;

	private DragGEv dragGEv;

	@Inject
	public DragGO(Game game, GUI gui) {
		super(game);
		this.gui = gui;
		this.dragGEv = new DragGEv();
		this.dragEvent = new DragEvent(dragGEv);
	}

	public void initialize() {
		super.initialize();
		done = false;
		target = (SceneElementGO) action.getTarget();
		parent = (SceneElementGO) target.getParent();
		x = target.getX();
		y = target.getY();
		z = target.getZ();
		hud = gui.getHUD(BottomHud.ID);
		parent.removeActor(target);
		target.setZ(0);
		hud.addSceneElement(target);
		mouseInitX = game.getGameState().getValue(SystemFields.MOUSE_SCENE_X);
		mouseInitY = game.getGameState().getValue(SystemFields.MOUSE_SCENE_Y);
		diffX = x - mouseInitX;
		diffY = y - mouseInitY;
		target.handle(DragEvent.MOUSE_START_DRAG);
	}

	public void act(float delta) {
		GameState gameState = this.game.getGameState();
		target.setX(gameState.getValue(SystemFields.MOUSE_SCENE_X) + diffX);
		target.setY(gameState.getValue(SystemFields.MOUSE_SCENE_Y) + diffY);
		SceneElementGO go = gui.getScene().getFirstGOIn(
				gameState.getValue(SystemFields.MOUSE_SCENE_X),
				gameState.getValue(SystemFields.MOUSE_SCENE_Y), false);
		if (go != currentGO) {
			if (currentGO != null) {
				// SceneElement id and definition id
				dragEvent.reset();
				dragGEv.setCarryElement(target.getName());
				dragGEv.setAction(DragGEvType.EXITED);
				currentGO.handle(dragEvent);
				dragEvent.reset();
				dragGEv.setCarryElement(target.getElement().getDefinition()
						.getId());
				currentGO.handle(dragEvent);
			}
			currentGO = go;
			if (currentGO != null) {
				// SceneElement id and definition id
				dragEvent.reset();
				dragGEv.setCarryElement(target.getName());
				dragGEv.setAction(DragGEvType.ENTERED);
				currentGO.handle(dragEvent);
				dragEvent.reset();
				dragGEv.setCarryElement(target.getElement().getDefinition()
						.getId());
				currentGO.handle(dragEvent);
			}
		}
		done = !Gdx.input.isButtonPressed(Buttons.LEFT);
	}

	public boolean isFinished() {
		return done;
	}

	public void finish() {
		if (effect.isReturnAfterDrag()) {
			target.setX(x);
			target.setY(y);
		} else {
			// XXX Objects can get outside of the window
			target.setX(x
					+ game.getGameState().getValue(SystemFields.MOUSE_SCENE_X)
					- mouseInitX);
			target.setY(y
					+ game.getGameState().getValue(SystemFields.MOUSE_SCENE_Y)
					- mouseInitY);
		}
		target.setZ(z);
		parent.addSceneElement(target);
		target.handle(new DragEvent(MouseGEv.MOUSE_DROP));
		if (currentGO != null) {
			// SceneElement id and definition id
			dragEvent.reset();
			dragGEv.setCarryElement(target.getName());
			dragGEv.setAction(DragGEvType.DROP);
			currentGO.handle(dragEvent);
			dragEvent.reset();
			dragGEv
					.setCarryElement(target.getElement().getDefinition()
							.getId());
			currentGO.handle(dragEvent);
		}
		super.finish();
	}

	public boolean isQueueable() {
		return true;
	}

}
