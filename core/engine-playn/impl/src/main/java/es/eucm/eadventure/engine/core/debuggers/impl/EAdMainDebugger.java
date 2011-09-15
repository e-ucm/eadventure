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

	@Inject
	public EAdMainDebugger() {
	}

	@Override
	public List<GameObject<?>> getGameObjects() {
		return null;
	}

}
