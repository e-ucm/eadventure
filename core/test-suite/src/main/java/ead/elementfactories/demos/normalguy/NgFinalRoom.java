package ead.elementfactories.demos.normalguy;

import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.KeyGEv;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.elementfactories.demos.scenes.EmptyScene;

/**
 * Final scene. Main character walks out the house and gets freedom
 */
public class NgFinalRoom extends EmptyScene{
	
	private SceneElement house;
	private SceneElement ng;
	private SceneElement post;
	
	
	public NgFinalRoom() {
		init();
	}
	
	protected void init() {
		setBackgroundFill(new LinearGradientFill(ColorFill.CYAN, ColorFill.BLUE, 800, 500));

		addSky();
		
		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER , 629, 579);
		ng.setInitialScale(0.8f);
				
		createTrajectory();
		
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		
		PhysicsEffect effect = new PhysicsEffect();

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(new BasicField<Boolean>(this, BasicScene.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, effect);

		// getEvents().add(event);
		getBackground().addBehavior(MouseGEv.MOUSE_ENTERED, effect);
		
		addGround(effect);
		setElements();
		addElementsInOrder();
		
		setPost();
	}
	
	private void createTrajectory() {
		SimpleTrajectoryDefinition d = new SimpleTrajectoryDefinition(false);
		d.setLimits(50, 575, 630, 580);
		setTrajectoryDefinition(d);
	}
	
	
	private void setElements() {
		house = new SceneElement(new Image("@drawable/ng_finalroom_house.png"));
		house.setId("house");
		house.setPosition(Corner.BOTTOM_CENTER, 660, 580);
		
		post = new SceneElement(new Image("@drawable/ng_finalroom_post.png"));
		post.setId("post");
		post.setPosition(Corner.BOTTOM_CENTER, 132, 580);
	}
	
	private void addElementsInOrder() {
		getSceneElements().add(house);
		getSceneElements().add(post);
		getSceneElements().add(ng);
	}
	
	public void setHouse(EAdScene corridor) {
		// Principal character moving to the house
		MoveSceneElementEf move = moveNg(630, 300);
        house.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
       
        // Define next scene
        ChangeSceneEf corridorScene = new ChangeSceneEf( );
        corridorScene.setId("corridorScene");
		corridorScene.setNextScene(corridor);
		move.getNextEffects().add(corridorScene);
	}
	
	public void setPost() {
		// To do: finish the game
		MoveSceneElementEf move = moveNg(132, 580);
		post.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
        
		ChangeSceneEf creditsScene = new ChangeSceneEf( );
		creditsScene.setId("creditsScene");
		creditsScene.setNextScene(NgSceneCreator.getCredits());
		move.getNextEffects().add(creditsScene);
		
	}
	
	/**
	 * Moves ng to x & y coordinates
	 * @param x
	 * @param y
	 * @return
	 */
	private MoveSceneElementEf moveNg(int x, int y) {
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("move");
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}
	
	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.getResources().addAsset(
				backgroundDef.getInitialBundle(),
				SceneElementDef.appearance,
				new Image("@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(
				new BasicField<Integer>(getBackground(),
						SceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEvType.FIRST_UPDATE,
				effect);

		this.getBackground().getEvents().add(event);

	}
	
	/**
	 * Creates and adds the scene's ground
	 * @param effect
	 */
	protected void addGround(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(799, 50);
		groundS.setPaint(new LinearGradientFill(ColorFill.BROWN,
				ColorFill.DARK_BROWN, 799, 50));
		SceneElement ground = new SceneElement(groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 575));

		// Moves ng over the ground
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setId("moveCharacter");
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X, SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		ground.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		effect.addSceneElement(ground);
		getSceneElements().add(ground);


	}
	
}
