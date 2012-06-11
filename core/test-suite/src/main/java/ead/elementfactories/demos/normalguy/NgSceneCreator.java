package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.scene.EAdScene;

public class NgSceneCreator {
	
	private static NgSceneCreator instance = new NgSceneCreator();
	
	private static NgCorridor corridor;
	private static NgRoom1 room1;
	private static NgRoom2 room2;
	private static NgRoom3 room3;
	private static NgWindow window;
	private static NgFinalRoom finalRoom;
	private static NgCreditsScreen credits;
	private static EAdScene initialScene;
	
	// Coordinates in the corridor
	private static int room1_x;
	private static int room1_y;
	
	private static int room2_x;
	private static int room2_y;
	
	private static int room3_x; 
	private static int room3_y; 
	
	private static int roomf_x;
	private static int roomf_y;
	
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static NgSceneCreator getInstance(EAdScene initScene) {
		initialScene = initScene;
        return instance;
    }
	
	/**
	 * Creates every games' scene and sets up their elemenScene's behavior
	 */
	private NgSceneCreator() {
		NgCommon.init();
		
		room1_x = 650;
		room1_y = 495;
		
		room2_x = 175;
		room2_y = 495;
		
		room3_x = 255; 
		room3_y = 360; 
		
		roomf_x = 565;
		roomf_y = 360;
		
		corridor = new NgCorridor();
		room1 = new NgRoom1();
		room2 = new NgRoom2();
		room3 = new NgRoom3();
		window = new NgWindow();
		finalRoom = new NgFinalRoom();
		credits = new NgCreditsScreen(initialScene);
		
		setElementsBehavior();
		
		
	}
	
	
	private void setElementsBehavior() {
		room1.setUpSceneElements(corridor);
		room2.setDoor(corridor);
		room3.setDoor(corridor);
		corridor.setUpSceneElements(window, room1, room2, room3, finalRoom);
		finalRoom.setHouse(corridor);
	}
	
	public static EAdScene getRoom1() { return room1; }
	
	public static EAdScene getCredits() { return credits; }
	
	public static int getRoom1_x() {
		return room1_x;
	}

	public static int getRoom1_y() {
		return room1_y;
	}

	public static int getRoom2_x() {
		return room2_x;
	}

	public static int getRoom2_y() {
		return room2_y;
	}

	public static int getRoom3_x() {
		return room3_x;
	}

	public static int getRoom3_y() {
		return room3_y;
	}

	public static int getRoomf_x() {
		return roomf_x;
	}

	public static int getRoomf_y() {
		return roomf_y;
	}


}
