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

package ead.engine.core.gameobjects.trajectories.polygon;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;

import com.google.inject.Inject;

import ead.common.interfaces.features.enums.Orientation;
import ead.common.model.elements.enums.CommonStates;
import ead.common.model.elements.trajectories.PolygonTrajectory;
import ead.common.model.params.util.EAdPosition;
import ead.common.model.params.util.Interpolator;
import ead.common.model.params.variables.SystemFields;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.trajectories.AbstractTrajectoryGO;
import ead.tools.pathfinding.PathFinder;

public class PolygonTrajectoryGO extends
		AbstractTrajectoryGO<PolygonTrajectory> {

	private static final int TIME_PER_UNIT = 4000 / 800;

	private PathFinder pathFinder = new PathFinder();

	private List<Integer> path;

	private int currentTarget;

	private int endX;

	private int endY;

	private float totalTime;

	private float currentTime;

	private boolean updateEnd;

	private int diffX;

	private int diffY;

	private int startX;

	private int startY;

	@Inject
	public PolygonTrajectoryGO(GameState gameState) {
		super(gameState);
	}

	public void setElement(PolygonTrajectory trajectory) {
		super.setElement(trajectory);
		List<PolygonPoint> points = new ArrayList<PolygonPoint>();
		for (EAdPosition p : trajectory.getPoints()) {
			points.add(new PolygonPoint(p.getX(), p.getY()));
		}
		Polygon polygon = new Polygon(points);
		pathFinder.setPolygon(polygon);
	}

	@Override
	public void set(SceneElementGO<?> movingElement, int destinyX,
			int destinyY, SceneElementGO<?> target) {
		super.set(movingElement, destinyX, destinyY, target);
		int startX = movingElement.getX();
		int startY = movingElement.getY();
		path = pathFinder.getPath(startX, startY, destinyX, destinyY);
		currentPath = path;
		currentTarget = 0;
		updateEnd = true;
	}

	@Override
	public void update() {
		currentTime += gameState.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE);
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
				movingElement.setState(CommonStates.EAD_STATE_WALKING
						.toString());

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
			currentTime += gameState
					.getValue(SystemFields.ELAPSED_TIME_PER_UPDATE);
			float x = Interpolator.LINEAR.interpolate(currentTime, totalTime,
					diffX);
			float y = Interpolator.LINEAR.interpolate(currentTime, totalTime,
					diffY);
			movingElement.setX((int) (startX + x));
			movingElement.setY((int) (startY + y));
			if (currentTime >= totalTime) {
				updateEnd = true;
				currentTarget += 2;
			}
		}

		if (isDone())
			movingElement.setState(CommonStates.EAD_STATE_DEFAULT.toString());
	}

	@Override
	public boolean isDone() {
		return currentTarget >= path.size();
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

}
