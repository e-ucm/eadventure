package es.eucm.eadventure.engine.core.gameobjects.impl.events;

import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.engine.core.GameState;

public class SceneElementEventGO extends AbstractEventGO<EAdSceneElementEvent>{
	
	private boolean firstCheck = true;
	
	@Override
	public void update(GameState state) {
		super.update(state);
		if ( firstCheck ){
			firstCheck = false;
			runEffects(element.getEffects(SceneElementEvent.ADDED_TO_SCENE));
		}
	}

}
