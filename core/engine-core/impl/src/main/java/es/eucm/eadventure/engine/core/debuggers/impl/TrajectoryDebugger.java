package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition.Node;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.LineShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.ComposedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;

@Singleton
public class TrajectoryDebugger implements EAdDebugger {

	private GameState gameState;

	private GameObjectFactory gameObjectFactory;

	private ValueMap valueMap;

	private EAdScene currentScene;

	private NodeTrajectoryDefinition currentTrajectory;

	private List<GameObject<?>> gameObjects;

	private List<BezierShape> barriers;

	@Inject
	public TrajectoryDebugger(GameState gameState,
			GameObjectFactory gameObjectFactory, ValueMap valueMap) {
		this.gameState = gameState;
		this.gameObjectFactory = gameObjectFactory;
		this.valueMap = valueMap;
		gameObjects = new ArrayList<GameObject<?>>();
		barriers = new ArrayList<BezierShape>();

	}

	@Override
	public List<GameObject<?>> getGameObjects() {
		if (currentScene != gameState.getScene().getElement()
				|| valueMap.getValue(currentScene,
						EAdSceneImpl.VAR_TRAJECTORY_DEFINITION) != currentTrajectory) {
			createTrajectory();
		}

		if (currentTrajectory != null) {
			int i = 0;
			for (EAdSceneElement e : currentTrajectory.getBarriers()) {
				barriers.get(i)
						.setFill(
								valueMap.getValue(e,
										NodeTrajectoryDefinition.VAR_BARRIER_ON) ? EAdColor.YELLOW
										: EAdColor.TRANSPARENT);
				i++;
			}
		}

		return gameObjects;
	}

	private void createTrajectory() {
		gameObjects.clear();
		currentScene = gameState.getScene().getElement();

		if (currentScene != null) {
			TrajectoryDefinition trajectory = valueMap.getValue(currentScene,
					EAdSceneImpl.VAR_TRAJECTORY_DEFINITION);

			if (trajectory instanceof NodeTrajectoryDefinition) {
				createNodes((NodeTrajectoryDefinition) trajectory);
			}
		}

	}

	private void createNodes(NodeTrajectoryDefinition trajectory) {
		currentTrajectory = trajectory;
		ComposedDrawable map = new ComposedDrawableImpl();
		for (Side s : trajectory.getSides()) {
			int x1 = trajectory.getNodeForId(s.getIDStart()).getX();
			int y1 = trajectory.getNodeForId(s.getIDStart()).getY();
			int x2 = trajectory.getNodeForId(s.getIDEnd()).getX();
			int y2 = trajectory.getNodeForId(s.getIDEnd()).getY();

			LineShape line = new LineShape(x1, y1, x2, y2, 4);
			line.setFill(EAdColor.WHITE);
			map.addDrawable(line);

		}

		for (Node n : trajectory.getNodes()) {
			CircleShape circle = new CircleShape(n.getX(), n.getY(), 20);
			EAdColor color = trajectory.getInitial() == n ? EAdColor.RED
					: EAdColor.BLUE;

			circle.setFill(new EAdBorderedColor(color, EAdColor.BLACK, 2));
			map.addDrawable(circle);
		}

		EAdBasicSceneElement mapElement = new EAdBasicSceneElement(
				"trajectoryMap");
		mapElement.getResources().addAsset(mapElement.getInitialBundle(),
				EAdBasicSceneElement.appearance, map);

		for (EAdSceneElement e : trajectory.getBarriers()) {
			BezierShape s = (BezierShape) e.getAsset(e.getInitialBundle(),
					EAdBasicSceneElement.appearance);
			BezierShape barrier = (BezierShape) s.clone();
			barrier.setFill(EAdColor.YELLOW);
			barriers.add(barrier);
			EAdPosition p = gameObjectFactory.get(e).getPosition();
			map.addDrawable(barrier, p.getX(), p.getY());
		}

		gameObjects.add(gameObjectFactory.get(mapElement));

	}

}
