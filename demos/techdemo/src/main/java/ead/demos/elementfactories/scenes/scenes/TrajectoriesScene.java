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

package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.KeyGEv;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.KeyEventType;
import ead.common.model.elements.guievents.enums.KeyGEvCode;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.model.elements.trajectories.Side;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.model.predef.effects.MoveActiveElementToMouseEf;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.scenes.normalguy.NgCommon;

public class TrajectoriesScene extends EmptyScene {

	public TrajectoriesScene() {
		NgCommon.init();
		setBackgroundFill(new LinearGradientFill(ColorFill.DARK_GRAY,
				ColorFill.LIGHT_GRAY, 800, 600, true));

		SceneElement element = new SceneElement(NgCommon.getMainCharacter());

		element.setPosition(new EAdPosition(Corner.BOTTOM_CENTER, 400, 300));

		MakeActiveElementEf effect = new MakeActiveElementEf(element);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);

		element.getEvents().add(event);

		getSceneElements().add(element);

		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				new MoveActiveElementToMouseEf());

		ChangeFieldEf changeSide = new ChangeFieldEf();
		changeSide.addField(new BasicField<Side>(element,
				NodeTrajectoryDefinition.VAR_CURRENT_SIDE));
		changeSide.setOperation(new ValueOp(null));

		// Sets up character's movement
		MoveSceneElementEf move = new MoveSceneElementEf();
		move.setTargetCoordiantes(SystemFields.MOUSE_SCENE_X,
				SystemFields.MOUSE_SCENE_Y);
		move.setSceneElement(element);
		move.setUseTrajectory(true);
		getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);

		createTrajectory1(changeSide);
		createTrajectory2(changeSide);
		createTrajectory3(changeSide);

	}

	private String calculateNodeId(int i, int j, int max) {
		return "" + (max * i + j);
	}

	private void createTrajectory1(ChangeFieldEf changeSide) {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		trajectory.addNode("0", 50, 300, 3.0f);
		trajectory.addNode("1", 750, 300, 1.0f);
		trajectory.addSide("0", "1", 700);

		ChangeFieldEf effect = new ChangeFieldEf();
		effect.addField(new BasicField<EAdTrajectoryDefinition>(this,
				BasicScene.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));

		getBackground().addBehavior(
				new KeyGEv(KeyEventType.KEY_PRESSED, KeyGEvCode.NUM_1), effect);

		effect.getNextEffects().add(changeSide);

		setTrajectoryDefinition(trajectory);
	}

	private void createTrajectory2(ChangeFieldEf changeSide) {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		int margin = 60;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 6; j++)
				trajectory.addNode(calculateNodeId(i, j, 6), j * 100 + margin,
						i * 150 + margin, 1.0f);

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 6; j++) {
				String currentNode = calculateNodeId(i, j, 6);
				// North node
				if (i > 0) {
					trajectory.addSide(currentNode,
							calculateNodeId(i - 1, j, 6), 20);
				}
				// South node
				if (i < 3) {
					trajectory.addSide(currentNode,
							calculateNodeId(i + 1, j, 6), 20);
				}

				// West node
				if (j < 0) {
					trajectory.addSide(currentNode,
							calculateNodeId(i, j - 1, 6), 20);
				}

				// East node
				if (j < 5) {
					trajectory.addSide(currentNode,
							calculateNodeId(i, j + 1, 6), 20);
				}

			}

		trajectory.setInitial("0");

		ChangeFieldEf effect = new ChangeFieldEf();
		effect.addField(new BasicField<EAdTrajectoryDefinition>(this,
				BasicScene.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));

		effect.getNextEffects().add(changeSide);

		getBackground().addBehavior(
				new KeyGEv(KeyEventType.KEY_PRESSED, KeyGEvCode.NUM_2), effect);
	}

	private void createTrajectory3(ChangeFieldEf changeSide) {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		trajectory.addNode("0", 50, 200, 3.0f);
		trajectory.addNode("1", 750, 200, 1.0f);
		trajectory.addNode("2", 350, 400, 1.0f);
		trajectory.addSide("0", "2", 700);
		trajectory.addSide("1", "2", 700);
		trajectory.addSide("1", "0", 700);

		ChangeFieldEf effect = new ChangeFieldEf();
		effect.addField(new BasicField<EAdTrajectoryDefinition>(this,
				BasicScene.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));
		effect.getNextEffects().add(changeSide);

		getBackground().addBehavior(
				new KeyGEv(KeyEventType.KEY_PRESSED, KeyGEvCode.NUM_3), effect);
	}

	@Override
	public String getSceneDescription() {
		return "Scene te test some trajectories";
	}

	public String getDemoName() {
		return "Trajectories scene";
	}

}
