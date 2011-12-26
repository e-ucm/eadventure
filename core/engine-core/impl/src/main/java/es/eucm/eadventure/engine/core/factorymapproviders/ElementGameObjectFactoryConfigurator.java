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

package es.eucm.eadventure.engine.core.factorymapproviders;

import es.eucm.eadventure.common.model.elements.VideoScene;
import es.eucm.eadventure.common.model.elements.extra.EAdCutscene;
import es.eucm.eadventure.common.model.elements.extra.EAdSlide;
import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scenes.ComplexSceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.ComposedScene;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.engine.core.gameobjects.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;

public class ElementGameObjectFactoryConfigurator
		extends
		AbstractMapProvider<Class<? extends EAdSceneElement>, Class<? extends SceneElementGO<? extends EAdSceneElement>>> {

	public ElementGameObjectFactoryConfigurator() {
		factoryMap.put(EAdScene.class, SceneGOImpl.class);
		factoryMap.put(SceneImpl.class, SceneGOImpl.class);
		factoryMap.put(ComposedScene.class, ComposedSceneGOImpl.class);
		factoryMap.put(EAdCutscene.class, ComposedSceneGOImpl.class);
		factoryMap.put(EAdSlide.class, SceneGOImpl.class);
		factoryMap.put(SceneElementImpl.class, BasicSceneElementGO.class);
		factoryMap
				.put(ComplexSceneElementImpl.class, ComplexSceneElementGO.class);
		// FIXME remove timers
		// factoryMap.put(EAdTimer.class, TimerGO.class);
		// factoryMap.put(EAdTimerImpl.class, TimerGO.class);
		
		factoryMap.put(VideoScene.class, VideoSceneGO.class);
		factoryMap.put(LoadingScreen.class, SceneGOImpl.class);
	}
}
