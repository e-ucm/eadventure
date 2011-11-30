package es.eucm.eadventure.engine.core.trajectories;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.trajectories.TrajectoryDefinition;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;

/**
 * General interface for trajectories generators. Trajectories are build from a
 * {@link TrajectoryDefinition}
 * 
 * 
 * @param <T>
 *            the class of the trajectory definition
 */
public interface TrajectoryGenerator<T extends TrajectoryDefinition> {

	/**
	 * Returns a {@link Path} representing the trajectory to go to the given x
	 * and y coordinates
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to calculate the actual
	 *            trajectory
	 * @param movingElement
	 * 			  the element that is to be moved
	 * @param x
	 *            target x coordinate
	 * @param y
	 *            target y coordinate
	 * @return the path
	 */
	Path getTrajectory(T trajectoryDefinition, EAdElement movingElement,
			int x, int y);

	/**
	 * Returns a {@link Path} representing the trajectory to get as close as
	 * possible to the given {@link SceneElementGO}. The distance to the element
	 * will depend on different variables of the SceneElement.
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to calculate the actual
	 *            trajectory
	 * @param movingElement
	 * 			  the element that is to be moved
	 * @param x
	 *            target x coordinate
	 * @param y
	 *            target y coordinate
	 * @param sceneElement
	 *            The element towards which to move
	 * @return a path
	 */
	Path getTrajectory(T trajectoryDefinition, EAdElement movingElement,
			int x, int y, SceneElementGO<?> sceneElement);

	/**
	 * Returns true if the player can get to the given {@link SceneElementGO}
	 * using the current trajectory definition and all existing obstacles.
	 * 
	 * @param trajectoryDefinition
	 *            the trajectory definition for which to establish if the player
	 *            can get to the element
	 * @param currentPosition
	 *            the start position of the player
	 * @param sceneElement
	 *            the element that needs to be reached
	 * @return true if the element can be reached, false otherwise
	 */
	boolean canGetTo(T trajectoryDefinition, EAdElement movingElement,
			SceneElementGO<?> sceneElement);

}
