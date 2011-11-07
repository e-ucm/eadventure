package es.eucm.eadventure.engine.core;

import es.eucm.eadventure.engine.core.gameobjects.factories.EffectGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;

public interface PluginHandler {
	
	void install( EffectGOFactory effectFactory );
	
	void install( SceneElementGOFactory sceneElementFactory );
	
	void install( EventGOFactory eventGOFactory );

}
