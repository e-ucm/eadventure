package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.enums.KeyActionType;
import es.eucm.eadventure.common.model.guievents.impl.EAdKeyEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.predef.model.effects.EAdMoveActiveElement;

public class TrajectoriesScene extends EmptyScene {

	public TrajectoriesScene() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.DARK_GRAY,
				EAdColor.LIGHT_GRAY, 800, 600, true));

		EAdBasicSceneElement element = new EAdBasicSceneElement(
				CharacterScene.getStateDrawable());
		element.setId("player");

		element.setScale(3.0f);

		element.setPosition(new EAdPositionImpl(Corner.BOTTOM_CENTER, 400, 300));

		EAdMakeActiveElementEffect effect = new EAdMakeActiveElementEffect(
				element);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);

		element.getEvents().add(event);

		getElements().add(element);

		getBackground().addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				new EAdMoveActiveElement());

		createTrajectory1();
		createTrajectory2();
		createTrajectory3();

	}

	private String calculateNodeId(int i, int j, int max) {
		return "" + (max * i + j);
	}

	private void createTrajectory1() {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		trajectory.addNode("0", 50, 300, 3.0f);
		trajectory.addNode("1", 750, 300, 1.0f);
		trajectory.addSide("0", "1", 700);

		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect();
		effect.setId("changeTrajectory");
		effect.addField(new EAdFieldImpl<TrajectoryDefinition>(this,
				EAdSceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOperation(trajectory));

		getBackground().addBehavior(
				new EAdKeyEventImpl(KeyActionType.KEY_PRESSED, '1'), effect);

		setTrajectoryDefinition(trajectory);
	}

	private void createTrajectory2() {
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

		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect();
		effect.setId("changeTrajectory");
		effect.addField(new EAdFieldImpl<TrajectoryDefinition>(this,
				EAdSceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOperation(trajectory));

		getBackground().addBehavior(
				new EAdKeyEventImpl(KeyActionType.KEY_PRESSED, '2'), effect);
	}

	private void createTrajectory3() {
		NodeTrajectoryDefinition trajectory = new NodeTrajectoryDefinition();
		trajectory.addNode("0", 50, 200, 3.0f);
		trajectory.addNode("1", 750, 200, 1.0f);
		trajectory.addNode("2", 350, 400, 1.0f);
		trajectory.addSide("0", "2", 700);
		trajectory.addSide("1", "2", 700);
		trajectory.addSide("1", "0", 700);

		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect();
		effect.setId("changeTrajectory");
		effect.addField(new EAdFieldImpl<TrajectoryDefinition>(this,
				EAdSceneImpl.VAR_TRAJECTORY_DEFINITION));
		effect.setOperation(new ValueOperation(trajectory));

		getBackground().addBehavior(
				new EAdKeyEventImpl(KeyActionType.KEY_PRESSED, '3'), effect);
	}

	@Override
	public String getSceneDescription() {
		return "Scene te test some trajectories";
	}

	public String getDemoName() {
		return "Trajectories scene";
	}

}
