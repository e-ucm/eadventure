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

package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.Map;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedElementImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedScene;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdCutscene;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdSlide;
import es.eucm.eadventure.common.model.impl.inventory.EAdBasicInventory;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;

public class ElementGameObjectFactoryConfigurator {

	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdScene.class, SceneGOImpl.class);
		factoryMap.put(EAdSceneImpl.class, SceneGOImpl.class);
		factoryMap.put(EAdComposedScene.class, ComposedSceneGOImpl.class);
		factoryMap.put(EAdCutscene.class, ComposedSceneGOImpl.class);
		factoryMap.put(EAdSlide.class, SceneGOImpl.class);
		factoryMap.put(EAdActorReference.class, ActorReferenceGO.class);
		factoryMap.put(EAdActorReferenceImpl.class, ActorReferenceGO.class);
		factoryMap.put(EAdBasicSceneElement.class, BasicSceneElementGO.class);
		factoryMap.put(EAdComposedElementImpl.class, ComplexSceneElementGO.class);
		factoryMap.put(EAdActor.class, ActorGO.class);
		factoryMap.put(EAdBasicActor.class, ActorGO.class);
		factoryMap.put(EAdTimer.class, TimerGO.class);
		factoryMap.put(EAdTimerImpl.class, TimerGO.class);
		factoryMap.put(EAdVideoScene.class, VideoSceneGO.class);

		factoryMap.put(EAdBasicInventory.class, BasicInventoryGO.class);
		factoryMap.put(LoadingScreen.class, SceneGOImpl.class);
	}
}
