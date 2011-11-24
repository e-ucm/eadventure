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

package es.eucm.eadventure.common.model.effects.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.transitions.EAdTransition;

/**
 * <p>Change the current scene, if the next scene is set to null, go to
 * previous possible scene.</p>
 *
 */
@Element(runtime = EAdChangeScene.class, detailed = EAdChangeScene.class)
public class EAdChangeScene extends AbstractEAdEffect {

	@Param("nextScreen")
	private EAdScene nextScene;
	
	@Param("transition")
	private EAdTransition transition;
	
	/**
	 * Construct a new EAdChangeScene effect
	 * 
	 * @param id
	 * 			The id of the effect
	 */
	public EAdChangeScene() {
		this(null, EAdTransition.BASIC);
	}

	/**
	 * Construct a new EAdChangeScene effect
	 * 
	 * @param id
	 * 			The id of the effect
	 * @param nextScene
	 * 			The next scene where to go, can be null to go back to previous
	 */
	public EAdChangeScene( EAdScene nextScene, EAdTransition transition) {
		super();
		setId("changeScene");
		this.nextScene = nextScene;
		this.transition = transition;
	}

	/**
	 * @return the nextScene
	 */
	public EAdScene getNextScene() {
		return nextScene;
	}

	/**
	 * @param nextScene the nextScene to set
	 */
	public void setNextScene(EAdScene nextScene) {
		this.nextScene = nextScene;
	}

	public EAdTransition getTransition() {
		return transition;
	}
	
	public void setTransition(EAdTransition transition) {
		this.transition = transition;
	}
	
}
