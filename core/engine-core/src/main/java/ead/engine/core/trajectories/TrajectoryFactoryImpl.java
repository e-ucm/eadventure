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

package ead.engine.core.trajectories;

import com.google.inject.Inject;

import ead.common.model.EAdElement;
import ead.common.model.elements.trajectories.EAdTrajectoryDefinition;
import ead.common.util.AbstractFactory;
import ead.common.util.ReflectionProvider;
import ead.engine.core.factorymapproviders.TrajectoryFactoryMapProvider;
import ead.engine.core.game.ValueMap;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.trajectories.Path;
import ead.engine.core.trajectories.TrajectoryFactory;
import ead.engine.core.trajectories.TrajectoryGenerator;

public class TrajectoryFactoryImpl extends
		AbstractFactory<TrajectoryGenerator<?>> implements TrajectoryFactory {

	@Inject
	public TrajectoryFactoryImpl(SceneElementGOFactory gameObjectFactory,
			ValueMap valueMap, ReflectionProvider interfacesProvider) {
		super(null, interfacesProvider);
		setMap(new TrajectoryFactoryMapProvider(this, gameObjectFactory,
				valueMap));
	}

	@Override
	public Path getTrajectory(EAdTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y) {

		@SuppressWarnings("unchecked")
		TrajectoryGenerator<EAdTrajectoryDefinition> generator = (TrajectoryGenerator<EAdTrajectoryDefinition>) this
				.get(trajectoryDefinition.getClass());

		return generator.getTrajectory(trajectoryDefinition, movingElement,
				x, y);
	}

	@Override
	public Path getTrajectory(EAdTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, int x, int y,
			SceneElementGO<?> sceneElement) {

		@SuppressWarnings("unchecked")
		TrajectoryGenerator<EAdTrajectoryDefinition> generator = (TrajectoryGenerator<EAdTrajectoryDefinition>) this
				.get(trajectoryDefinition.getClass());

		return generator.getTrajectory(trajectoryDefinition, movingElement,
				x, y, sceneElement);

	}

	@Override
	public boolean canGetTo(EAdTrajectoryDefinition trajectoryDefinition,
			EAdElement movingElement, SceneElementGO<?> sceneElement) {
		return false;
	}
}
