package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.EAdElementsFactory;
import ead.elementfactories.demos.scenes.EmptyScene;

public class NgRoom3 extends EmptyScene{
	private SceneElement ng;
	private SceneElement door;
	private EAdScene previousScene;
	
	
	public NgRoom3(EAdScene previousScene) {
		this.previousScene = previousScene;
		NgCommon.init();
		setBackground(new SceneElement(new Image("@drawable/ng_room2_bg.jpg")));
		getBackground().setId("background");
		
		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 862, 235);
		ng.setInitialScale(0.8f);
		
		// Character can talk in the scene
		SpeakEf effect = new SpeakSceneElementEf(ng);
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(
						effect.getString(),
						"Oh... this is getting weird... where the heck am I?");

		ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);
		
		// Area where the character can walk
		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(150, 380, 800, 600);
		setTrajectoryDefinition(d);
		
		// Sets up character's movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		
		createElements();
		addElementsInOrder();
		setDoor();
	}
	
	/**
	 * Generates the SceneElements
	 */
	private void createElements() {
		door = new SceneElement(new Image("@drawable/ng_door.png"));
		door.setId("door");
		door.setPosition(Corner.CENTER, 862, 235);

	}
	
	/**
	 * Adds the SceneElements in the correct order
	 */
	private void addElementsInOrder() {
		getSceneElements().add(door);
		getSceneElements().add(ng);
	}
	
	/**
	 * Sets door behavior
	 */
	private void setDoor() {
		ChangeSceneEf goToPreviousScene = new ChangeSceneEf(this.previousScene,
				new FadeInTransition(1000));
		door.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
				goToPreviousScene);
	}
}
