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

package ead.common.importer.subimporters.chapter.scene;

import ead.common.EAdElementImporter;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.Trajectory.Node;
import es.eucm.eadventure.common.data.chapter.Trajectory.Side;

public class TrajectoryImporter implements
		EAdElementImporter<Trajectory, NodeTrajectoryDefinition> {

	@Override
	public NodeTrajectoryDefinition init(Trajectory oldObject) {
		return new NodeTrajectoryDefinition();
	}

	@Override
	public NodeTrajectoryDefinition convert(Trajectory oldObject,
			Object newElement) {
		NodeTrajectoryDefinition trajectory = (NodeTrajectoryDefinition) newElement;

		oldObject.deleteUnconnectedNodes();

		for (Node n : oldObject.getNodes()) {
			trajectory.addNode(n.getID(), n.getX(), n.getY(), n.getScale());
		}

		for (Side s : oldObject.getSides()) {
			trajectory.addSide(s.getIDStart(), s.getIDEnd(), s.getLength());
		}
		
		trajectory.setInitial(oldObject.getInitial().getID());

		return trajectory;
	}

}
