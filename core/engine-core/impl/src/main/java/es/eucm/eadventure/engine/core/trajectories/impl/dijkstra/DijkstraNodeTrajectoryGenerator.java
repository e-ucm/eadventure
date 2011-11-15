package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

@Singleton
public class DijkstraNodeTrajectoryGenerator implements
		TrajectoryGenerator<NodeTrajectoryDefinition> {

	private SceneElementGOFactory gameObjectFactory;

	private ValueMap valueMap;

	public DijkstraNodeTrajectoryGenerator(SceneElementGOFactory gameObjectFactory,
			ValueMap valueMap) {
		this.gameObjectFactory = gameObjectFactory;
		this.valueMap = valueMap;
	}

	@Override
	public Path getTrajectory(NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y) {
		return pathToNearestPoint(trajectoryDefinition, currentPosition, x, y,
				null);
	}

	@Override
	public Path getTrajectory(NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int x, int y,
			SceneElementGO<?> sceneElement) {
		return pathToNearestPoint(trajectoryDefinition, currentPosition, x, y,
				sceneElement);
	}

	@Override
	public boolean canGetTo(NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, SceneElementGO<?> sceneElement) {
		return pathToNearestPoint(trajectoryDefinition, currentPosition,
				sceneElement.getCenterX(), sceneElement.getCenterY(),
				sceneElement).isGetsTo();
	}

	/**
	 * Returns a {@link Path} from the a point to another. If there is a
	 * destinationElement the destination point is ignored.
	 * 
	 * @param fromX
	 *            The current position along the x-axis
	 * @param fromY
	 *            The current position along the y-axis
	 * @param toX
	 *            The current position along the x-axis
	 * @param toY
	 *            The current position along the y-axis
	 * @return The path to the destination
	 */
	private Path pathToNearestPoint(
			NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int toX, int toY,
			SceneElementGO<?> sceneElement) {

		Map<String, DijkstraNode> nodeMap = new HashMap<String, DijkstraNode>();
		List<DijkstraNode> nodeList = new ArrayList<DijkstraNode>();
		
		for (Node node : trajectoryDefinition.getNodes()) {
			DijkstraNode dNode = new DijkstraNode(new EAdPositionImpl(node.getX(), node.getY()));
			dNode.calculateGoalDistance(toX, toY);
			//TODO needs to get the influence area and check if inside
			//dNode.calculateGetsTo(sceneElement);
			nodeMap.put(node.getId(), dNode);
			nodeList.add(dNode);
		}
		
		DijkstraNode currentNode = generateSides(trajectoryDefinition, nodeMap, currentPosition,
				toX, toY, sceneElement);
		
		Map<DijkstraNode, PathInfo> map = new HashMap<DijkstraNode, PathInfo>();
		PathInfo pathInfo = new PathInfo();
		pathInfo.length = 0;
		map.put(currentNode, pathInfo);
		List<DijkstraNode> visitNodes = new ArrayList<DijkstraNode>();
		visitNodes.add(currentNode);
		List<DijkstraNode> visitedNodes = new ArrayList<DijkstraNode>();
		visitedNodes.add(currentNode);
		
		while (!visitNodes.isEmpty()) {
			DijkstraNode node = visitNodes.get(0);
			visitNodes.remove(0);

			if (node.isBreakNode())
				continue;
			
			for (DijkstraPathSide side : node.getSides()) {
				DijkstraNode other = side.getOtherNode(node);

				if (map.get(other) == null)
					map.put(other, new PathInfo());
				
				if (map.get(node).length + side.getLength() < map.get(other).length) {
					map.get(other).length = map.get(node).length + side.getLength();
					map.get(other).path = side;
					if (!visitedNodes.contains(other)) {
						visitNodes.add(other);
						visitedNodes.add(other);
					}
				}
			}
		}
		
		DijkstraNode bestNode = currentNode;
		for (DijkstraNode node : map.keySet()) {
			if (node.isGetsTo() && !bestNode.isGetsTo())
				bestNode = node;
			else if (!node.isGetsTo() && bestNode.isGetsTo())
				continue;
			else if (node.getGoalDistance() < bestNode.getGoalDistance())
				bestNode = node;
		}
		
		DijkstraPath path = new DijkstraPath();
		path.setGetsTo(bestNode.isGetsTo());
		while (bestNode != currentNode) {
			DijkstraPathSide side = map.get(bestNode).path;
			side.setEndPosition(bestNode.getPosition());
			bestNode = side.getOtherNode(bestNode);
			path.addSide(side);
		}
		return path;
	}
	
	private class PathInfo {
		public float length = Integer.MAX_VALUE;
		public DijkstraPathSide path = null;
	}
	
	private DijkstraNode generateSides(NodeTrajectoryDefinition trajectoryDefinition,
			Map<String, DijkstraNode> nodeMap, EAdPosition currentPosition,
			int toX, int toY, SceneElementGO<?> sceneElement) {
		Side currentSide = getCurrentSide(trajectoryDefinition, currentPosition);
		DijkstraNode currentNode = null;
		
		for (Side side : trajectoryDefinition.getSides()) {
			DijkstraNode start = nodeMap.get(side.getIDStart());
			DijkstraNode end = nodeMap.get(side.getIDEnd());
			
			List<DijkstraNode> intersections = new ArrayList<DijkstraNode>();
			intersections.add(start);
			if (side == currentSide) {
				if (currentPosition.getX() == start.getPosition().getX() && currentPosition.getY() == start.getPosition().getY())
					currentNode = start;
				else if (currentPosition.getX() == end.getPosition().getX() && currentPosition.getY() == end.getPosition().getY())
					currentNode = end;
				else {
					currentNode = new DijkstraNode(currentPosition);
					intersections.add(currentNode);
				}
			}
			intersections.add(end);
			
			addClosestPoint(intersections, toX, toY);
			
			addBarrierIntersections(trajectoryDefinition, intersections);
			
			if (sceneElement != null)
				addInfluenceAreaIntersections(sceneElement, intersections);
			
			for (DijkstraNode newNode : intersections) {
				newNode.calculateGoalDistance(toX, toY);
				//TODO needs to get the influence area and check if inside
				//dNode.calculateGetsTo(sceneElement);
			}
			
			for (int i = 0; i < intersections.size() - 1; i++) {
				DijkstraNode s = intersections.get(i);
				DijkstraNode e = intersections.get(i+1);
				double length = getLength(s.getPosition(), e.getPosition());
				DijkstraPathSide pathSide = new DijkstraPathSide(s, e, side.getLength() * length / side.getRealLength(), length, side);
				s.addSide(pathSide);
				e.addSide(pathSide);
			}
		}
		return currentNode;

	}
	
	/**
	 * @param s a position in the 2D plane
	 * @param e a position in the 2D plane
	 * @return The length of the side from e to s
	 */
	public double getLength(EAdPosition s, EAdPosition e) {
		return Math.sqrt(Math.pow(s.getX() - e.getX(), 2) + Math.pow(s.getY() - e.getY(), 2));
	}
	
	
	private void addClosestPoint(List<DijkstraNode> intersections, int toX,
			int toY) {
		for (int i = 0; i < intersections.size() - 1; i++) {
			EAdPosition pos = getClosestPosition(intersections.get(i).getPosition(), intersections.get(i+1).getPosition(), toX, toY);
			if (pos != null) {
				DijkstraNode newNode = new DijkstraNode(pos);
				intersections.add(i + 1, newNode);
				break;
			}
		}
	}

	/**
	 * Inspired by code in http://www.gamedev.net/topic/444154-closest-point-on-a-line/
	 * 
	 * @param A
	 * @param B
	 * @param P
	 * @return
	 */
	private EAdPosition getClosestPosition(EAdPosition A, EAdPosition B, int toX, int toY)
	{
		float APx = toX - A.getX();
		float APy = toY - A.getY();

		float ABx = B.getX() - A.getX();
		float ABy = B.getY() - B.getY();
		
	    float ab2 = ABx*ABx + ABy*ABy;
	    float ap_ab = APx*ABx + APy*ABy;
	    float t = ap_ab / ab2;
	    if (ab2 == 0 || t < 0.0f || t > 1.0f)
	    	return null;
	    float x = A.getX();
	    x += ABx * t;
	    float y = A.getY();
	    y += ABy * t;
	    return new EAdPositionImpl((int) x, (int) y);
	}
	
	/**
	 * Add the intersections to the influence area of the scene element with the current side
	 * 
	 * @param sceneElement the scene element with the influence area
	 * @param intersections the current intersections or nodes of the side
	 */
	private void addInfluenceAreaIntersections(SceneElementGO<?> sceneElement, List<DijkstraNode> intersections) {
		
	}
	
	/**
	 * Add the intersections to the barriers to the list of intersections of the current side
	 * 
	 * @param trajectoryDefinition The trajectory definition, to get the barriers
 	 * @param intersections The current intersections or nodes of the side
	 */
	private void addBarrierIntersections(NodeTrajectoryDefinition trajectoryDefinition, List<DijkstraNode> intersections) {
		for (EAdSceneElement barrier : trajectoryDefinition.getBarriers()) {
			SceneElementGO<?> go = gameObjectFactory.get(barrier);
			EAdField<Boolean> barrierOn = new EAdFieldImpl<Boolean>(barrier,
					NodeTrajectoryDefinition.VAR_BARRIER_ON);
			if (valueMap.getValue(barrierOn)){ 
				RectangleShape rectangle = (RectangleShape) barrier.getAsset(barrier.getInitialBundle(), EAdBasicSceneElement.appearance);
				EAdPosition position = go.getPosition();
				
				int i = 0;
				while (i < intersections.size() - 1) {
					List<DijkstraNode> newIntersections = getIntersections(intersections.get(i), intersections.get(i + 1), rectangle, position);
					for (DijkstraNode newNode : newIntersections)
						newNode.setBreakNode(true);
					intersections.addAll(i + 1, newIntersections);
					i++;
				}
			}
		}

	}
	
	//TODO needs to return the intersections
	private List<DijkstraNode> getIntersections(DijkstraNode start, DijkstraNode end, RectangleShape rectangle, EAdPosition position) {
		List<DijkstraNode> intersections = new ArrayList<DijkstraNode>();
		
		return intersections;
		
	}
	
	/**
	 * Get current side from variables or choose a side from the closest
	 * node if none is available
	 * 
	 * @param nodeTrajectoryDefinition
	 * @param currentPosition
	 * @return
	 */
	private Side getCurrentSide(
			NodeTrajectoryDefinition nodeTrajectoryDefinition,
			EAdPosition currentPosition) {
		Side side = valueMap.getValue(nodeTrajectoryDefinition,
				NodeTrajectoryDefinition.VAR_CURRENT_SIDE);
		if (side == null) {
			int distance = Integer.MAX_VALUE;
			for (Node node : nodeTrajectoryDefinition.getNodes()) {
				int d = (int) Math.sqrt(Math.pow(
						node.getX() - currentPosition.getX(), 2)
						+ Math.pow(node.getX() - currentPosition.getX(), 2));
				if (d < distance) {
					for (Side side2 : nodeTrajectoryDefinition.getSides()) 
						if (side2.getIDEnd().equals(node.getId()) || side2.getIDStart().equals(node.getId())) {
							side = side2;
							distance = d;
						}
				}
			}
		}
		return side;
	}

}
