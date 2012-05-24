package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElementDef;

public class NgSceneCreator {
	
	private static NgSceneCreator instance = new NgSceneCreator();
	
	private static NgCorridor corridor;
	private static NgRoom1 room1;
	private static NgRoom2 room2;
	private static NgRoom3 room3;
	private static NgWindow window;
	private static NgFinalRoom finalRoom;
	
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static NgSceneCreator getInstance() {
        return instance;
    }
	
	/**
	 * Creates every games' scene and sets up their elemenScene's behavior
	 */
	private NgSceneCreator() {
		NgCommon.init();
		
		corridor = new NgCorridor();
		room1 = new NgRoom1();
		room2 = new NgRoom2();
		room3 = new NgRoom3();
		window = new NgWindow();
		finalRoom = new NgFinalRoom();
		
		setElementsBehavior();
		
	}
	
	
	private void setElementsBehavior() {
		room1.setUpSceneElements(corridor);
		room2.setDoor(corridor);
		room3.setDoor(corridor);
		// comportamiento de la final room?
		corridor.setUpSceneElements(window, room1, room2, room3, finalRoom);
		
	}
	
	public static EAdScene getRoom1() { return room1; }
	

}
