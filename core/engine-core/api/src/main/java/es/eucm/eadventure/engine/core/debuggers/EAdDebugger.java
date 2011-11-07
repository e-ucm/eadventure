package es.eucm.eadventure.engine.core.debuggers;

import java.util.List;

import es.eucm.eadventure.engine.core.gameobjects.DrawableGO;

/**
 * General interface for eAdventure debuggers. The debugger is intended to help
 * the development of games
 * 
 * 
 */
public interface EAdDebugger {

	/**
	 * Additional game objects that must be rendered during debugging
	 * 
	 * @return
	 */
	public List<DrawableGO<?>> getGameObjects();

}
