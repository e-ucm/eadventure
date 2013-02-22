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
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.predef.effects.SpeakSceneElementEf;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.Side;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position.Corner;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

public class NgCorridor extends EmptyScene {

	private SceneElement ng;
	private SceneElement window; // Displays a video

	private SceneElement door1;
	private SceneElement door2;
	private SceneElement door3;
	private SceneElement door4;

	private SceneElement doorClosed;

	public NgCorridor() {
		// Set up room's variables
		initVariables();
		// Set the scene background
		setBackground(new SceneElement(
				new Image("@drawable/ng_corridor_bg.png")));
		// Puts main character into the scene
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 650, 495);
		ng.setInitialScale(0.8f);

		// Star form node trajectory
		ChangeFieldEf changeSide = new ChangeFieldEf();
		changeSide.addField(new BasicField<Side>(ng,
				NodeTrajectory.VAR_CURRENT_SIDE));
		changeSide.setOperation(new ValueOp(null));
		createNodeTrajectory(changeSide);

		restOfTheRoom();

		addSceneElements();

		ngMovement();

	}

	private void initVariables() {

	}

	/**
	 * Creates the rest of the scene elements
	 */
	public void restOfTheRoom() {
		setDoors();
		setWindow();
	}

	/**
	 * Star form node trajectory for this room
	 * 
	 * @param changeSide
	 */
	private void createNodeTrajectory(ChangeFieldEf changeSide) {
		NodeTrajectory trajectory = new NodeTrajectory();
		// 5 nodes
		trajectory.addNode("0", 175, 495, 0.8f);
		trajectory.addNode("1", 255, 360, 0.8f);
		trajectory.addNode("2", 410, 265, 0.8f);
		trajectory.addNode("3", 565, 360, 0.8f);
		trajectory.addNode("4", 650, 495, 0.8f);
		trajectory.addNode("5", 410, 405, 0.8f);
		// 11 connections between nodes
		trajectory.addSide("0", "1", 157);
		trajectory.addSide("1", "2", 182);
		trajectory.addSide("2", "3", 182);
		trajectory.addSide("3", "4", 160);
		trajectory.addSide("4", "0", 475);
		trajectory.addSide("5", "0", 252);
		trajectory.addSide("5", "1", 182);
		trajectory.addSide("5", "2", 140);
		trajectory.addSide("5", "3", 162);
		trajectory.addSide("5", "4", 257);
		trajectory.addSide("1", "3", 110);

		setTrajectoryDefinition(trajectory);
	}

	/**
	 * Sets the window's position & image
	 */
	private void setWindow() {
		window = new SceneElement(new Image("@drawable/ng_corridor_window.png"));
		window.setPosition(Corner.TOP_LEFT, 345, 39);
	}

	/**
	 * Sets the doors' position & image
	 */
	private void setDoors() {
		door1 = new SceneElement(new Image("@drawable/ng_corridor_door1.png"));
		door1.setPosition(Corner.TOP_LEFT, 692, 125);

		door2 = new SceneElement(new Image("@drawable/ng_corridor_door2.png"));
		door2.setPosition(Corner.TOP_LEFT, 48, 148);

		door3 = new SceneElement(new Image("@drawable/ng_corridor_door3.png"));
		door3.setPosition(Corner.TOP_LEFT, 145, 8);

		door4 = new SceneElement(new Image("@drawable/ng_corridor_door4.png"));
		door4.setPosition(Corner.TOP_LEFT, 597, 8);

		doorClosed = new SceneElement(new Image(
				"@drawable/ng_corridor_closed.png"));
		doorClosed.setPosition(Corner.TOP_LEFT, 570, 72);

	}

	/**
	 * Adds the others scene elements to the room
	 */
	private void addSceneElements() {
		getSceneElements().add(door1);
		getSceneElements().add(door2);
		getSceneElements().add(door3);
		getSceneElements().add(door4);
		getSceneElements().add(doorClosed);
		getSceneElements().add(window);
		getSceneElements().add(ng);
	}

	/**
	 * Defines main character's movement effect
	 */
	private void ngMovement() {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}

	/**
	 * Moves principal character thought the room
	 * 
	 * @param x
	 * @param y
	 * @return MoveSceneElementEf with the movement
	 */
	private MoveSceneElementEf moveNg(int x, int y) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

	/**
	 * Configures the element's behavior in this scene
	 * 
	 * @param window
	 * @param room1
	 * @param room2
	 * @param room3
	 * @param finalRoom
	 */
	public void setUpSceneElements(EAdScene window, EAdScene room1,
			EAdScene room2, EAdScene room3, EAdScene finalRoom) {
		windowBehavior(window);
		doorsBehavior(room1, room2, room3, finalRoom);
	}

	/**
	 * Specifies the window's behavior: NgWindow with 'eAdventure.webm' video
	 * displayed
	 */
	private void windowBehavior(EAdScene windowScene) {
		// Principal character moving to the window
		MoveSceneElementEf move = moveNg(345, 39);
		window.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		// Changing the scene: play the video
		ChangeSceneEf toWindowScene = new ChangeSceneEf();
		toWindowScene.setNextScene(windowScene);
		move.getNextEffects().add(toWindowScene);

		this.getSceneElements().add(window);

	}

	/**
	 * Establish door's behavior
	 */
	private void doorsBehavior(EAdScene room1, EAdScene room2, EAdScene room3,
			EAdScene finalRoom) {
		setMovementAndChangeRoomBehavior(room1, door1, NgSceneCreator
				.getRoom1_x(), NgSceneCreator.getRoom1_y());
		setMovementAndChangeRoomBehavior(room2, door2, NgSceneCreator
				.getRoom2_x(), NgSceneCreator.getRoom2_y());
		setMovementAndChangeRoomBehavior(room3, door3, NgSceneCreator
				.getRoom3_x(), NgSceneCreator.getRoom3_y());
		setMovementAndChangeRoomBehavior(finalRoom, door4, NgSceneCreator
				.getRoomf_x(), NgSceneCreator.getRoomf_y());
		doorClosed();
	}

	/**
	 * Configures the movement & change room effects
	 * 
	 * @param room
	 *            -> where to go through that door
	 * @param element
	 *            -> door selected
	 */
	private void setMovementAndChangeRoomBehavior(EAdScene room,
			SceneElement element, int x, int y) {
		// Movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(x, y);
		move.setSceneElement(ng);
		element.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		// Changing the scene
		ChangeSceneEf goToRoom = new ChangeSceneEf(room, new FadeInTransition(
				1000));
		move.getNextEffects().add(goToRoom);

	}

	private void doorClosed() {
		// Message when the main character tries to open the door
		MoveSceneElementEf move = moveNg(565, 360);
		doorClosed.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		move.getNextEffects().add(NgCommon.getLookEastEffect());

		SpeakSceneElementEf speak = new SpeakSceneElementEf();
		speak.setElement(ng);
		move.getNextEffects().add(speak);
	}

	public SceneElement getNg() {
		return ng;
	}

}
