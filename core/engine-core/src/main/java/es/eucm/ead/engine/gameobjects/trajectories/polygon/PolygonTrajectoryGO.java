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

package es.eucm.ead.engine.gameobjects.trajectories.polygon;

import com.google.inject.Inject;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.engine.gameobjects.trajectories.AbstractTrajectoryGO;
import es.eucm.ead.model.elements.enums.CommonStates;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.trajectories.PolygonTrajectory;
import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.tools.pathfinding.PathFinder;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;

import java.util.ArrayList;
import java.util.List;

public class PolygonTrajectoryGO extends
		AbstractTrajectoryGO<PolygonTrajectory> {

	private static final int TIME_PER_UNIT = 4000 / 800;

	private PathFinder pathFinder = new PathFinder();

	private List<Float> path;

	private int currentTarget;

	private float endX;

	private float endY;

	private float totalTime;

	private float currentTime;

	private boolean updateEnd;

	private float diffX;

	private float diffY;

	private float startX;

	private float startY;

	@Inject
	public PolygonTrajectoryGO(GameState gameState,
			SceneElementFactory sceneElementFactory) {
		super(gameState, sceneElementFactory);
	}

	public void setElement(PolygonTrajectory trajectory) {
		super.setElement(trajectory);
		List<PolygonPoint> points = new ArrayList<PolygonPoint>();
		for (Position p : trajectory.getPoints()) {
			points.add(new PolygonPoint(p.getX(), p.getY()));
		}
		Polygon polygon = new Polygon(points);
		pathFinder.setPolygon(polygon);
	}

	@Override
	public void set(SceneElementGO movingElement, float destinationX,
			float destinationY, SceneElementGO target) {
		super.set(movingElement, destinationX, destinationY, target);
		float startX = movingElement.getX();
		float startY = movingElement.getY();
		path = pathFinder.getPath(startX, startY, destinationX, destinationY);
		currentPath = path;
		currentTarget = 0;
		updateEnd = true;
	}

	@Override
	public void act(float delta) {
		movingElement = sceneElementFactory.get(sceneElement);
		currentTime += gameState.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE,
				0);
		if (updateEnd) {
			updateEnd = false;
			endX = path.get(currentTarget);
			endY = path.get(currentTarget + 1);
			startX = movingElement.getX();
			startY = movingElement.getY();
			diffX = endX - startX;
			diffY = endY - startY;
			float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);
			totalTime = distance * TIME_PER_UNIT;
			currentTime = 0;

			// If it's really going to move...
			if (Math.abs(diffX) > 0.1f || Math.abs(diffY) > 0.1f) {
				movingElement.setState(CommonStates.WALKING.toString());

				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (diffX > 0) {
						movingElement.setOrientation(Orientation.E);
					} else {
						movingElement.setOrientation(Orientation.W);
					}
				} else {
					if (diffY > 0) {
						movingElement.setOrientation(Orientation.S);
					} else {
						movingElement.setOrientation(Orientation.N);
					}
				}
			}
		}

		if (!isDone()) {
			currentTime += gameState.getValue(
					SystemFields.ELAPSED_TIME_PER_UPDATE, 0);
			float x = (currentTime / totalTime) * diffX;
			float y = (currentTime / totalTime) * diffY;
			movingElement.setX((int) (startX + x));
			movingElement.setY((int) (startY + y));
			if (currentTime >= totalTime) {
				updateEnd = true;
				currentTarget += 2;
			}
		}

		if (isDone())
			movingElement.setState(CommonStates.DEFAULT.toString());
	}

	@Override
	public boolean isDone() {
		return currentTarget >= path.size();
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	@Override
	public boolean isReachedTarget() {
		// FIXME
		return false;
	}

}
