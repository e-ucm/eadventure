package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdComplexBlockingEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.effects.impl.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.EAdQuitGame;
import es.eucm.eadventure.common.model.effects.impl.EAdRandomEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdHighlightActorReference;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeVarValueEffect;
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
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSystemEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimerEventImpl;
import es.eucm.eadventure.common.model.impl.inventory.EAdBasicInventory;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.TimerGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.BasicSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComplexSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.ComposedSceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.CancelEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeAppearanceGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeScreenGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ChangeVariableGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ComplexBlockingEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.HighlightEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ModifyActorStateGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.MoveSceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.QuitGameEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.RandomEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowActionsEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.ShowTextEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.TriggerMacroEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.VarInterpolationGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.WaitEffectGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.ConditionEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SystemEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.TimerEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.inventory.BasicInventoryGO;

@Singleton
public class GameObjectFactoryMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>> {

	private static Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> tempMap = new HashMap<Class<? extends EAdElement>, Class<? extends GameObject<?>>>();

	@Inject
	public GameObjectFactoryMapProvider() {
		super();

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

		configureEffectGameObjects();
		configureEventGameObjects();

		factoryMap.putAll(tempMap);
	}

	protected void configureEffectGameObjects() {
		factoryMap.put(EAdShowText.class, ShowTextEffectGO.class);
		factoryMap.put(EAdComplexBlockingEffect.class,
				ComplexBlockingEffectGO.class);
		factoryMap.put(EAdChangeScene.class, ChangeScreenGO.class);
		factoryMap.put(EAdMoveSceneElement.class, MoveSceneElementGO.class);
		factoryMap.put(EAdHighlightActorReference.class,
				HighlightEffectGO.class);
		factoryMap.put(EAdWaitEffect.class, WaitEffectGO.class);
		factoryMap.put(EAdCancelEffect.class, CancelEffectGO.class);
		factoryMap.put(EAdChangeVarValueEffect.class, ChangeVariableGO.class);
		factoryMap.put(EAdActorActionsEffect.class, ShowActionsEffectGO.class);
		factoryMap.put(EAdQuitGame.class, QuitGameEffectGO.class);
		factoryMap.put(EAdChangeAppearance.class, ChangeAppearanceGO.class);
		factoryMap.put(EAdTriggerMacro.class, TriggerMacroEffectGO.class);
		factoryMap.put(EAdModifyActorState.class, ModifyActorStateGO.class);
		factoryMap.put(EAdRandomEffect.class, RandomEffectGO.class);
		factoryMap.put(EAdVarInterpolationEffect.class, VarInterpolationGO.class);
	}

	protected void configureEventGameObjects() {
		factoryMap.put(EAdConditionEventImpl.class, ConditionEventGO.class);
		factoryMap.put(EAdSystemEventImpl.class, SystemEventGO.class);
		factoryMap.put(EAdTimerEvent.class, TimerEventGO.class);
		factoryMap.put(EAdTimerEventImpl.class, TimerEventGO.class);
		factoryMap.put(EAdSceneElementEvent.class, SceneElementEventGO.class);
		factoryMap.put(EAdSceneElementEventImpl.class, SceneElementEventGO.class);
	}

	public static void add(Class<? extends EAdElement> element,
			Class<? extends GameObject<?>> gameobject) {
		tempMap.put(element, gameobject);
	}

}
