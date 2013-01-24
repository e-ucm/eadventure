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
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.physics.PhysicsEffect;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.guievents.MouseGEv;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.scenes.scenes.EmptyScene;

/**
 * Final scene. Main character walks out the house and gets freedom
 */
public class NgFinalRoom extends EmptyScene {

	private SceneElement house;
	private SceneElement ng;
	private SceneElement post;

	public NgFinalRoom() {
		init();
	}

	protected void init() {
		setBackgroundFill(new LinearGradientFill(ColorFill.CYAN,
				ColorFill.BLUE, 800, 500));

		addSky();

		// Set up character's initial position
		ng = new SceneElement(NgCommon.getMainCharacter());
		ng.setPosition(Corner.BOTTOM_CENTER, 629, 579);
		ng.setInitialScale(0.8f);

		createTrajectory();

		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		PhysicsEffect effect = new PhysicsEffect();

		ConditionedEv event = new ConditionedEv();
		OperationCond condition = new OperationCond(new BasicField<Boolean>(
				this, BasicScene.VAR_SCENE_LOADED));
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
		SimpleTrajectory d = new SimpleTrajectory(false);
		d.setLimits(50, 575, 630, 580);
		setTrajectoryDefinition(d);
	}

	private void setElements() {
		house = new SceneElement(new Image("@drawable/ng_finalroom_house.png"));
		house.setPosition(Corner.BOTTOM_CENTER, 660, 580);

		post = new SceneElement(new Image("@drawable/ng_finalroom_post.png"));
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
		ChangeSceneEf corridorScene = new ChangeSceneEf();
		corridorScene.setNextScene(corridor);
		move.getNextEffects().add(corridorScene);
	}

	public void setPost() {
		// The game ends
		MoveSceneElementEf move = moveNg(132, 580);
		post.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		ChangeSceneEf creditsScene = new ChangeSceneEf(NgSceneCreator
				.getCredits(), new FadeInTransition(1000));
		move.getNextEffects().add(creditsScene);

	}

	/**
	 * Moves ng to x & y coordinates
	 * 
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

	private void addSky() {
		EAdSceneElementDef backgroundDef = getBackground().getDefinition();
		backgroundDef.addAsset(SceneElementDef.appearance, new Image(
				"@drawable/sky.png"));

		SceneElementEv event = new SceneElementEv();

		InterpolationEf effect = new InterpolationEf(new BasicField<Integer>(
				getBackground(), SceneElement.VAR_X), 0, -800, 100000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);

		this.getBackground().getEvents().add(event);

	}

	/**
	 * Creates and adds the scene's ground
	 * 
	 * @param effect
	 */
	protected void addGround(PhysicsEffect effect) {
		RectangleShape groundS = new RectangleShape(799, 50);
		groundS.setPaint(new LinearGradientFill(ColorFill.BROWN,
				ColorFill.DARK_BROWN, 799, 50));
		SceneElement ground = new SceneElement(groundS);
		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 575));

		// Moves ng over the ground
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(ng);
		move.setUseTrajectory(true);

		ground.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		effect.addSceneElement(ground);
		getSceneElements().add(ground);

	}

}
