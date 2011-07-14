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

package es.eucm.eadventure.common.model.elements.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.impl.AbstractEAdElement;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.trajectories.TrajectoryGenerator;
import es.eucm.eadventure.common.model.variables.impl.vars.BooleanVar;
import es.eucm.eadventure.common.model.variables.impl.vars.IntegerVar;

public class EAdComposedScene extends AbstractEAdElement implements EAdScene {

	protected EAdList<EAdScene> scenes;

	/**
	 * Pointer to the variable that manages the current scene
	 */
	@Param("currentScene")
	protected IntegerVar currentScene;
	
	private BooleanVar sceneLoaded;

	public EAdComposedScene(String id) {
		super(id);
		scenes = new EAdListImpl<EAdScene>(EAdScene.class);
		currentScene = new IntegerVar("currentScene");
		sceneLoaded = new BooleanVar("sceneLoaded");
	}

	@Override
	public EAdList<EAdSceneElement> getSceneElements() {
		return null;
	}

	@Override
	public EAdSceneElement getBackground() {
		return null;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
	
	public IntegerVar currentSceneVar() {
		return currentScene;
	}

	public EAdList<EAdScene> getScenes() {
		return scenes;
	}

	@Override
	public BooleanVar sceneLoaded() {
		return sceneLoaded;
	}

	@Override
	public TrajectoryGenerator getTrajectoryGenerator() {
		return null;
	}

}
