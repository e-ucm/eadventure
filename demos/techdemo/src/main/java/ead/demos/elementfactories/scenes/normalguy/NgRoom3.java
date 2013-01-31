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

package ead.demos.elementfactories.scenes.normalguy;

import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position.Corner;
import ead.common.model.params.variables.SystemFields;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

public class NgRoom3 extends EmptyScene {
	private SceneElement ng;
	private SceneElement door;
	private SceneElement evil_ng;

	public NgRoom3() {
		NgCommon.init();
		setBackground(new SceneElement(new Image("@drawable/ng_room3_bg.png")));

		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 150, 525);
		ng.setInitialScale(0.8f);

		// Character can talk in the scene
		SpeakEf effect = new SpeakSceneElementEf(ng);
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(effect.getString(),
						"There's a strange man over there... I will ask him who is he and where I am");
		effect.getNextEffects().add(NgCommon.getLookSouthEffect());

		ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		// Area where the character can walk
		SimpleTrajectory d = new SimpleTrajectory(false);
		d.setLimits(145, 495, 750, 550);
		setTrajectoryDefinition(d);

		// Sets up character's movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		createElements();
		addElementsInOrder();

		setEvilGuy();
	}

	/**
	 * Generates the SceneElements
	 */
	private void createElements() {
		door = new SceneElement(new Image("@drawable/ng_room3_door.png"));
		door.setPosition(Corner.BOTTOM_CENTER, 86, 530);

		evil_ng = new SceneElement(
				new Image("@drawable/evil_man_stand_s_1.png"));
		evil_ng.setInitialScale(0.9f);
		evil_ng.setPosition(Corner.BOTTOM_CENTER, 660, 510);

	}

	/**
	 * Adds the SceneElements in the correct order
	 */
	private void addElementsInOrder() {
		getSceneElements().add(door);
		getSceneElements().add(evil_ng);
		getSceneElements().add(ng);
	}

	/**
	 * Sets door behavior
	 */
	public void setDoor(EAdScene corridor) {
		ChangeSceneEf goToPreviousScene = new ChangeSceneEf(corridor,
				new FadeInTransition(1000));
		door.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToPreviousScene);

	}

	/**
	 * The evil guy drives to the user to a Quiz game
	 */
	private void setEvilGuy() {
		// MoveSceneElementEf move = moveNg(560, 510);

		SpeakSceneElementEf speech = new SpeakSceneElementEf();
		speech.setElement(evil_ng);

		ChangeSceneEf changeScene = new ChangeSceneEf(new NgQuiz(),
				new FadeInTransition(1000));

		// move.getNextEffects().add(speech);
		speech.getNextEffects().add(changeScene);
		evil_ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, speech);
	}

	/**
	 * Sets the x & y coordinates for moving the ng
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	/*
	 * private MoveSceneElementEf moveNg(int x, int y) { MoveSceneElementEf move
	 * = new MoveSceneElementEf(); move.setId("move"); move.setSceneElement(ng);
	 * move.setTargetCoordiantes(x, y); return move; }
	 */
}
