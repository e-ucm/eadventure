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

package es.eucm.ead.model.elements.effects;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.transitions.EmptyTransition;
import es.eucm.ead.model.elements.transitions.Transition;
import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

/**
 * <p>
 * Change the current scene, if the next scene is set to null, go to previous
 * possible scene. Next scene can be defined with an element (an EAdField, an
 * Scene) or with the scene id
 * </p>
 */
@Element
public class ChangeSceneEf extends Effect {

	@Param
	private String nextSceneId;

	@Param
	private Transition transition;

	/**
	 * Construct a new EAdChangeScene effect
	 */
	public ChangeSceneEf() {
		this(null, EmptyTransition.instance());
	}

	/**
	 * Construct a new EAdChangeScene effect
	 *
	 * @param nextScene  The next scene where to go, can be null to go back to previous
	 * @param transition the transition
	 */
	public ChangeSceneEf(BasicElement nextScene, Transition transition) {
		super();
		this.nextSceneId = nextScene == null ? null : nextScene.getId();
		this.transition = transition;
	}

	public ChangeSceneEf(BasicElement scene) {
		this(scene, EmptyTransition.instance());
	}

	/**
	 * @return the nextScene. It could be a scene, or a field pointing to a
	 *         scene
	 */
	public String getNextSceneId() {
		return nextSceneId;
	}

	/**
	 * @param nextScene the nextScene to set. It should be an Scene or a field with
	 *                  Scene type. If it is neither, then the effect returns to
	 *                  the previous scene
	 */
	public void setNextScene(BasicElement nextScene) {
		this.nextSceneId = nextScene == null ? null : nextScene.getId();
	}

	public void setNextSceneId(String nextScene) {
		this.nextSceneId = nextScene;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public String toString() {
		return "nextScene:" + nextSceneId;
	}

}
