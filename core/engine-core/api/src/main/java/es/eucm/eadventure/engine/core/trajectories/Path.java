package es.eucm.eadventure.engine.core.trajectories;

import java.util.List;

/**
 * A path or section in a trajectory
 */
public interface Path {

	/**
	 * @return Get the {@link PathSide}s in the trajectory
	 */
	List<PathSide> getSides();
	
	boolean isGetsTo();

}
