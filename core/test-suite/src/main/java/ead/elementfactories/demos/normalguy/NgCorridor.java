package ead.elementfactories.demos.normalguy;


import java.util.ArrayList;

import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.guievents.DragGEv;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.DragGEvType;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
import ead.common.model.elements.variables.SystemFields;
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
	
	private ArrayList<SceneElement> rooms;
	
	private EmptyCond allRoomsVisited;  // Enables room4's door
	
	
	public NgCorridor() {
		// Set up room's variables
		initVariables();
		// Set the scene background
		setBackground(new SceneElement(new Image("@drawable/bg_prueba.png")));
		getBackground().setId("ng_corridor_bg");
		// Puts main character into the scene
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.TOP_LEFT, 692, 125);	
		ng.setInitialScale(0.8f);
		
		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);	
		d.setLimits(270, 210, 535, 600);
		this.setTrajectoryDefinition(d);

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
	 * Sets the window's position & image
	 */
	private void setWindow() {
		window = new SceneElement(new Image("@drawable/ng_corridor_window.jpg"));
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
		door2.setPosition(Corner.TOP_LEFT, 49, 234);
		
		door3 = new SceneElement(new Image("@drawable/ng_corridor_door3.png"));
		door3.setId("door3");
		door3.setPosition(Corner.TOP_LEFT, 145, 92);
		
		door4 = new SceneElement(new Image("@drawable/ng_corridor_door4.png"));
		door4.setId("door4");
		door4.setPosition(Corner.TOP_LEFT, 597, 8);
		
	}
	
	/**
	 * Adds the others scene elements to the room
	 */
	private void addSceneElements() {
		getSceneElements().add(window);
		getSceneElements().add(door1);
		getSceneElements().add(door2);
		getSceneElements().add(door3);
		getSceneElements().add(door4);
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
		ChangeSceneEf goToRoom1 = new ChangeSceneEf(room1, new FadeInTransition(1000));
		door1.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToRoom1);
		
		ChangeSceneEf goToRoom2 = new ChangeSceneEf(room2, new FadeInTransition(1000));
		door2.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToRoom2);
		
		ChangeSceneEf goToRoom3 = new ChangeSceneEf(room3, new FadeInTransition(1000));
		door3.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToRoom3);
		
		ChangeSceneEf goToRoom4 = new ChangeSceneEf(finalRoom, new FadeInTransition(1000));
		door4.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToRoom4);
		
	}
	
	
}
