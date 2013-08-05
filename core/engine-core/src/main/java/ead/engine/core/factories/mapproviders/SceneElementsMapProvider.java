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

import es.eucm.ead.model.elements.debuggers.FieldsDebugger;
import es.eucm.ead.model.elements.debuggers.GhostDebugger;
import es.eucm.ead.model.elements.debuggers.ProfilerDebugger;
import es.eucm.ead.model.elements.debuggers.TrajectoryDebugger;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.predef.LoadingScreen;
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.VideoScene;
import es.eucm.ead.model.elements.transitions.DisplaceTransition;
import es.eucm.ead.model.elements.transitions.EmptyTransition;
import es.eucm.ead.model.elements.transitions.FadeInTransition;
import es.eucm.ead.model.elements.transitions.MaskTransition;
import es.eucm.ead.model.elements.transitions.ScaleTransition;
import es.eucm.ead.model.elements.widgets.TextArea;
import ead.engine.core.gameobjects.debuggers.FieldsDebuggerGO;
import ead.engine.core.gameobjects.debuggers.GhostDebuggerGO;
import ead.engine.core.gameobjects.debuggers.ProfilerDebuggerGO;
import ead.engine.core.gameobjects.debuggers.TrajectoryDebuggerGO;
import ead.engine.core.gameobjects.sceneelements.GhostElementGO;
import ead.engine.core.gameobjects.sceneelements.GroupElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.engine.core.gameobjects.sceneelements.VideoSceneGO;
import ead.engine.core.gameobjects.sceneelements.huds.MouseHudGO;
import ead.engine.core.gameobjects.sceneelements.transitions.DisplaceTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.EmptyTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.FadeInTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.MaskTransitionGO;
import ead.engine.core.gameobjects.sceneelements.transitions.ScaleTransitionGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;

public class SceneElementsMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdSceneElement>, Class<? extends SceneElementGO>> {

	public SceneElementsMapProvider() {
		factoryMap.put(SceneElement.class, SceneElementGO.class);
		factoryMap.put(GhostElement.class, GhostElementGO.class);
		factoryMap.put(GroupElement.class, GroupElementGO.class);
		factoryMap.put(EAdScene.class, SceneGO.class);
		factoryMap.put(BasicScene.class, SceneGO.class);
		factoryMap.put(LoadingScreen.class, SceneGO.class);
		factoryMap.put(VideoScene.class, VideoSceneGO.class);
		factoryMap.put(TextArea.class, TextAreaGO.class);
		// Transitions
		factoryMap.put(EmptyTransition.class, EmptyTransitionGO.class);
		factoryMap.put(DisplaceTransition.class, DisplaceTransitionGO.class);
		factoryMap.put(FadeInTransition.class, FadeInTransitionGO.class);
		factoryMap.put(MaskTransition.class, MaskTransitionGO.class);
		factoryMap.put(ScaleTransition.class, ScaleTransitionGO.class);
		// Huds
		factoryMap.put(MouseHud.class, MouseHudGO.class);

		// Debuggers
		factoryMap.put(TrajectoryDebugger.class, TrajectoryDebuggerGO.class);
		factoryMap.put(GhostDebugger.class, GhostDebuggerGO.class);
		factoryMap.put(FieldsDebugger.class, FieldsDebuggerGO.class);
		factoryMap.put(ProfilerDebugger.class, ProfilerDebuggerGO.class);
	}
}
