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

package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.events.SceneElementEv;
import es.eucm.eadventure.common.model.elements.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.elements.guievents.EAdKeyEvent;
import es.eucm.eadventure.common.model.elements.guievents.EAdMouseEvent;
import es.eucm.eadventure.common.model.elements.guievents.enums.KeyEventType;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.common.model.elements.trajectories.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.elements.trajectories.Side;
import es.eucm.eadventure.common.model.elements.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.ValueOp;
import es.eucm.eadventure.common.model.predef.effects.MakeActiveElementEf;
import es.eucm.eadventure.common.model.predef.effects.MoveActiveElementEf;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.util.EAdPosition;
import es.eucm.eadventure.common.util.EAdPosition.Corner;

public class TrajectoriesScene extends EmptyScene {

	public TrajectoriesScene() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.DARK_GRAY,
				EAdColor.LIGHT_GRAY, 800, 600, true));

		SceneElementImpl element = new SceneElementImpl(
				CharacterScene.getStateDrawable());
		element.setId("player");

		element.setScale(3.0f);

		element.setPosition(new EAdPosition(Corner.BOTTOM_CENTER, 400, 300));

		MakeActiveElementEf effect = new MakeActiveElementEf(
				element);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);

		element.getEvents().add(event);

		getComponents().add(element);

		getBackground().addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK,
				new MoveActiveElementEf());

		ChangeFieldEf changeSide = new ChangeFieldEf();
		changeSide.addField(new FieldImpl<Side>( element, NodeTrajectoryDefinition.VAR_CURRENT_SIDE));
		changeSide.setOperation(new ValueOp( null ));
		
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
		effect.setId("changeTrajectory");
		effect.addField(new FieldImpl<TrajectoryDefinition>(this,
				SceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));

		getBackground().addBehavior(
				new EAdKeyEvent(KeyEventType.KEY_PRESSED, '1'), effect);
		
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
		effect.setId("changeTrajectory");
		effect.addField(new FieldImpl<TrajectoryDefinition>(this,
				SceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));
		
		effect.getNextEffects().add(changeSide);

		getBackground().addBehavior(
				new EAdKeyEvent(KeyEventType.KEY_PRESSED, '2'), effect);
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
		effect.setId("changeTrajectory");
		effect.addField(new FieldImpl<TrajectoryDefinition>(this,
				SceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOp(trajectory));
		effect.getNextEffects().add(changeSide);

		getBackground().addBehavior(
				new EAdKeyEvent(KeyEventType.KEY_PRESSED, '3'), effect);
	}

	@Override
	public String getSceneDescription() {
		return "Scene te test some trajectories";
	}

	public String getDemoName() {
		return "Trajectories scene";
	}

}
