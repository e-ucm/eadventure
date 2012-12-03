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

package ead.engine.core.factorymapproviders;

import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.ComposedScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.widgets.TextArea;
import ead.engine.core.gameobjects.go.BasicComplexSceneElementGO;
import ead.engine.core.gameobjects.go.BasicSceneElementGO;
import ead.engine.core.gameobjects.go.ComposedSceneGOImpl;
import ead.engine.core.gameobjects.go.GhostElementGO;
import ead.engine.core.gameobjects.go.SceneElementGO;
import ead.engine.core.gameobjects.go.SceneGOImpl;
import ead.engine.core.gameobjects.go.VideoSceneGO;
import ead.engine.core.gameobjects.widgets.TextAreaGO;
import ead.engine.core.platform.LoadingScreen;

public class ElementGameObjectFactoryConfigurator
		extends
		AbstractMapProvider<Class<? extends EAdSceneElement>, Class<? extends SceneElementGO<? extends EAdSceneElement>>> {

	public ElementGameObjectFactoryConfigurator() {
		factoryMap.put(SceneElement.class, BasicSceneElementGO.class);
		factoryMap.put(GhostElement.class, GhostElementGO.class);
		factoryMap.put(ComplexSceneElement.class,
				BasicComplexSceneElementGO.class);
		factoryMap.put(EAdScene.class, SceneGOImpl.class);
		factoryMap.put(BasicScene.class, SceneGOImpl.class);
		factoryMap.put(LoadingScreen.class, SceneGOImpl.class);
		factoryMap.put(ComposedScene.class, ComposedSceneGOImpl.class);
		factoryMap.put(VideoScene.class, VideoSceneGO.class);
		factoryMap.put(TextArea.class, TextAreaGO.class);

	}
}
