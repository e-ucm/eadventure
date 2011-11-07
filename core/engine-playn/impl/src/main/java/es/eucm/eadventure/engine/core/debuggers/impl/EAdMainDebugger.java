package es.eucm.eadventure.engine.core.debuggers.impl;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.debuggers.EAdDebugger;
import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;

@Singleton
public class EAdMainDebugger implements EAdDebugger {

	@Inject
	public EAdMainDebugger() {
	}

	@Override
	public List<DrawableGO<?>> getGameObjects() {
		return null;
	}

}
