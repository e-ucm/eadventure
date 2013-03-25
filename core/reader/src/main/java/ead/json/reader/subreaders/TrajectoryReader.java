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

package ead.json.reader.subreaders;

import java.util.Collection;

import com.google.gson.internal.StringMap;

import ead.common.model.elements.trajectories.EAdTrajectory;
import ead.common.model.elements.trajectories.NodeTrajectory;

public class TrajectoryReader {

	public EAdTrajectory read(StringMap<Object> t) {
		String type = t.get("type").toString();
		if (type.equals("nodes")) {
			return parseNodes(t);
		}
		return null;
	}

	private EAdTrajectory parseNodes(StringMap<Object> t) {
		NodeTrajectory trajectory = new NodeTrajectory();
		Collection<StringMap<Object>> ns = (Collection<StringMap<Object>>) t
				.get("nodes");
		for (StringMap<Object> n : ns) {
			Number x = (Number) n.get("x");
			Number y = (Number) n.get("y");
			String nodeId = (String) n.get("nodeId");
			Number scale = (Number) n.get("scale");
			trajectory.addNode(nodeId, x.intValue(), y.intValue(), scale
					.floatValue());
		}
		Collection<StringMap<Object>> ss = (Collection<StringMap<Object>>) t
				.get("sides");
		for (StringMap<Object> s : ss) {
			String start = (String) s.get("start");
			String end = (String) s.get("end");
			Number distance = (Number) s.get("distance");
			trajectory.addSide(start, end, distance.floatValue());
		}
		return trajectory;
	}

}
