package es.eucm.eadventure.common.model.weev.story.impl;

import es.eucm.eadventure.common.model.weev.common.Retractable;

public class AbstractRetractableStoryElement extends AbstractStoryElement
		implements Retractable {

	private Boolean retracted;
	
	@Override
	public void setRetracted(Boolean retracted) {
		this.retracted = retracted;
	}

	@Override
	public Boolean isRetracted() {
		return retracted;
	}

}
