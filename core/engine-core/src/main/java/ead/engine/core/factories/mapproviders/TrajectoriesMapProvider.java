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

package ead.engine.core.factories.mapproviders;

import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.NodeTrajectory;
import ead.common.model.elements.trajectories.PolygonTrajectory;
import ead.common.model.elements.trajectories.SimpleTrajectory;
import ead.engine.core.gameobjects.trajectories.TrajectoryGO;
import ead.engine.core.gameobjects.trajectories.dijkstra.NodeTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.polygon.PolygonTrajectoryGO;
import ead.engine.core.gameobjects.trajectories.simple.SimpleTrajectoryGO;

public class TrajectoriesMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdTrajectory>, Class<? extends TrajectoryGO<? extends EAdTrajectory>>> {

	public TrajectoriesMapProvider() {
		factoryMap.put(SimpleTrajectory.class, SimpleTrajectoryGO.class);
		factoryMap.put(NodeTrajectory.class, NodeTrajectoryGO.class);
		factoryMap.put(PolygonTrajectory.class, PolygonTrajectoryGO.class);
	}
}
