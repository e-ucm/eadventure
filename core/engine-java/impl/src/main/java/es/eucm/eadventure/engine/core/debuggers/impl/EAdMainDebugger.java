package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;

@Singleton
public class EAdMainDebugger implements EAdDebugger {

	private static List<Class<? extends EAdDebugger>> debuggersClass = new ArrayList<Class<? extends EAdDebugger>>();

	public static void addDebugger(Class<? extends EAdDebugger> debugger) {
		debuggersClass.add(debugger);
	}

	private List<EAdDebugger> debuggers;

	private List<GameObject<?>> gameObjects;

	@Inject
	public EAdMainDebugger(Injector injector) {
		debuggers = new ArrayList<EAdDebugger>();
		for (Class<? extends EAdDebugger> c : debuggersClass) {
			debuggers.add(injector.getInstance(c));
		}

		gameObjects = new ArrayList<GameObject<?>>();
	}

	@Override
	public List<GameObject<?>> getGameObjects() {
		gameObjects.clear();
		for (EAdDebugger d : debuggers) {
			gameObjects.addAll(d.getGameObjects());
		}
		return gameObjects;
	}

}
