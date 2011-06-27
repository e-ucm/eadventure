package es.eucm.eadventure.common.model.events.impl;

import es.eucm.eadventure.common.model.events.EAdSceneElementTimedEvent;

public class EAdSceneElementTimedEventImpl extends AbstractEAdEvent implements
		EAdSceneElementTimedEvent {

	private int time;

	private int repeats;

	/**
	 * Creates an event with time of 1 second and infinite repeats
	 * 
	 * @param id
	 *            the id for the element
	 */
	public EAdSceneElementTimedEventImpl(String id) {
		super(id);
		repeats = -1;
		time = 1000;
	}

	@Override
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public int getRepeats() {
		return repeats;
	}
	
	public void setRepeats( int repeats ){
		this.repeats = repeats;
	}

}
