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
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComposedScene;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdTimerImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
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

public class ElementGameObjectFactoryConfigurator {

	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdScene.class, SceneGOImpl.class);
		factoryMap.put(EAdSceneImpl.class, SceneGOImpl.class);
		factoryMap.put(EAdComposedScene.class, ComposedSceneGOImpl.class);
		factoryMap.put(EAdActorReference.class, ActorReferenceGO.class);
		factoryMap.put(EAdActorReferenceImpl.class, ActorReferenceGO.class);
		factoryMap.put(EAdBasicSceneElement.class, BasicSceneElementGO.class);
		factoryMap.put(EAdComplexSceneElement.class,
				ComplexSceneElementGO.class);
		factoryMap.put(EAdActor.class, ActorGO.class);
		factoryMap.put(EAdBasicActor.class, ActorGO.class);
		factoryMap.put(EAdTimer.class, TimerGO.class);
		factoryMap.put(EAdTimerImpl.class, TimerGO.class);
		factoryMap.put(EAdVideoScene.class, VideoSceneGO.class);

		factoryMap.put(EAdBasicInventory.class, BasicInventoryGO.class);
	}
}
