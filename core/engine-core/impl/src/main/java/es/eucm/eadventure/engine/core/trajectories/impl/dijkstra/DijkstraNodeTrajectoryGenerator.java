package es.eucm.eadventure.engine.core.trajectories.impl.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

/**
 * Dijkstra's algorithm based {@link TrajectoryGenerator} for
 * {@link NodeTrajectoryDefinition} trajectory types.
 * 
 */
@Singleton
public class DijkstraNodeTrajectoryGenerator implements
		TrajectoryGenerator<NodeTrajectoryDefinition> {

	private SceneElementGOFactory gameObjectFactory;

	/**
	 * The values in the game
	 */
	private ValueMap valueMap;

	public DijkstraNodeTrajectoryGenerator(
			SceneElementGOFactory gameObjectFactory, ValueMap valueMap) {
		this.gameObjectFactory = gameObjectFactory;
		this.valueMap = valueMap;
	}

	@Override
	public Path getTrajectory(NodeTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y) {
		return pathToNearestPoint(trajectoryDefinition, movingElement, x, y,
				null);
	}

	@Override
	public Path getTrajectory(NodeTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y,
			SceneElementGO<?> sceneElement) {
		return pathToNearestPoint(trajectoryDefinition, movingElement, x, y,
				sceneElement);
	}

	@Override
	public boolean canGetTo(NodeTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, SceneElementGO<?> sceneElement) {
		return pathToNearestPoint(trajectoryDefinition, movingElement,
				sceneElement.getCenterX(), sceneElement.getCenterY(),
				sceneElement).isGetsTo();
	}

	/**
	 * Returns a {@link Path} from the a point to another.
	 * <p>
	 * Uses an algorithm based on Dijkstra's
	 * (http://en.wikipedia.org/wiki/Dijkstra's_algorithm), where all relevant
	 * points in the representation (including those in each side that are
	 * closest to the target) are turned into nodes.
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
			EAdElement movingElement, int toX, int toY,
			SceneElementGO<?> sceneElement) {

		Map<String, DijkstraNode> nodeMap = new HashMap<String, DijkstraNode>();
		List<DijkstraNode> nodeList = new ArrayList<DijkstraNode>();

		for (Node node : trajectoryDefinition.getNodes()) {
			DijkstraNode dNode = new DijkstraNode(new EAdPositionImpl(
					node.getX(), node.getY()));
			dNode.setScale(node.getScale());
			dNode.calculateGoalDistance(toX, toY);
			nodeMap.put(node.getId(), dNode);
			nodeList.add(dNode);
			dNode.setGetsTo(isGetsTo(dNode.getPosition(), sceneElement));
		}

		DijkstraNode currentNode = generateSides(trajectoryDefinition, nodeMap,
				movingElement, toX, toY, sceneElement);

		Map<DijkstraNode, PathInfo> map = getMap(currentNode);

		return decodePath(currentNode, map);
	}
	
	/**
	 * Generate the map, used to calculate the best path and distance to each node
	 * 
	 * @param currentNode
	 * @return
	 */
	public Map<DijkstraNode, PathInfo> getMap(DijkstraNode currentNode) {
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
					map.get(other).length = map.get(node).length
							+ side.getLength();
					map.get(other).path = side;
					if (!visitedNodes.contains(other)) {
						visitNodes.add(other);
						visitedNodes.add(other);
					}
				}
			}
		}
		return map;
	}

	/**
	 * Decode the Dijkstra path
	 * 
	 * @param currentNode
	 * @param map
	 * @return
	 */
	private DijkstraPath decodePath(DijkstraNode currentNode,
			Map<DijkstraNode, PathInfo> map) {
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
		DijkstraNode lastNode = bestNode;
		path.setGetsTo(lastNode.isGetsTo());
		while (bestNode != currentNode) {
			DijkstraPathSide side = map.get(bestNode).path;
			if (bestNode == lastNode) {
				// This code is needed so that the avatar stops just before
				// reaching a barrier or similar
				// it should not affect anything else.
				int x1 = bestNode.getPosition().getX();
				int y1 = bestNode.getPosition().getY();
				int x21 = side.getOtherNode(bestNode).getPosition().getX() - x1;
				int y21 = side.getOtherNode(bestNode).getPosition().getY() - y1;
				if (x21 != 0)
					x1 += x21 / Math.abs(x21);
				if (y21 != 0)
					y1 += y21 / Math.abs(y21);
				side.setEndPosition(new EAdPositionImpl(x1, y1));
			} else
				side.setEndPosition(bestNode.getPosition());
			side.setEndScale(bestNode.getScale());
			bestNode = side.getOtherNode(bestNode);
			path.addSide(side);
		}
		return path;
	}

	/**
	 * Class to store information about a DijkstraPath, usded in the Dijkstra
	 * algorithm
	 */
	private class PathInfo {
		public float length = Integer.MAX_VALUE;
		public DijkstraPathSide path = null;
	}

	/**
	 * Generate the sides in the Dijkstra trajectory representation. The sides
	 * are assigned to the nodes where they start or end.
	 * 
	 * @param trajectoryDefinition
	 * @param nodeMap
	 * @param currentPosition
	 * @param toX
	 * @param toY
	 * @param sceneElement
	 * @return
	 */
	private DijkstraNode generateSides(
			NodeTrajectoryDefinition trajectoryDefinition,
			Map<String, DijkstraNode> nodeMap, EAdElement movingElement,
			int toX, int toY, SceneElementGO<?> sceneElement) {
		
		Side currentSide = getCurrentSide(trajectoryDefinition, movingElement);
		DijkstraNode currentNode = null;

		for (Side side : trajectoryDefinition.getSides()) {
			DijkstraNode start = nodeMap.get(side.getIDStart());
			DijkstraNode end = nodeMap.get(side.getIDEnd());

			EAdPosition currentPosition = getCurrentPosition(movingElement);
			
			List<DijkstraNode> intersections = new ArrayList<DijkstraNode>();
			intersections.add(start);
			if (side == currentSide) {
				if (currentPosition.getX() == start.getPosition().getX()
						&& currentPosition.getY() == start.getPosition().getY())
					currentNode = start;
				else if (currentPosition.getX() == end.getPosition().getX()
						&& currentPosition.getY() == end.getPosition().getY())
					currentNode = end;
				else {
					currentNode = new DijkstraNode(currentPosition);
					currentNode.setScale(valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_SCALE));
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
				newNode.setGetsTo(isGetsTo(newNode.getPosition(), sceneElement));
			}

			for (int i = 0; i < intersections.size() - 1; i++) {
				DijkstraNode s = intersections.get(i);
				DijkstraNode e = intersections.get(i + 1);
				double length = getLength(s.getPosition(), e.getPosition());
				DijkstraPathSide pathSide = new DijkstraPathSide(s, e,
						side.getLength() * length / side.getRealLength(),
						length, side);
				s.addSide(pathSide);
				e.addSide(pathSide);
			}
		}
		return currentNode;

	}

	/**
	 * @param s
	 *            a position in the 2D plane
	 * @param e
	 *            a position in the 2D plane
	 * @return The length of the side from e to s
	 */
	public double getLength(EAdPosition s, EAdPosition e) {
		return Math.sqrt(Math.pow(s.getX() - e.getX(), 2)
				+ Math.pow(s.getY() - e.getY(), 2));
	}

	/**
	 * Add the closest point (if different to one of the nodes already in the
	 * intersections list) to the intersections list as a new node
	 * 
	 * @param intersections
	 *            the list of nodes in the path
	 * @param toX
	 *            the target x
	 * @param toY
	 *            the target y
	 */
	private void addClosestPoint(List<DijkstraNode> intersections, int toX,
			int toY) {
		for (int i = 0; i < intersections.size() - 1; i++) {
			DijkstraNode newNode = getClosestPosition(intersections.get(i).getPosition(), intersections.get(i).getScale(),
					intersections.get(i + 1).getPosition(), intersections.get(i + 1).getScale(),
					toX, toY);
			if (newNode != null) {
				intersections.add(i + 1, newNode);
				break;
			}
		}
	}

	/**
	 * Inspired by code in
	 * http://www.gamedev.net/topic/444154-closest-point-on-a-line/
	 * 
	 * @param A
	 *            one of the nodes in the the segment
	 * @param B
	 *            the other node in the segment
	 * @param P
	 *            the point
	 * @return null if the closest point is one of the nodes, or a position
	 *         otherwise
	 */
	private DijkstraNode getClosestPosition(EAdPosition A, float scaleA, EAdPosition B, float scaleB,
			int toX, int toY) {
		float APx = toX - A.getX();
		float APy = toY - A.getY();

		float ABx = B.getX() - A.getX();
		float ABy = B.getY() - A.getY();

		float ab2 = ABx * ABx + ABy * ABy;
		float ap_ab = APx * ABx + APy * ABy;
		float t = ap_ab / ab2;
		if (ab2 == 0 || t <= 0.01f || t >= 0.99f)
			return null;
		float x = A.getX();
		x += ABx * t;
		float y = A.getY();
		y += ABy * t;
		DijkstraNode node = new DijkstraNode(new EAdPositionImpl((int) x, (int) y));
		node.setScale(scaleA + t * (scaleB - scaleA));
		return node;
	}

	/**
	 * Add the intersections to the influence area of the scene element with the
	 * current side
	 * 
	 * @param sceneElement
	 *            the scene element with the influence area
	 * @param intersections
	 *            the current intersections or nodes of the side
	 */
	private void addInfluenceAreaIntersections(SceneElementGO<?> sceneElement,
			List<DijkstraNode> intersections) {
		EAdRectangle rectangle = valueMap
				.getValue(new EAdFieldImpl<EAdRectangle>(
						(EAdElement) sceneElement.getElement(),
						NodeTrajectoryDefinition.VAR_INFLUENCE_AREA));
		// TODO check if the position of the element isn't relevant (i.e. if the
		// position of the rectangle is not relative to the element)
		EAdPosition position = new EAdPositionImpl(rectangle.getX(),
				rectangle.getY());

		int i = 0;
		while (i < intersections.size() - 1) {
			List<DijkstraNode> newIntersections = getIntersections(
					intersections.get(i), intersections.get(i + 1),
					rectangle.getWidth(), rectangle.getHeight(), position);
			for (DijkstraNode newNode : newIntersections)
				newNode.setGetsTo(true);
			intersections.addAll(i + 1, newIntersections);
			i += newIntersections.size() + 1;
		}
	}

	private boolean isGetsTo(EAdPosition position,
			SceneElementGO<?> sceneElement) {
		if (sceneElement == null)
			return false;
		EAdRectangle rectangle = valueMap
				.getValue(new EAdFieldImpl<EAdRectangle>(
						(EAdElement) sceneElement.getElement(),
						NodeTrajectoryDefinition.VAR_INFLUENCE_AREA));
		if (rectangle == null)
			return false;
		// TODO check if the position of the element isn't relevant (i.e. if the
		// position of the rectangle is not relative to the element)
		if (rectangle.getX() < position.getX()
				&& rectangle.getY() < position.getY()
				&& rectangle.getX() + rectangle.getWidth() > position.getX()
				&& rectangle.getY() + rectangle.getHeight() > position.getY())
			return true;
		return false;
	}

	/**
	 * Add the intersections to the barriers to the list of intersections of the
	 * current side
	 * 
	 * @param trajectoryDefinition
	 *            The trajectory definition, to get the barriers
	 * @param intersections
	 *            The current intersections or nodes of the side
	 */
	private void addBarrierIntersections(
			NodeTrajectoryDefinition trajectoryDefinition,
			List<DijkstraNode> intersections) {
		for (EAdSceneElement barrier : trajectoryDefinition.getBarriers()) {
			SceneElementGO<?> go = gameObjectFactory.get(barrier);
			EAdField<Boolean> barrierOn = new EAdFieldImpl<Boolean>(barrier,
					NodeTrajectoryDefinition.VAR_BARRIER_ON);
			if (valueMap.getValue(barrierOn)) {
				EAdPosition position = go.getPosition();

				int i = 0;
				while (i < intersections.size() - 1) {
					List<DijkstraNode> newIntersections = getIntersections(
							intersections.get(i), intersections.get(i + 1),
							(int) (go.getWidth() * go.getScale()),
							(int) (go.getHeight() * go.getScale()), position);
					for (DijkstraNode newNode : newIntersections)
						newNode.setBreakNode(true);
					intersections.addAll(i + 1, newIntersections);
					i += newIntersections.size() + 1;
				}
			}
		}

	}

	/**
	 * Returns a list with the {@link DijkstraNode}s representing the intersections
	 * of a line from start to end with the rectangle in the given {@link EAdPosition}
	 * with a width and height
	 * 
	 * @param start The start {@link DijkstraNode}
	 * @param end The end {@link DijkstraNode}
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param position The {@link EAdPosition} of the rectangle
	 * @return A list of the intersections as {@link DijkstraNode}s
	 */
	private List<DijkstraNode> getIntersections(DijkstraNode start,
			DijkstraNode end, int width, int height, EAdPosition position) {
		ArrayList<DijkstraNode> intersections = new ArrayList<DijkstraNode>();

		int x = position.getJavaX(width);
		int y = position.getJavaY(height);

		int startX = start.getPosition().getX();
		int startY = start.getPosition().getY();
		int endX = end.getPosition().getX();
		int endY = end.getPosition().getY();
		float startScale = start.getScale();
		float endScale = end.getScale();

		DijkstraNode temp = getIntersection(startX, startY, startScale,
				endX, endY, endScale, x, y, x + width, y);
		if (temp != null)
			intersections.add(temp);
		temp = getIntersection(startX,
				startY, startScale, endX, endY, endScale, x + width, y, x + width, y + height);
		if (temp != null)
			intersections.add(temp);
		temp = getIntersection(startX, 
				startY, startScale, endX, endY, endScale, x, y + height, x + width, y + height);
		if (temp != null)
			intersections.add(temp);
		temp = getIntersection(startX, startY, startScale, endX, endY, endScale, x, y, x, y + height);
		if (temp != null)
			intersections.add(temp);

		Collections.sort(intersections, new Comparator<DijkstraNode>() {

			@Override
			public int compare(DijkstraNode arg0, DijkstraNode arg1) {
				if (arg0.getLinePosition() < arg1.getLinePosition())
					return -1;
				return 1;
			}

		});

		return intersections;
	}

	/**
	 * Using formula from http://paulbourke.net/geometry/lineline2d/
	 * 
	 * @return
	 */
	private DijkstraNode getIntersection(int x1, int y1, float scale1, int x2, int y2, float scale2,
			int x3, int y3, int x4, int y4) {
		int den = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (den == 0)
			return null;
		int x13 = x1 - x3;
		int y13 = y1 - y3;
		float numA = (x4 - x3) * y13 - (y4 - y3) * x13;
		float numB = (x2 - x1) * y13 - (y2 - y1) * x13;
		float uA = numA / den;
		float uB = numB / den;
		if (uA < 0 || uB < 0 || uA > 1 || uB > 1)
			return null;
		int x = (int) (x1 + uA * (x2 - x1));
		int y = (int) (y1 + uA * (y2 - y1));
		float scale = scale1 + uA * (scale2 - scale1);
		DijkstraNode node = new DijkstraNode(new EAdPositionImpl(x, y), uA);
		node.setScale(scale);
		return node;
	}

	/**
	 * Get current side from variables or choose a side from the closest node if
	 * none is available
	 * 
	 * @param nodeTrajectoryDefinition
	 * @param currentPosition
	 * @return
	 */
	private Side getCurrentSide(
			NodeTrajectoryDefinition nodeTrajectoryDefinition,
			EAdElement movingElement) {
		Side side = valueMap.getValue(movingElement,
				NodeTrajectoryDefinition.VAR_CURRENT_SIDE);
		if (side == null) {
			int distance = Integer.MAX_VALUE;
			for (Node node : nodeTrajectoryDefinition.getNodes()) {
				int d = (int) Math.sqrt(Math.pow(
						node.getX() - valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_X), 2)
						+ Math.pow(node.getY() - valueMap.getValue(movingElement, EAdBasicSceneElement.VAR_Y), 2));
				if (d < distance) {
					for (Side side2 : nodeTrajectoryDefinition.getSides())
						if (side2.getIDEnd().equals(node.getId())
								|| side2.getIDStart().equals(node.getId())) {
							side = side2;
							distance = d;
						}
				}
			}
		}
		return side;
	}
	
	private EAdPosition getCurrentPosition(EAdElement element) {
		int x = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_X);
		int y = valueMap.getValue(element,
				EAdBasicSceneElement.VAR_Y);
		return new EAdPositionImpl(x, y);
	}


}
