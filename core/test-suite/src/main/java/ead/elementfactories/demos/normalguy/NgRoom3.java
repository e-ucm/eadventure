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
	private SceneElement evil_ng;
	
	
	public NgRoom3() {
		NgCommon.init();
		setBackground(new SceneElement(new Image("@drawable/ng_room3_bg.png")));
		getBackground().setId("ng_room3_bg");
		
		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 150, 525);
		ng.setInitialScale(0.8f);
		
		// Character can talk in the scene
		SpeakEf effect = new SpeakSceneElementEf(ng);
		EAdElementsFactory
				.getInstance()
				.getStringFactory()
				.setString(
						effect.getString(),
						"There's a strange man over there... I will ask him who is he and where I am");
		effect.getNextEffects().add(NgCommon.getLookSouthEffect());

		ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);
		
		// Area where the character can walk
		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(145, 495, 750, 550);
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
	}
	
	/**
	 * Generates the SceneElements
	 */
	private void createElements() {
		door = new SceneElement(new Image("@drawable/ng_room3_door.png"));
		door.setId("ng_room3_door");
		door.setPosition(Corner.BOTTOM_CENTER, 86, 530);
		
		evil_ng = new SceneElement(new Image("@drawable/evil_man_stand_s_1.png"));
		evil_ng.setId("ng_room3_evil_ng");
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
		ChangeSceneEf goToPreviousScene = new ChangeSceneEf(corridor, new FadeInTransition(1000));
		door.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, goToPreviousScene);
		
		
	}
}
