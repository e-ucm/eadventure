package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.Map;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent;
import es.eucm.eadventure.common.model.events.EAdTimerEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementTimedEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSystemEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdTimerEventImpl;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.ConditionEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SceneElementTimedEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.SystemEventGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.TimerEventGO;

public class EventGameObjectFactoryConfigurator {
	
	public void configure(
			Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> factoryMap) {
		factoryMap.put(EAdConditionEventImpl.class, ConditionEventGO.class);
		factoryMap.put(EAdSystemEventImpl.class, SystemEventGO.class);
		factoryMap.put(EAdTimerEvent.class, TimerEventGO.class);
		factoryMap.put(EAdTimerEventImpl.class, TimerEventGO.class);
		factoryMap.put(EAdSceneElementEvent.class, SceneElementEventGO.class);
		factoryMap.put(EAdSceneElementEventImpl.class, SceneElementEventGO.class);
		factoryMap.put(EAdSceneElementTimedEvent.class, SceneElementTimedEventGO.class );
		factoryMap.put(EAdSceneElementTimedEventImpl.class, SceneElementTimedEventGO.class );	}

}
