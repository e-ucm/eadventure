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

package es.eucm.ead.engine.gameobjects.trajectories;

import java.util.ArrayList;
import java.util.List;

import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.trajectories.EAdTrajectory;

public abstract class AbstractTrajectoryGO<T extends EAdTrajectory> implements
		TrajectoryGO<T> {

	protected GameState gameState;

	protected T trajectory;

	protected SceneElementGO movingElement;

	protected EAdSceneElement sceneElement;

	protected float destinationX;

	protected float destinationY;

	protected SceneElementGO target;

	protected List<Float> currentPath;

	protected SceneElementFactory sceneElementFactory;

	public AbstractTrajectoryGO(GameState gameState,
			SceneElementFactory sceneElementFactory) {
		this.sceneElementFactory = sceneElementFactory;
		this.gameState = gameState;
		this.currentPath = new ArrayList<Float>();
	}

	public void setElement(T transition) {
		this.trajectory = transition;
	}

	public T getElement() {
		return trajectory;
	}

	@Override
	public void set(SceneElementGO movingElement, float destinationX,
			float destinationY, SceneElementGO target) {
		this.movingElement = movingElement;
		this.sceneElement = movingElement.getElement();
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.target = target;
	}

	public List<Float> getCurrentPath() {
		return currentPath;
	}

	public void release() {

	}

}
