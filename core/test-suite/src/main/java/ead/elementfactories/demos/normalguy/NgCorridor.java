package ead.elementfactories.demos.normalguy;


import java.util.ArrayList;

import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.guievents.KeyGEv;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.trajectories.Side;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.demos.scenes.EmptyScene;

public class NgCorridor extends EmptyScene{
	
	private SceneElement ng;
	private SceneElement window;  // Displays a video
	
	private SceneElement door1;
	private SceneElement door2;
	private SceneElement door3;
	private SceneElement door4;
	
	private SceneElement doorClosed;
	
	private EmptyCond allRoomsVisited;  // Enables room4's door
	
	
	public NgCorridor() {
		// Set up room's variables
		initVariables();
		// Set the scene background
		setBackground(new SceneElement(new Image("@drawable/ng_corridor_bg.png")));
		getBackground().setId("ng_corridor_bg");
		// Puts main character into the scene
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 700, 520);	
		ng.setInitialScale(0.8f);
		
		// Star form node trajectory
		ChangeFieldEf changeSide = new ChangeFieldEf();
		changeSide.addField(new BasicField<Side>(ng, NodeTrajectoryDefinition.VAR_CURRENT_SIDE));
		changeSide.setOperation(new ValueOp(null));
		createNodeTrajectory(changeSide);

		restOfTheRoom();
		
		addSceneElements();
		
		ngMovement();
		
	}
	
	private void initVariables() {
		allRoomsVisited = EmptyCond.FALSE_EMPTY_CONDITION;
	}
	
	/**
	 * Creates the rest of the scene elements
	 */
	public void restOfTheRoom(){
		setDoors();
		setWindow();
	}
	
	/**
	 * Star form node trajectory for this room
	 * @param changeSide
	 */
	private void createNodeTrajectory(ChangeFieldEf changeSide) {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		// 5 nodes
		trajectory.addNode("0", 70, 540, 0.8f);
		trajectory.addNode("1", 270, 235, 0.8f);
		trajectory.addNode("2", 410, 235, 0.8f);
		trajectory.addNode("3", 525, 235, 0.8f);
		trajectory.addNode("4", 730, 540, 0.8f);
		// 10 connections between nodes
		trajectory.addSide("0", "1", 202);
		trajectory.addSide("1", "2", 231);
		trajectory.addSide("2", "3", 217);
		trajectory.addSide("3", "4", 209);
		trajectory.addSide("4", "0", 600);
//		trajectory.addSide("4", "1", 530);
//		trajectory.addSide("4", "2", 418);
//		trajectory.addSide("0", "2", 418);
//		trajectory.addSide("0", "3", 510);
//		trajectory.addSide("3", "1", 380);


		setTrajectoryDefinition(trajectory);
	}
	
	/**
	 * Sets the window's position & image
	 */
	private void setWindow() {
		window = new SceneElement(new Image("@drawable/ng_corridor_window.png"));
		window.setId("window");
		window.setPosition(Corner.TOP_LEFT , 345, 39);
	}
	
	/**
	 * Sets the doors' position & image
	 */
	private void setDoors() {
		door1 = new SceneElement(new Image("@drawable/ng_corridor_door1.png"));
		door1.setId("door1");
		door1.setPosition(Corner.TOP_LEFT, 692, 125);
		
		door2 = new SceneElement(new Image("@drawable/ng_corridor_door2.png"));
		door2.setId("door2");
		door2.setPosition(Corner.TOP_LEFT, 48, 148);
		
		door3 = new SceneElement(new Image("@drawable/ng_corridor_door3.png"));
		door3.setId("door3");
		door3.setPosition(Corner.TOP_LEFT, 145, 8);
		
		door4 = new SceneElement(new Image("@drawable/ng_corridor_door4.png"));
		door4.setId("door4");
		door4.setPosition(Corner.TOP_LEFT, 597, 8);
		
		doorClosed = new SceneElement(new Image("@drawable/ng_corridor_closed.png"));
		doorClosed.setId("doorClosed");
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
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);
		
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
	}
	
	/**
	 * Moves principal character thought the room
	 * @param x 
	 * @param y
	 * @return MoveSceneElementEf with the movement
	 */
	private MoveSceneElementEf moveNg(int x, int y) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("move");
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}
	
	/**
	 * Configures the element's behavior in this scene
	 * @param window
	 * @param room1
	 * @param room2
	 * @param room3
	 * @param finalRoom
	 */
	public void setUpSceneElements(EAdScene window, EAdScene room1, EAdScene room2, EAdScene room3, EAdScene finalRoom) {
		windowBehavior(window);
		doorsBehavior(room1, room2, room3, finalRoom);
	}
	
	/**
	 * Specifies the window's behavior: NgWindow with 'eAdventure.webm' video displayed
	 */
	private void windowBehavior(EAdScene windowScene) {
		// Principal character moving to the window
		MoveSceneElementEf move = moveNg(345, 39); 
		window.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		// Changing the scene: play the video
        ChangeSceneEf toWindowScene = new ChangeSceneEf( );
        toWindowScene.setId("windowScene");
        toWindowScene.setNextScene(windowScene);
        move.getNextEffects().add(toWindowScene);
        
		this.getSceneElements().add(window);
		
	}
	
	/**
	 * Establish door's behavior
	 */
	private void doorsBehavior(EAdScene room1, EAdScene room2, EAdScene room3, EAdScene finalRoom) {
		setMovementAndChangeRoomBehavior(room1, door1);
		setMovementAndChangeRoomBehavior(room2, door2);
		setMovementAndChangeRoomBehavior(room3, door3);
		setMovementAndChangeRoomBehavior(finalRoom, door4);
		doorClosed();
	}
	
	/**
	 * Configures the movement & change room effects
	 * @param room -> where to go through that door
	 * @param element -> door selected
	 */
	private void setMovementAndChangeRoomBehavior(EAdScene room, SceneElement element) {
		// Movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X, SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		element.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		
		// Changing the scene
		ChangeSceneEf goToRoom = new ChangeSceneEf(room, new FadeInTransition(1000));
		move.getNextEffects().add(goToRoom);
		
	}
	
	private void doorClosed() {
		// Message when the main character tries to open the door
		MoveSceneElementEf move = moveNg(597, 8);
		doorClosed.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		
		move.getNextEffects().add(NgCommon.getLookEastEffect());
        
		EAdString talking = new EAdString("I think it is closed dude...");
		SpeakSceneElementEf speak = new SpeakSceneElementEf(talking);
		speak.setElement(ng);
		speak.setId("speakEffect");
		move.getNextEffects().add(speak);
	}
	
	
}
