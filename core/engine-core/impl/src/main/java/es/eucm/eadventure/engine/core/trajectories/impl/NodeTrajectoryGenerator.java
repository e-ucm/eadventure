package es.eucm.eadventure.engine.core.trajectories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.trajectories.impl.Node;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.model.trajectories.impl.Side;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.EAdRectangle;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.trajectories.Path;
import es.eucm.eadventure.engine.core.trajectories.PathSide;
import es.eucm.eadventure.engine.core.trajectories.TrajectoryGenerator;

@Singleton
@Deprecated
public class NodeTrajectoryGenerator implements
		TrajectoryGenerator<NodeTrajectoryDefinition> {

	private Map<NodeTrajectoryDefinition, List<PathSideImpl>> sides;

	private SceneElementGOFactory gameObjectFactory;

	private ValueMap valueMap;

	@Inject
	public NodeTrajectoryGenerator(SceneElementGOFactory gameObjectFactory,
			ValueMap valueMap) {
		sides = new HashMap<NodeTrajectoryDefinition, List<PathSideImpl>>();
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
	private PathImpl pathToNearestPoint(
			NodeTrajectoryDefinition trajectoryDefinition,
			EAdPosition currentPosition, int toX, int toY,
			SceneElementGO<?> sceneElement) {

		List<PathSideImpl> currentSides = getCurrentValidSides(trajectoryDefinition);

		List<PathImpl> tempPaths = new ArrayList<PathImpl>();

		for (PathSideImpl currentSide : currentSides) {
			List<PathSide> tempSides = new ArrayList<PathSide>();
			tempSides.add(currentSide);
			float distReal = getDistanceFast(currentPosition.getX(),
					currentPosition.getY(), currentSide.getEndNode().getX(),
					currentSide.getEndNode().getY());
			float dist = currentSide.getLength() / currentSide.getRealLength()
					* distReal;
			PathImpl newPath = new PathImpl(dist, Float.MAX_VALUE, tempSides);
			tempPaths.add(newPath);
		}

		List<PathImpl> fullPathList = getFullPathList(trajectoryDefinition,
				tempPaths);

		PathImpl bestPath = getValidPaths(trajectoryDefinition, fullPathList,
				currentPosition, toX, toY, sceneElement);

		if (bestPath != null) {
			// this.nearestX = (int) bestPath.getDestX( );
			// this.nearestY = (int) bestPath.getDestY( );
			// currentSide.put(trajectoryDefinition,
			// bestPath.getSides().get(0));
			// this.getsTo = bestPath.isGetsTo( );
			bestPath.getSides();

		} else {
			Node currentNode = trajectoryDefinition.getInitial();
			// this.nearestX = currentNode.getX( );
			// this.nearestY = currentNode.getY( );
			for (PathSideImpl side : getSides(trajectoryDefinition)) {
				if (side.getStartNode() == currentNode) {
					// currentSide.put(trajectoryDefinition, side);
				}
			}
			// this.getsTo = false;
			bestPath = new PathImpl(currentNode.getX(), currentNode.getY(),
					null);
			bestPath.setGetsTo(false);
			// return new ArrayList<FunctionalSide>( );
		}
		return bestPath;

	}

	/**
	 * Returns the valid starting sides.
	 * <p>
	 * 
	 * @param trajectoryDefinition
	 * 
	 * @return
	 */
	private List<PathSideImpl> getCurrentValidSides(
			NodeTrajectoryDefinition trajectoryDefinition) {
		List<PathSideImpl> tempList = new ArrayList<PathSideImpl>();
		for (PathSideImpl side : getSides(trajectoryDefinition)) {
			if (side.getSide() == getCurrentSide(trajectoryDefinition))
				tempList.add(side);
		}
		return tempList;
	}

	private List<PathSideImpl> getSides(
			NodeTrajectoryDefinition nodeTrajectoryDefinition) {
		if (sides.containsKey(nodeTrajectoryDefinition))
			return sides.get(nodeTrajectoryDefinition);
		List<PathSideImpl> currentSides = new ArrayList<PathSideImpl>();
		for (Side side : nodeTrajectoryDefinition.getSides()) {
			PathSideImpl temp = new PathSideImpl(side,
					nodeTrajectoryDefinition, false, gameObjectFactory,
					valueMap);
			if (!currentSides.contains(temp))
				currentSides.add(temp);
			temp = new PathSideImpl(side, nodeTrajectoryDefinition, true,
					gameObjectFactory, valueMap);
			if (!currentSides.contains(temp))
				currentSides.add(temp);
		}
		for (PathSideImpl side : currentSides) {
			for (PathSideImpl tempSide : currentSides) {
				if (tempSide.getStartNode() == side.getEndNode()
						&& tempSide.getEndNode() != side.getStartNode())
					side.getFollowingSides().add(tempSide);
			}
			if (side.getStartNode() == nodeTrajectoryDefinition.getInitial()) {
				EAdField<Side> currentSide = new EAdFieldImpl<Side>(
						nodeTrajectoryDefinition,
						NodeTrajectoryDefinition.VAR_CURRENT_SIDE);
				valueMap.setValue(currentSide, side.getSide());
			}
		}
		sides.put(nodeTrajectoryDefinition, currentSides);
		return currentSides;
	}

	private Side getCurrentSide(
			NodeTrajectoryDefinition nodeTrajectoryDefinition) {
		Side side = valueMap.getValue(nodeTrajectoryDefinition,
				NodeTrajectoryDefinition.VAR_CURRENT_SIDE);
		return side;
	}

	/**
	 * Get all the possible paths from the list of full paths.
	 * <p>
	 * Example: <code>
	 *    B - D
	 *   /   / \
	 * A    /   \
	 *   \ /     \
	 *    C -----E
	 * </code> with the player in A, the full paths will be: <code>
	 *                     |-D
	 *             |-E---C-|-A
	 *             |
	 *             |   |-A 
	 *   A-|-B---D-|-C-|-D
	 *     |           |-E---D
	 *     |
	 *     |-C-|-D-|-E---C
	 *         |   |-B---A
	 *         |           
	 *         |-E---D-|-C
	 *                 |-B---A
	 * </code>
	 * 
	 * @param tempPaths
	 *            A list of the full paths
	 * @return A list of all the possible paths for the given full paths
	 */
	private List<PathImpl> getFullPathList(
			NodeTrajectoryDefinition nodeTrajectoryDefinition,
			List<PathImpl> tempPaths) {

		List<PathImpl> fullPathList = new ArrayList<PathImpl>();

		while (!tempPaths.isEmpty()) {
			PathImpl originalPath = tempPaths.get(0);
			tempPaths.remove(0);

			PathSideImpl lastSide = (PathSideImpl) originalPath.getSides().get(
					originalPath.getSides().size() - 1);

			boolean continues = false;
			for (PathSideImpl side : lastSide.getFollowingSides()) {
				if (!originalPath.getSides().contains(side)
						&& !originalPath.getNodes().contains(side.getEndNode())) {
					PathImpl temp = originalPath.newFunctionalPath(
							side.getLength(), 0, side);
					if (temp != null) {
						tempPaths.add(temp);
						continues = true;
					}
				}
			}
			if (!continues)
				fullPathList.add(originalPath);
		}
		return fullPathList;
	}

	/**
	 * Returns a list of the valid paths (paths that get to the desired
	 * destination) from a list of all the possible paths form the starting
	 * position. If an element is set as the destination, the valid paths will
	 * be those that get to the influence area of said element.
	 * <p>
	 * The method starts out by precalculating the values for the sides.<br>
	 * Then it searches though every "full path" to determine it's distance to
	 * the destination, if it is inside a active area and the length of the
	 * path, always keeping the best path found up to that moment.<br>
	 * When the side is in the middle (because the player doesn't start out in a
	 * node) then it "moves along" it calculating the different values. When a
	 * side must be search form beging to end, it uses the pre-calculated
	 * values.
	 * 
	 * @param fullPathList
	 *            A list of the paths from the current position
	 * @param fromX
	 *            The current position along the x-axis
	 * @param fromY
	 *            The current position along the y-axis
	 * @param toX
	 *            The destination position along the x-axis
	 * @param toY
	 *            The destination position along the y-axis
	 * @return A list with all the paths that get to the destination.
	 */
	private PathImpl getValidPaths(
			NodeTrajectoryDefinition nodeTrajectoryDefinition,
			List<PathImpl> fullPathList, EAdPosition currentPosition, int toX,
			int toY, SceneElementGO<?> sceneElement) {

		PathImpl best = null;

		for (PathSideImpl side : getSides(nodeTrajectoryDefinition)) {
			side.updateMinimunDistance(toX, toY, sceneElement,
					nodeTrajectoryDefinition.getBarriers());
		}

		for (PathImpl tempPath : fullPathList) {
			PathImpl newPath = new PathImpl(0, Float.MAX_VALUE,
					new ArrayList<PathSide>());
			PathSideImpl firstPath = ((PathSideImpl) tempPath.getSides().get(0));
			float length = getDistance(currentPosition.getX(),
					currentPosition.getY(), firstPath.getEndNode().getX(),
					firstPath.getEndNode().getY())
					/ firstPath.getRealLength()
					* firstPath.getLength();
			newPath.addSide(length, Float.MAX_VALUE, tempPath.getSides().get(0));

			float posX = currentPosition.getX();
			float posY = currentPosition.getY();

			boolean end = false;
			int sideNr = 1;
			while (!end && sideNr <= tempPath.getSides().size()) {
				if (sideNr == 1) {
					Node endNode = ((PathSideImpl) newPath.getSides().get(
							sideNr - 1)).getEndNode();
					float deltaX = endNode.getX() - posX;
					float deltaY = endNode.getY() - posY;

					int delta = (int) (Math.abs(deltaX) > Math.abs(deltaY) ? Math
							.abs(deltaX) : Math.abs(deltaY));

					for (int i = 0; i < delta && !end; i++) {
						posY = posY + deltaY / delta;
						posX = posX + deltaX / delta;
						if (inBarrier(nodeTrajectoryDefinition, posX, posY))
							end = true;
						else if (sceneElement != null
								&& inInfluenceArea(sceneElement, posX, posY)) {
							float dist = 0;
							newPath.updateUpTo(dist, posX, posY);
							newPath.setGetsTo(true);
							end = true;
						} else if (sceneElement == null) {
							float dist = getDistanceFast(posX, posY, toX, toY);
							newPath.updateUpTo(dist, posX, posY);
						}
					}
				} else {
					PathSideImpl side = (PathSideImpl) newPath.getSides().get(
							sideNr - 1);
					end = side.end;
					newPath.updateUpTo(side.dist, side.posX, side.posY);
					newPath.setGetsTo(side.getsTo);
					posX = side.posX;
					posY = side.posY;
				}

				if (best == null) {
					best = newPath;
				} else if (best.compareTo(newPath) < 0) {
					best = newPath;
				}

				if (sideNr < tempPath.getSides().size()) {
					newPath = newPath.newFunctionalPath(tempPath.getSides()
							.get(sideNr).getLength(), Float.MAX_VALUE, tempPath
							.getSides().get(sideNr));
					posX = ((PathSideImpl) tempPath.getSides().get(sideNr))
							.getStartNode().getX();
					posY = ((PathSideImpl) tempPath.getSides().get(sideNr))
							.getStartNode().getY();
				}

				sideNr++;
			}
		}

		return best;
	}

	/**
	 * Returns true if the point is inside a barrier.
	 * 
	 * @param posX
	 *            the position along the x-axis
	 * @param posY
	 *            the position along the y-axis
	 * @return True if the point is inside a barrier
	 */
	private boolean inBarrier(
			NodeTrajectoryDefinition nodeTrajectoryDefinition, float posX,
			float posY) {

		boolean temp = false;
		for (EAdSceneElement barrier : nodeTrajectoryDefinition.getBarriers()) {
			SceneElementGO<?> seGO = ((SceneElementGO<?>) gameObjectFactory
					.get(barrier));
			int x = seGO.getPosition().getJavaX(seGO.getWidth());
			int y = seGO.getPosition().getJavaY(seGO.getHeight());
			if (x < posX && y < posY && x + seGO.getWidth() > posX
					&& y + seGO.getHeight() > posY)
				return true;
		}
		return temp;
	}

	/**
	 * Returns true if the given point is inside the influence area of the
	 * destination element, false in another case.
	 * 
	 * @param posX
	 *            The position along the x-axis
	 * @param posY
	 *            The position along the y-axis
	 * @return True if the point is inside the destination elements influence
	 *         area.
	 */
	private boolean inInfluenceArea(SceneElementGO<?> sceneElement, float posX,
			float posY) {
		if (sceneElement == null)
			return false;
		else {
			EAdRectangle area = valueMap.getValue(sceneElement.getElement(),
					NodeTrajectoryDefinition.VAR_INFLUENCE_AREA);
			if (area == null)
				area = new EAdRectangleImpl(-20, -20,
						sceneElement.getWidth() + 40,
						sceneElement.getHeight() + 40);

			int x1 = (int) (sceneElement.getPosition().getJavaX(sceneElement
					.getWidth())) + area.getX();
			int y1 = (int) (sceneElement.getPosition().getJavaY(sceneElement
					.getHeight())) + area.getY();
			int x2 = x1 + area.getWidth();
			int y2 = y1 + area.getHeight();

			if (posX > x1 && posX < x2 && posY > y1 && posY < y2)
				return true;
		}
		return false;

	}

	/**
	 * Get the distance from one point to another.
	 * 
	 * @param x1
	 *            The position along the x-axis of the first point
	 * @param y1
	 *            The position along the y-axis of the first point
	 * @param x2
	 *            The position along the x-axis of the second point
	 * @param y2
	 *            The position along the y-axis of the second point
	 * @return The distance between to given points
	 */
	public static float getDistance(float x1, float y1, float x2, float y2) {

		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * Get a fast estimate of the distance from one point to another.
	 * 
	 * @param x1
	 *            The position along the x-axis of the first point
	 * @param y1
	 *            The position along the y-axis of the first point
	 * @param x2
	 *            The position along the x-axis of the second point
	 * @param y2
	 *            The position along the y-axis of the second point
	 * @return An estimate of the distance between to given points
	 */
	public static float getDistanceFast(float x1, float y1, float x2, float y2) {

		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

}
