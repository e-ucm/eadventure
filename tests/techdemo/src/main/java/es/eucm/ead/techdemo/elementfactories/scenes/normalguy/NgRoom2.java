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

package es.eucm.ead.techdemo.elementfactories.scenes.normalguy;

import es.eucm.ead.model.assets.drawable.basics.EAdShape;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.shapes.CircleShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.effects.AddActorReferenceEf;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.enums.PhShape;
import es.eucm.ead.model.elements.effects.enums.PhType;
import es.eucm.ead.model.elements.effects.physics.PhApplyImpulseEf;
import es.eucm.ead.model.elements.effects.physics.PhysicsEf;
import es.eucm.ead.model.elements.effects.sceneelements.MoveSceneElementEf;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.predef.effects.SpeakSceneElementEf;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.elements.trajectories.SimpleTrajectory;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;
import es.eucm.ead.techdemo.elementfactories.EAdElementsFactory;
import es.eucm.ead.techdemo.elementfactories.scenes.scenes.EmptyScene;

/**
 * Room 2. Shape & Physics scene together. When principal character turns on the fan, the balls fall down and clears
 * the area for the user to play the puzzle (Drag & Drop and questions)
 */
public class NgRoom2 extends EmptyScene {

	private SceneElement ng;
	private SceneElement door;
	private SceneElement wallpaper;
	private SceneElement fan;
	private SceneElement topFan;

	public NgRoom2() {
		NgCommon.init();
		setBackground(new SceneElement(new Image("@drawable/ng_room2_bg.png")));

		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 715, 515);
		ng.setInitialScale(0.8f);

		// Character can talk in the scene
		SpeakEf effect = new SpeakSceneElementEf(ng, new EAdString("n.56"));
		EAdElementsFactory.getInstance().getStringFactory().setString(
				effect.getString(),
				"Oh... this is getting weird... where the heck am I?");

		ng.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		// Area where the character can walk
		SimpleTrajectory d = new SimpleTrajectory(false);
		d.setLimits(445, 490, 800, 600);
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

	}

	/**
	 * Generates the SceneElements
	 */
	private void createElements() {
		door = new SceneElement(new Image("@drawable/ng_room2_door.png"));
		door.setPosition(Corner.TOP_LEFT, 615, 165);

		fan = new SceneElement(new Image("@drawable/ng_room2_fan.png"));
		fan.setPosition(Corner.TOP_LEFT, 540, 350);

		topFan = new SceneElement(new Image("@drawable/ng_room2_fan_piece.png"));
		topFan.setPosition(Corner.CENTER, 582, 402);

		wallpaper = new SceneElement(new Image(
				"@drawable/ng_room2_wallpaper.png"));
		wallpaper.setPosition(Corner.TOP_LEFT, 5, 59);

		setFan();

	}

	/**
	 * The fan turns on when its clicked or topFan piece is clicked too
	 */
	private void setFan() {
		InterpolationEf interpolation = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getInterpolationEffect(
						new ElementField(topFan, SceneElement.VAR_ROTATION), 0,
						(float) (Math.PI * 2.0), 500,
						InterpolationLoopType.RESTART, InterpolationType.LINEAR);

		topFan.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, interpolation);

		int height = 427; // Top fan's vertical position

		ElementField mouseX = new ElementField(null, "integer");
		ElementField mouseY = new ElementField(null, "integer");
		ElementField canyonX = new ElementField(topFan, SceneElement.VAR_X);
		ElementField canyonY = new ElementField(topFan, SceneElement.VAR_Y);

		// Bullet generation
		EAdShape circle = new CircleShape(30);
		circle.setPaint(new LinearGradientFill(ColorFill.TRANSPARENT,
				ColorFill.TRANSPARENT, 20, 20));
		//circle.setPaint(new LinearGradientFill(ColorFill.LIGHT_GRAY, ColorFill.LIGHT_GRAY, 20, 20));
		SceneElementDef bullet = new SceneElementDef(circle);

		PhApplyImpulseEf applyForce = new PhApplyImpulseEf();
		applyForce.setForce(new MathOp("([0] - [1]) * 500", mouseX, canyonX),
				new MathOp("([0] - [1])", mouseY, canyonY));
		AddActorReferenceEf addEffect = new AddActorReferenceEf(bullet,
				new Position(Corner.CENTER, 552, height), applyForce);

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
	public void setDoor(Scene corridor) {
		// Principal character moving to the door
		MoveSceneElementEf move = moveNg(715, 515);
		door.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		move.getNextEffects().add(NgCommon.getLookNorthEffect());

		// Define next scene, add next behavior
		ChangeSceneEf corridorScene = new ChangeSceneEf();
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
		move.setSceneElement(ng);
		move.setTargetCoordiantes(x, y);
		return move;
	}

	private void setPhysics() {
		int spotX = 100;
		int spotY = 300;
		int desp = 0;
		PhysicsEf effect = new PhysicsEf();

		EAdShape circle = new CircleShape(20);
		circle.setPaint(new LinearGradientFill(ColorFill.BLACK, new ColorFill(
				5, 5, 5), 40, 40));

		for (int i = 0; i < 8; i++) {
			desp += 20;
			for (int j = 0; j < 10; j++) {
				SceneElement e = new SceneElement(circle);
				e.setPosition(new Position(Corner.CENTER,
						spotX + i * 20 + desp, spotY + j * 20));
				getSceneElements().add(e);
				effect.addSceneElement(e);
				e.putProperty(PhysicsEf.VAR_PH_TYPE, PhType.DYNAMIC);
				e.putProperty(PhysicsEf.VAR_PH_RESTITUTION, 0.3f);
				e.putProperty(PhysicsEf.VAR_PH_SHAPE, PhShape.CIRCULAR);
				// Ng moving to the selected ball
				MoveSceneElementEf move = new MoveSceneElementEf();
				move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
						SystemFields.MOUSE_SCENE_Y);
				e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
			}
		}

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.ADDED, effect);

		addEvent(event);

		addGround(effect);
	}

	protected void addGround(PhysicsEf effect) {
		RectangleShape groundS = new RectangleShape(799, 1);
		groundS.setPaint(new LinearGradientFill(ColorFill.TRANSPARENT,
				ColorFill.TRANSPARENT, 799, 1));
		SceneElement ground = new SceneElement(groundS);
		ground.setPosition(new Position(Corner.CENTER, 400, 575));

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
