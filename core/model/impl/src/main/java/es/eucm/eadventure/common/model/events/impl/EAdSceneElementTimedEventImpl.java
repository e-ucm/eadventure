package es.eucm.eadventure.common.model.events.impl;

import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent;

public class EAdSceneElementTimedEventImpl extends AbstractEAdEvent implements EAdSceneElementTimedEvent {
	
	private int time;

	public EAdSceneElementTimedEventImpl(String id) {
		super(id);
	}

	@Override
	public int getTime() {
		return time;
	}
	
	public void setTime( int time ){
		this.time = time;
	}

}
