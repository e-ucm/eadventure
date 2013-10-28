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

package es.eucm.ead.model.elements.effects.physics;

import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

@Element
public class PhysicsEf extends Effect {

	public static final String VAR_PH_TYPE = "ph_type";

	public static final String VAR_PH_SHAPE = "ph_shape";

	public static final String VAR_PH_FRICTION = "ph_friction";

	public static final String VAR_PH_RESTITUTION = "ph_restitution";

	public static final String VAR_PH_DENSITY = "ph_restitution";

	/**
	 * Elements that are affect by the physics
	 */
	@Param
	private EAdList<SceneElement> elements;

	@Param
	private EAdList<SceneElement> joints;

	public PhysicsEf() {
		super();
		elements = new EAdList<SceneElement>();
		joints = new EAdList<SceneElement>();
	}

	public void addSceneElement(SceneElement element) {
		this.elements.add(element);
	}

	public void addJoint(SceneElement e1, SceneElement e2) {
		joints.add(e1);
		joints.add(e2);
	}

	public EAdList<SceneElement> getElements() {
		return elements;
	}

	public EAdList<SceneElement> getJoints() {
		return joints;
	}

	public void setElements(EAdList<SceneElement> elements) {
		this.elements = elements;
	}

	public void setJoints(EAdList<SceneElement> joints) {
		this.joints = joints;
	}

}
