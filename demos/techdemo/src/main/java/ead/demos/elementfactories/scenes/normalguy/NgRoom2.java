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


import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.AddActorReferenceEf;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.enums.PhShape;
import ead.common.model.elements.effects.enums.PhType;
import ead.common.model.elements.effects.physics.PhApplyImpluseEf;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.SimpleTrajectoryDefinition;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.predef.effects.SpeakSceneElementEf;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.EAdElementsFactory;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

/**
 * Room 2. Shape & Physics scene together. When principal character turns on the fan, the balls fall down and clears
 * the area for the user to play the puzzle (Drag & Drop and questions)
 */
public class NgRoom2 extends EmptyScene{
	
	private SceneElement ng;
	private SceneElement door;
	private SceneElement wallpaper;
	private SceneElement fan;
	private SceneElement topFan;
	
	
	public NgRoom2() {
		NgCommon.init();
		setBackground(new SceneElement(new Image("@drawable/ng_room2_bg.png")));
		getBackground().setId("ng_room2_bg");
		
		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER , 715, 515);
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
		d.setLimits(445, 490, 800, 600);
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
		door = new SceneElement(new Image("@drawable/ng_room2_door.png"));
		door.setId("ng_room2_door");
		door.setPosition(Corner.TOP_LEFT, 615, 165);
		
		fan = new SceneElement(new Image("@drawable/ng_room2_fan.png"));
		fan.setId("ng_room2_fan");
		fan.setPosition(Corner.TOP_LEFT, 540, 350);
		
		topFan = new SceneElement(new Image("@drawable/ng_room2_fan_piece.png"));
		topFan.setId("ng_room2_fan_piece");
		topFan.setPosition(Corner.CENTER, 582, 402);
		
		wallpaper = new SceneElement(new Image("@drawable/ng_room2_wallpaper.png"));
		wallpaper.setId("ng_room2_wallpaper");
		wallpaper.setPosition(Corner.TOP_LEFT, 5, 59);

		
		setFan();

	}
	
	/**
	 * The fan turns on when its clicked or topFan piece is clicked too
	 */
	private void setFan() {		
		InterpolationEf interpolation = EAdElementsFactory.getInstance().getEffectFactory()
				.getInterpolationEffect(new BasicField<Float>(topFan, 
						SceneElement.VAR_ROTATION), 0, (float) (Math.PI * 2.0), 500, 
						InterpolationLoopType.RESTART, InterpolationType.LINEAR);
		
		topFan.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED ,interpolation);
		
		int height = 427 ; // Top fan's vertical position
		
		EAdField<Integer> mouseX = new BasicField<Integer>(null, new VarDef<Integer>("integer", Integer.class, 0));
		EAdField<Integer> mouseY = new BasicField<Integer>(null, new VarDef<Integer>("integer", Integer.class, 0));
		EAdField<Integer> canyonX = new BasicField<Integer>(topFan, SceneElement.VAR_X);
		EAdField<Integer> canyonY = new BasicField<Integer>(topFan, SceneElement.VAR_Y);

		// Bullet generation
		BezierShape circle = new CircleShape(30, 30, 30, 25);
		circle.setPaint(new LinearGradientFill(ColorFill.TRANSPARENT, ColorFill.TRANSPARENT, 20, 20));
		//circle.setPaint(new LinearGradientFill(ColorFill.LIGHT_GRAY, ColorFill.LIGHT_GRAY, 20, 20));
		EAdSceneElementDef bullet = new SceneElementDef(circle);
		bullet.setId("bullet");

		PhApplyImpluseEf applyForce = new PhApplyImpluseEf();
		applyForce.setForce(new MathOp("([0] - [1]) * 500", mouseX, canyonX), new MathOp("([0] - [1])", mouseY, canyonY));
		AddActorReferenceEf addEffect = new AddActorReferenceEf(bullet, new EAdPosition(Corner.CENTER, 552, height), applyForce);
		
		//interpolation.getNextEffects().add(addEffect);
		topFan.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, addEffect);
		
	}
	
	
	
	/**
	 * Adds the SceneElements in the correct order
	 */
	private void addElementsInOrder() {
		getSceneElements().add(door);
		getSceneElements().add(wallpaper);
		setPhysics();
		getSceneElements().add(fan);
		getSceneElements().add(topFan);
		getSceneElements().add(ng);
	
	}
	
	/**
	 * Sets door behavior
	 */
	public void setDoor(EAdScene corridor) {		
		// Principal character moving to the door
		MoveSceneElementEf move = moveNg(715, 515);
        door.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
        
        move.getNextEffects().add(NgCommon.getLookNorthEffect());
       
        // Define next scene, add next behavior
        ChangeSceneEf corridorScene = new ChangeSceneEf( );
        corridorScene.setId("corridorScene");
		corridorScene.setNextScene(corridor);
		//((NgCorridor)corridorScene.getNextScene()).getNg().setPosition(NgSceneCreator.getRoom2_x(), NgSceneCreator.getRoom2_y());
		move.getNextEffects().add(corridorScene);
		
	}
	
	/**
	 * Sets the x & y coordinates for moving the ng
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
	
	private void setPhysics() {
		int spotX = 100;
		int spotY = 300;
		int desp = 0;
		PhysicsEffect effect = new PhysicsEffect();
		
		BezierShape circle = new CircleShape(20, 20, 20, 60);
		circle.setPaint(new LinearGradientFill(ColorFill.BLACK, new ColorFill(5, 5, 5), 40, 40));

		for (int i = 0; i < 8; i++) {
			desp += 20;
			for (int j = 0; j < 10; j++) {
				SceneElement e = new SceneElement( circle);
				e.setId("ball" + i + "_" + j);
				e.setPosition(new EAdPosition(Corner.CENTER,spotX + i * 20 + desp, spotY + j * 20));
				getSceneElements().add(e);
				effect.addSceneElement(e);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_TYPE, PhType.DYNAMIC);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_RESTITUTION, 0.3f);
				e.setVarInitialValue(PhysicsEffect.VAR_PH_SHAPE, PhShape.CIRCULAR);
				// Ng moving to the selected ball
				MoveSceneElementEf move = new MoveSceneElementEf();
				move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X, SystemFields.MOUSE_SCENE_Y);
		        e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
			}
		}

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(new BasicField<Boolean>(this, BasicScene.VAR_SCENE_LOADED));
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, effect);

		getEvents().add(event);
		
		addGround(effect);
	}
	
	protected void addGround(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(799, 1);
		groundS.setPaint(new LinearGradientFill(ColorFill.TRANSPARENT,
				ColorFill.TRANSPARENT, 799, 1));
		SceneElement ground = new SceneElement(groundS);
		ground.setId("ground");
		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 575));


		effect.addSceneElement(ground);
		getSceneElements().add(ground);


	}
	
	@Override
	public String getSceneDescription() {
		return "A scene with a character moving and talking. Press anywhere in the scene to move the character there. Press on the character to make him talk.";
	}

	public String getDemoName() {
		return "Speak and Move Scene";
	}
}
