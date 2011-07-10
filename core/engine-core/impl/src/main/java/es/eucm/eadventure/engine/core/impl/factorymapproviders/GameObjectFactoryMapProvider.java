package es.eucm.eadventure.engine.core.impl.factorymapproviders;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public class GameObjectFactoryMapProvider
		extends
		AbstractMapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>> {

	private static Map<Class<? extends EAdElement>, Class<? extends GameObject<?>>> tempMap = new HashMap<Class<? extends EAdElement>, Class<? extends GameObject<?>>>();
	
	@Inject
	public GameObjectFactoryMapProvider(ElementGameObjectFactoryConfigurator elementGOFC,
			EffectGameObjectFactoryConfigurator effectGOFC,
			EventGameObjectFactoryConfigurator eventGOFC) {
		super();
		
		elementGOFC.configure(factoryMap);
		effectGOFC.configure(factoryMap);
		eventGOFC.configure(factoryMap);
		

		factoryMap.putAll(tempMap);
	}

	public static void add(Class<? extends EAdElement> element,
			Class<? extends GameObject<?>> gameobject) {
		tempMap.put(element, gameobject);
	}

}
