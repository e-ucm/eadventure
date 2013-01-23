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

import ead.common.model.elements.debuggers.TrajectoryDebugger;
import ead.common.model.elements.huds.InventoryHud;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.EmptyTransition;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.widgets.TextArea;
import ead.engine.core.debuggers.TrajectoryDebuggerGO;
import ead.engine.core.gameobjects.huds.InventoryHUDGO;
import ead.engine.core.gameobjects.sceneelements.GhostElementGO;
import ead.engine.core.gameobjects.sceneelements.GroupElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGOImpl;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.gameobjects.sceneelements.VideoSceneGO;
import ead.engine.core.gameobjects.sceneelements.transitions.BasicTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.FadeInTransitionGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;
import ead.engine.core.platform.LoadingScreen;

public class SceneElementsMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdSceneElement>, Class<? extends SceneElementGO<? extends EAdSceneElement>>> {

	public SceneElementsMapProvider() {
		factoryMap.put(SceneElement.class, SceneElementGOImpl.class);
		factoryMap.put(GhostElement.class, GhostElementGO.class);
		factoryMap.put(GroupElement.class, GroupElementGO.class);
		factoryMap.put(EAdScene.class, SceneGO.class);
		factoryMap.put(BasicScene.class, SceneGO.class);
		factoryMap.put(LoadingScreen.class, SceneGO.class);
		factoryMap.put(VideoScene.class, VideoSceneGO.class);
		factoryMap.put(TextArea.class, TextAreaGO.class);
		// Transitions
		factoryMap.put(EmptyTransition.class, BasicTransitionGO.class);
		factoryMap.put(DisplaceTransition.class, DisplaceTransitionGO.class);
		factoryMap.put(FadeInTransition.class, FadeInTransitionGO.class);
		// Huds
		factoryMap.put(InventoryHud.class, InventoryHUDGO.class);
		// Debuggers
		factoryMap.put(TrajectoryDebugger.class, TrajectoryDebuggerGO.class);
	}
}
